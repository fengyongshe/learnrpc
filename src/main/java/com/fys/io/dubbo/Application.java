package com.fys.io.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;

import java.io.IOException;

public class Application {

  public static void main(String[] args) throws IOException {
    ServiceConfig<GreetingService> service = new ServiceConfig<>();
    service.setApplication(new ApplicationConfig("First-Dubbo-Provider"));
    service.setRegistry(new RegistryConfig("multicast://224.5.6.7:12340"));
    service.setInterface(GreetingService.class);
    service.setRef(new GreetingsServiceImpl());
    service.export();
    System.in.read();
  }
}
