package com.fys.io.proxy;

public class RealSubject implements Subject {
  @Override
  public void hello(String msg) {
    System.out.println("Hello " + msg);
  }
}
