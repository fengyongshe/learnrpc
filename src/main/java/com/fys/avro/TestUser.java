package com.fys.avro;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;

public class TestUser {

  public static void main(String[] args) throws IOException {

    User user1 = new User();
    user1.setName("Alysa");
    user1.setFavoriteNumber(256);

    User user2 = new User("ben",7,"red");

    User user3 = User.newBuilder()
        .setFavoriteNumber(null)
        .setFavoriteColor("blue")
        .setName("charlie")
        .build();

    DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
    DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
    dataFileWriter.create(user1.getSchema(), new File("users.avro"));
    dataFileWriter.append(user1);
    dataFileWriter.append(user2);
    dataFileWriter.append(user3);
    dataFileWriter.close();

    DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
    DataFileReader<User> dataFileReader = new DataFileReader<User>(new File("users.avro"), userDatumReader);
    User user = null;
    while(dataFileReader.hasNext()) {
      user = dataFileReader.next(user);
      System.out.println(user);
    }
  }
}
