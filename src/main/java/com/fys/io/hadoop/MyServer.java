package com.fys.io.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtocolSignature;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyServer implements MyProtocol {

  private Server server;
  public MyServer() throws Exception {
    Configuration conf = new Configuration();
    server = new RPC.Builder(conf)
        .setProtocol(MyProtocol.class)
        .setInstance(this)
        .setBindAddress("127.0.0.1")
        .setPort(12341)
        .build();
    server.start();
    System.out.println("Server is running: "
        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    server.join();
  }

  @Override
  public String sayHi(String msg) {
    return "MyServer msg:"+msg;
  }

  @Override
  public long getProtocolVersion(String s, long l) throws IOException {
    return MyProtocol.versionID;
  }

  @Override
  public ProtocolSignature getProtocolSignature(String s, long l, int i) throws IOException {
    return new ProtocolSignature(MyProtocol.versionID, null);
  }

  public static void main(String[] args) throws Exception {
    new MyServer();
  }
}
