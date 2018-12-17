package com.fys.io.bio;

import java.io.*;
import java.net.Socket;

public class BIOEchoClient {

  public static void main(String[] args) throws IOException {
    int port = 8082;
    String serverIP = "127.0.0.1";
    Socket socket = null;
    BufferedReader reader = null;
    BufferedWriter writer = null;
    try {
      socket = new Socket(serverIP,port);
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      writer.write("Hello, Block IO.\n");
      writer.flush();
      String echo = reader.readLine();
      System.out.println("Echo:" + echo);
    } finally {
      if(reader != null) {
        reader.close();
      }
      if(socket != null) {
        socket.close();
      }
    }
  }
}
