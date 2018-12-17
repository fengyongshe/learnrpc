package com.fys.io;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestFileChannel {

  public static void main(String[] args) throws Exception {

    FileInputStream fin = new FileInputStream("e:\\fileop.txt");
    FileChannel fc = fin.getChannel();
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    fc.read(buffer);
    buffer.flip();
    while(buffer.remaining() > 0) {
      byte b = buffer.get();
      System.out.println((char)b);
    }
    fin.close();
  }
}
