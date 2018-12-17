package com.fys.io.dubbo;

public class GreetingsServiceImpl implements GreetingService {
  @Override
  public String sayHi(String name) {
    return "Hi, " + name;
  }
}
