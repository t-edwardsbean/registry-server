package com.edwardsbean.registry.example;

import org.apache.avro.AvroRemoteException;
import org.springframework.stereotype.Service;

/**
 * Created by edwardsbean on 14-9-5.
 */
@Service("service")
public class HelloServiceImpl implements HelloService  {
    @Override
    public CharSequence sayHello(CharSequence name) throws AvroRemoteException {
        return "hello " + name;
    }
}
