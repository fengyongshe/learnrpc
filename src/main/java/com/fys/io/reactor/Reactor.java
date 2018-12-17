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

public class Reactor implements Runnable {

  ServerSocketChannel serverChannel;
  Selector selector;

  private Reactor() throws Exception {
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
          new Handler(sc, selector).run();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  class Handler {

    SocketChannel sc  = null;
    Selector selector = null;

    Handler(SocketChannel sc, Selector selector) {
      this.sc = sc;
      this.selector = selector;
    }

    public void run() throws Exception {
      ByteBuffer buf = ByteBuffer.allocate(1024);
      int len = 0;
      System.out.println("Read Msg from Channel!");
      while((len = sc.read(buf)) > 0) {
        buf.flip();
        byte[] bytes = new byte[1024];
        buf.get(bytes,0, len);
        System.out.println(new String(bytes,0, len));
      }
    }
  }

  public static void main(String[] args) throws Exception {
    new Reactor().run();
  }
}
