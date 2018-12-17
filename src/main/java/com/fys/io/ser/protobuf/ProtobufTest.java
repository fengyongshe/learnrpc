package com.fys.io.ser.protobuf;

public class ProtobufTest {

  public static void main(String[] args) throws Exception {
    PersonModel.Person.Builder builder = PersonModel.Person.newBuilder();
    builder.setId(1);
    builder.setName("Test");
    builder.setEmail("Test@Test.com");
    PersonModel.Person person = builder.build();
    System.out.println("befrore:" + person);

    System.out.println("Person Bytes:");
    for(byte b : person.toByteArray()) {
      System.out.println(b);
    }

    System.out.println("====================");
    byte[] byteArray = person.toByteArray();
    PersonModel.Person p2 = PersonModel.Person.parseFrom(byteArray);
    System.out.println("After Persion Name:" + p2.getName());

  }
}
