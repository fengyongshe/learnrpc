package com.fys.io.ser;

import java.io.Serializable;

public class Employee implements Serializable {

  public String name;
  public String address;
  public transient int SSN;

  public void mailCheck() {
    System.out.println("Mailing a check to " + name + " " + address);
  }

}
