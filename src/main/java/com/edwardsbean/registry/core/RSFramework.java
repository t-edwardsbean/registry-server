package com.edwardsbean.registry.core;

import com.edwardsbean.registry.service.ZookeeperServicePubliser;
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
public final class RSFramework implements ApplicationListener {
    private static final Logger log = LoggerFactory.getLogger(RSFramework.class);
    /**
     * TODO:
     * 根据配置文件读取对应的注册中心类型
     */
    @Autowired
    ZookeeperServicePubliser zookeeperServicePubliser;

    @Autowired
    Config config;

    private void start() {
        try {
            zookeeperServicePubliser.publishService();
            System.gc();
        } catch (Exception e) {
            log.error("注册服务失败：", e);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ContextStartedEvent) {
            log.debug("捕获启动事件");
            if (config.serviceName != null) {
                log.info("注册服务:" + config.serviceName);
                start();
            }
        }
    }
}
