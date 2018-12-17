package com.fys.io.ser;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SerializeDemo {

  public static void main(String[] args) throws Exception {
    Employee emp = new Employee();
    emp.name = "Reyan Ali";
    emp.address = "Suzhou,CN";
    emp.SSN = 112233;

    FileOutputStream fileOut = new FileOutputStream("e://employee.ser");
    ObjectOutputStream out = new ObjectOutputStream(fileOut);
    out.writeObject(emp);
    out.close();
    fileOut.close();

    System.out.println("Serialized Data is saved in c://employee.ser");
  }
}
