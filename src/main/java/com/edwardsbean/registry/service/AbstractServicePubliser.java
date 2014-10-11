package com.edwardsbean.registry.service;

import java.net.InetSocketAddress;

import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by edwardsbean on 14-9-5.
 */
public abstract class AbstractServicePubliser implements ServicePubliser {
    private static final Logger log = LoggerFactory.getLogger(AbstractServicePubliser.class);

    protected ServiceDefine serviceDefine;
    protected ServiceInstance serviceInstance;
    protected Object serviceImpl;

    public AbstractServicePubliser() {
    }

    public AbstractServicePubliser(ServiceDefine serviceDefine, ServiceInstance serviceInstance, Object serviceImpl) {
        this.serviceDefine = serviceDefine;
        this.serviceInstance = serviceInstance;
        this.serviceImpl = serviceImpl;
    }


    protected void startService() throws Exception {
        Class serviceClass = Class.forName(serviceDefine.serviceClass);
        Server server = new NettyServer(
                new SpecificResponder(serviceClass, serviceImpl),
                new InetSocketAddress(serviceInstance.hostname, serviceInstance.port)
        );
        server.start();
        log.info(serviceDefine.serviceName + " remote server started, listen on " + serviceInstance.hostname + ":" + serviceInstance.port);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "{" +
                "serviceDefine=" + serviceDefine +
                ", serviceInstance=" + serviceInstance +
                ", serviceImpl=" + serviceImpl +
                '}';
    }
}
