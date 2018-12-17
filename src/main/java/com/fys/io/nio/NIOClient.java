package com.fys.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Scanner;

public class NIOClient {

  public static void main(String[] args) throws IOException{
    SocketChannel sChannel = SocketChannel.open();
    sChannel.connect(new InetSocketAddress("127.0.0.1",9898));

    ByteBuffer buf = ByteBuffer.allocate(1024);
    sChannel.configureBlocking(false);

    Scanner scanner = new Scanner(System.in);
    while(scanner.hasNext()) {
      String msg = scanner.nextLine();
      buf.put((new Date() + ": " + msg).getBytes());
      buf.flip();
      sChannel.write(buf);
      buf.clear();
    }

  }
}
