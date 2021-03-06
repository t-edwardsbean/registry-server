package com.edwardsbean.registry.service;

import com.edwardsbean.registry.constant.Registry;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 服务发布者,用于向zookeeper注册服务器注册服务
 * Created by edwardsbean on 14-9-5.
 */
@Component("zookeeperServicePubliser")
public class ZookeeperServicePubliser extends AbstractServicePubliser {
    private static final Logger log = LoggerFactory.getLogger(ZookeeperServicePubliser.class);
    //注册服务器ip
    @Value("${rs.registry.host}") private String host;

    public ZookeeperServicePubliser(){
    }

    /**
     * 自动注入需要的配置，包括服务定义，
     * @param serviceDefine
     * @param serviceInstance
     * @param serviceImpl
     */
    @Autowired(required = true)
    public ZookeeperServicePubliser(@Qualifier("serviceDefine")ServiceDefine serviceDefine,@Qualifier("serviceInstance")ServiceInstance serviceInstance,@Qualifier("service")Object serviceImpl){
        super(serviceDefine, serviceInstance, serviceImpl);
    }

    @Override
    public void publishService() throws Exception{
        //启动服务
        startService();
        ZkClient zkClient = new ZkClient(host,2000, 2000, new SerializableSerializer());
        String servicePath = Registry.ROOT + "/" + serviceDefine.getServiceName() ;
        //创建服务根节点
        zkClient.createPersistent(servicePath,true);
        //设置服务信息
        zkClient.writeData(servicePath,serviceDefine);
        //创建服务实例
        String createPath = zkClient.createEphemeralSequential(servicePath + "/", serviceInstance);
        log.info("注册服务实例成功，path:" + createPath);
    }


}
