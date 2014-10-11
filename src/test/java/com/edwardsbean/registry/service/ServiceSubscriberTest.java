package com.edwardsbean.registry.service;

import com.edwardsbean.registry.core.RSFramework;
import com.edwardsbean.registry.example.HelloService;
import com.edwardsbean.registry.pool.FixedPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 订阅测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ServiceSubscriberTest {
    @Autowired
    RSFramework rsFramework;
    ServiceSubscriber serviceSubscriber;
    HelloService helloService;
    @Test
    public void testSubscribeService() throws Exception {
        serviceSubscriber = rsFramework.getServiceSubscriber();
        serviceSubscriber.subscribeService(HelloService.class, new FixedPool(2));
    }

    @Test
    public void testGetServiceClient() throws Exception {
        helloService = serviceSubscriber.getServiceClient(HelloService.class);
    }

    @Test
    public void testHello() throws Exception {
        helloService.sayHello("haha");
    }
}