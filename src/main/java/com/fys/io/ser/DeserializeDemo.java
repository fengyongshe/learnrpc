package com.fys.io.ser;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class DeserializeDemo {

  public static void main(String[] args) throws Exception  {
    Employee emp = null;
    FileInputStream fileIn = new FileInputStream("e://employee.ser");
    ObjectInputStream in = new ObjectInputStream(fileIn);
    emp = (Employee) in.readObject();
    in.close();
    fileIn.close();
    System.out.println("Deserialized Employee..");
    System.out.println("Employee Info: " + emp.name + "");
    System.out.println("Employee SSN: " + emp.SSN);
  }

}
