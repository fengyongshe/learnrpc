package com.fys.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TestFileStream {

  public static void main(String[] args) throws Exception {
    File f = new File("e:\\fileop.txt");
    InputStream in = new FileInputStream(f);
    byte b[] = new byte[(int)f.length()];
    in.read(b);
    in.close();
    System.out.println(new String(b));
  }
}
