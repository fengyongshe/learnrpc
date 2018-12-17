package com.fys.io.reactor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiMultiReactor implements Runnable {

  ServerSocketChannel serverChannel;
  Selector selector;
  ExecutorService executorService = null;

  private MultiMultiReactor() throws Exception {
    executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    serverChannel = ServerSocketChannel.open();
    serverChannel.bind(new InetSocketAddress(9898));
    serverChannel.configureBlocking(false);
    selector = Selector.open();
    System.out.println("Selector Open Success");
    SelectionKey sk = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    sk.attach(new MultiworkThreadAcceptor(serverChannel));
  }

  @Override
  public void run() {
    try {
      while (!Thread.interrupted()) {
        selector.select();
        Set<SelectionKey> selected = selector.selectedKeys();
        Iterator it = selected.iterator();
        while (it.hasNext()) {
          dispatch((SelectionKey) it.next());
          it.remove();
        }
      }
    } catch (IOException ex) {

    }
  }

  void dispatch(SelectionKey selectKey ) {
    Runnable runnable = (Runnable) selectKey.attachment();
    if(runnable!= null) {
      runnable.run();
    }
  }


  class MultiworkThreadAcceptor implements Runnable {

    ServerSocketChannel serverSocket = null;
    int workCount = Runtime.getRuntime().availableProcessors();
    SubReactor[] workThreadHandlers = new SubReactor[workCount];
    volatile int nextHandler = 0;

    public MultiworkThreadAcceptor(ServerSocketChannel serverSocket) {
      this.serverSocket = serverSocket;
      this.init();
    }

    public void init( ) {
      nextHandler = 0;
      for(int i = 0;i < workThreadHandlers.length;i++) {
        try {
          workThreadHandlers[i] = new SubReactor();
        } catch (Exception ex) {

        }
      }
    }

    @Override
    public void run() {
      try {
        SocketChannel c = serverSocket.accept();
        if (c != null) {// 注册读写
          synchronized (c) {
            SubReactor work = workThreadHandlers[nextHandler];
            work.registerChannel(c);
            nextHandler++;
            if (nextHandler >= workThreadHandlers.length) {
              nextHandler = 0;
            }
          }
        }
      } catch (Exception e) {
      }
    }
  }

  class SubReactor implements Runnable {
    final Selector mySelector;
    int workCount = Runtime.getRuntime().availableProcessors();
    ExecutorService executorService = Executors.newFixedThreadPool(workCount);

    public SubReactor() throws Exception {
      this.mySelector = SelectorProvider.provider().openSelector();
    }

    public void registerChannel(SocketChannel sc) throws Exception {
      sc.register(mySelector, SelectionKey.OP_READ | SelectionKey.OP_CONNECT);
    }

    @Override
    public void run() {
      while(true) {
        try {
          selector.select();
          Set<SelectionKey> keys = selector.selectedKeys();
          Iterator<SelectionKey> iterator = keys.iterator();
          while(iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            executorService.submit(() ->process());
          }
        } catch (Exception e) {

        }
      }
    }

    public void process( ){
      System.out.println("Read Msg from Channel!");
    }

  }

  class MultiThreadHandler implements Runnable {

    SocketChannel sc  = null;
    Selector selector = null;

    ExecutorService executorService = null;

    MultiThreadHandler(SocketChannel sc,
                       Selector selector,
                       ExecutorService executorService) {
      this.executorService = executorService;
      this.sc = sc;
      this.selector = selector;
    }

    @Override
    public void run() {
      executorService.submit(() -> process());

    }

    public void process(){
      ByteBuffer buf = ByteBuffer.allocate(1024);
      int len = 0;
      System.out.println("Read Msg from Channel!");
      try {
        while ((len = sc.read(buf)) > 0) {
          buf.flip();
          byte[] bytes = new byte[1024];
          buf.get(bytes, 0, len);
          System.out.println(new String(bytes, 0, len));
        }
      }catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws Exception {
    new MultiMultiReactor().run();
  }

}
