package com.fys.io.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

public class Consumer {

  public static void main(String[] args) {
    ReferenceConfig<GreetingService> reference = new ReferenceConfig<>();
    reference.setApplication(new ApplicationConfig("First-Dubbo-Client"));
    reference.setRegistry(new RegistryConfig("multicast://224.5.6.7:12340"));
    reference.setInterface(GreetingService.class);
    GreetingService greetingService = reference.get();
    String message = greetingService.sayHi("Dubbo");
    System.out.println(message);
  }

}
