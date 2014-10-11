package com.edwardsbean.registry.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 向注册中心查询服务，并订阅服务变化。将查询到的服务放在本地的连接池中，使用默认策略轮询
 * Created by edwardsbean on 14-9-9.
 */

public abstract class AbstractServiceSubscriber implements ServiceSubscriber {
    private static final Logger log = LoggerFactory.getLogger(AbstractServiceSubscriber.class);

    /**
     * 获取某个服务的一个客户端实例
     *
     * @param serviceClass
     * @param <E>
     * @return
     */
    @Override
    public <E> E getServiceClient(Class<E> serviceClass) {
        //获取服务
        Service service = ServiceManager.getService(serviceClass.getSimpleName());
        if (service != null) {
            return service.getServiceProxy();
        }
        throw new RuntimeException("获取客户端:服务" + serviceClass.getSimpleName() + "不存在");
    }
}
