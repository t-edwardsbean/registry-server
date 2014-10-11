package com.edwardsbean.registry.example;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 发布服务测试
 * Created by edwardsbean on 14-9-5.
 */
public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        context.start();
    }
}
