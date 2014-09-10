package com.edwardsbean.registry.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

/**
 * Registry Server Framework，用于启动注册服务框架
 * Created by edwardsbean on 14-9-5.
 */
@Component
public final class RSFramework implements ApplicationListener{
    private static final Logger log = LoggerFactory.getLogger(RSFramework.class);
    @Autowired
    ZookeeperServicePubliser zookeeperServicePubliser;

    public void start(){
        zookeeperServicePubliser.publishService();
        System.gc();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof ContextStartedEvent){
            log.debug("捕获启动事件");
            start();
        }
    }
}
