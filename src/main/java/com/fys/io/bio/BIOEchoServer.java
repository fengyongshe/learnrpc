package com.fys.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOEchoServer {

  private static final ExecutorService executor = Executors.newCachedThreadPool();

  public static void main(String[] args) throws IOException {

    int port = 8082;
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(port);
      Socket socket = null;
      ByteBuffer bb = null;
      while(true) {
        socket = serverSocket.accept();
        executor.submit(new BIOEchoServerHandler(socket));
      }
    } finally {
      if(serverSocket != null) {
        serverSocket.close();
      }
    }
  }
}
