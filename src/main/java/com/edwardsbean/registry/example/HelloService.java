/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.edwardsbean.registry.example;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public interface HelloService {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"HelloService\",\"namespace\":\"com.edwardsbean.registry.example\",\"types\":[],\"messages\":{\"sayHello\":{\"request\":[{\"name\":\"name\",\"type\":\"string\"}],\"response\":\"string\"}}}");
  java.lang.CharSequence sayHello(java.lang.CharSequence name) throws org.apache.avro.AvroRemoteException;

  @SuppressWarnings("all")
  public interface Callback extends HelloService {
    public static final org.apache.avro.Protocol PROTOCOL = com.edwardsbean.registry.example.HelloService.PROTOCOL;
    void sayHello(java.lang.CharSequence name, org.apache.avro.ipc.Callback<java.lang.CharSequence> callback) throws java.io.IOException;
  }
}