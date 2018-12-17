package com.fys.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

  static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

  public static void main(String[] args) throws Exception {

    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.TCP_NODELAY, true)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
              ChannelPipeline p = ch.pipeline();
              p.addLast(new EchoClientHandler());
            }
          });
        ChannelFuture f = b.connect("localhost",18007).sync();
        f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully();
    }

  }

}
