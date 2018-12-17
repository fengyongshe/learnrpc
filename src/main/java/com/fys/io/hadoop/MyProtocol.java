package com.fys.io.hadoop;

import org.apache.hadoop.ipc.VersionedProtocol;

public interface MyProtocol extends VersionedProtocol {

  public static final long versionID = 1L;

  public String sayHi(String msg);

}
