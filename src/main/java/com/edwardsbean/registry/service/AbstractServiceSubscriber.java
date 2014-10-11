package com.edwardsbean.registry.service;

import com.edwardsbean.registry.constant.Registry;
import com.edwardsbean.registry.pool.PoolStrategy;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * 向注册中心查询服务，并订阅服务变化。将查询到的服务放在本地的连接池中，使用默认策略轮询
 * Created by edwardsbean on 14-9-9.
 */
@Component("serviceSubscriber")
public class AbstractServiceSubscriber implements ServiceSubscriber{
    private static final Logger log = LoggerFactory.getLogger(AbstractServiceSubscriber.class);
    @Value("${rs.registry.host}")
    private String host;
    @Value("${rs.service.name}")
    private String serviceName;
    private ZkClient zkClient;

    /**
     * 订阅服务
     *
     * @param serviceClass
     * @param poolStrategy
     * @throws Exception
     */
    @Override
    public void subscribeService(Class<?> serviceClass, PoolStrategy poolStrategy) throws Exception {
        if (zkClient == null)
            init();

        String servicePath = Registry.ROOT + "/" + serviceClass.getSimpleName();
        //读取服务定义
        ServiceDefine returnDefine = zkClient.readData(servicePath);
        //检查类是否存在
        returnDefine.reflectClass();
        //读取服务实例
        List<String> paths = zkClient.getChildren(servicePath);
        List<ServiceInstance> instances = new LinkedList<>();
        for (String path : paths) {
            ServiceInstance serviceInstance = zkClient.readData(path);
            //如果服务实例在线，则加入本地服务实例列表
            if (serviceInstance.status == ServiceInstance.Status_Online) {
                instances.add(serviceInstance);
                log.debug("服务实例添加成功:" + path);
            }
        }
        ServiceManager.addService(returnDefine, instances, poolStrategy);


    }

    /**
     * 获取某个服务的一个客户端实例
     * @param serviceClass
     * @param <E>
     * @return
     */
    public <E> E getServiceClient(Class<E> serviceClass) {
        //获取服务
        Service service = ServiceManager.getService(serviceClass.getSimpleName());
        if (service != null) {
            return service.getServiceProxy();
        }
        throw new RuntimeException("获取客户端:服务" + serviceClass.getSimpleName() + "不存在");
    }

    private void init() {
        zkClient = new ZkClient(host, 2000, 2000, new SerializableSerializer());
    }
}
