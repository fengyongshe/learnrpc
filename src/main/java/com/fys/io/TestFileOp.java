package com.fys.io;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TestFileOp {

  public static void main(String[] args) throws IOException {

    FileReader fr =  new FileReader("e:\\fileop.txt");
    char[] buf = new char[1024];
    int len = 0;
    while ( (len = fr.read(buf)) != -1) {
      System.out.println(new String(buf,0,len));
    }
    fr.close();

    FileWriter fw = new FileWriter("e:\\fileop.txt");
    fw.write("FW Write MSG");
    fw.close();

  }
}
