package com.fys.io.reactor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiReactor implements Runnable {

  ServerSocketChannel serverChannel;
  Selector selector;
  ExecutorService executorService = null;

  private MultiReactor() throws Exception {
    executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    serverChannel = ServerSocketChannel.open();
    serverChannel.bind(new InetSocketAddress(9898));
    serverChannel.configureBlocking(false);
    selector = Selector.open();
    System.out.println("Selector Open Success");
    SelectionKey sk = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    sk.attach(new Acceptor());
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

  class Acceptor implements Runnable {
    @Override
    public void run(){
      try {
        SocketChannel sc = serverChannel.accept();
        if(sc != null) {
          new MultiThreadHandler(sc,selector,executorService).run();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
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
    new MultiReactor().run();
  }
}
