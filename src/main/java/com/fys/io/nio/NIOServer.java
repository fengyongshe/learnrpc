package com.fys.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {

  public static void main(String[] args) throws IOException {

    ServerSocketChannel ssChannel = ServerSocketChannel.open();
    ssChannel.bind(new InetSocketAddress(9898));
    ssChannel.configureBlocking(false);

    Selector selector = Selector.open();
    System.out.println("Selector Open Success");
    ssChannel.register(selector, SelectionKey.OP_ACCEPT);

    while (selector.select() > 0 ) {
      Iterator<SelectionKey> it = selector.selectedKeys().iterator();
      while(it.hasNext()) {
        SelectionKey key = it.next();
        if(key.isAcceptable()) {
          SocketChannel socketChannel = ssChannel.accept();
          socketChannel.configureBlocking(false);
          socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
          SocketChannel channel = (SocketChannel) key.channel();
          readMsg(channel);
        }
      }
      it.remove();
    }

  }

  private static void readMsg(SocketChannel channel) throws IOException {
    ByteBuffer buf = ByteBuffer.allocate(1024);
    int len = 0;
    System.out.println("Read Msg from Channel!");
    while((len = channel.read(buf)) > 0) {
      buf.flip();
      byte[] bytes = new byte[1024];
      buf.get(bytes,0,len);
      System.out.println(new String(bytes,0,len));
    }
  }

}
