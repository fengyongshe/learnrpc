package com.fys.io.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.net.InetSocketAddress;

public class MyClient {

  public static void main(String[] args) throws Exception {
    InetSocketAddress addr = new InetSocketAddress("127.0.0.1",12341);
    MyProtocol proxy = RPC.waitForProxy(MyProtocol.class,
        MyProtocol.versionID,
        addr,
        new Configuration());
    proxy.sayHi("MyClient Invoked");
  }
}
