package com.edwardsbean.registry.service;

import com.edwardsbean.registry.fail.FailStrategy;
import com.edwardsbean.registry.pool.PoolStrategy;
import com.edwardsbean.registry.route.RouteStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 服务对象，包含服务定义，服务的所有实例
 * Created by edwardsbean on 14-9-9.
 */
public class Service {
    private static final Logger log = LoggerFactory.getLogger(Service.class);
    protected List<ServiceInstance> instances;
    protected ServiceDefine serviceDefine;
    protected RouteStrategy routeStrategy;
    protected PoolStrategy poolStrategy;
    protected FailStrategy failStrategy;

    private Object proxy;
    private ServiceProxy<?> serviceProxy;

    /**
     * 获取服务代理
     * @param <E>
     * @return
     */
    protected <E> E getServiceProxy() {
        return (E) proxy;
    }

    protected void createProxy() {

        try {
            serviceProxy = new ServiceProxy<>(this.serviceDefine, this.instances, this.routeStrategy, this.failStrategy);
            Object target = serviceDefine.getProxyClass().newInstance();
            proxy = Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),serviceProxy);
            log.info("创建" + serviceDefine.serviceName + "服务代理：" + target);
        } catch (Exception e) {
            log.error("创建" + serviceDefine.serviceName + "服务代理异常" + e.getMessage(), e);
        }
    }



}
