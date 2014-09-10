package com.edwardsbean.registry.service;

import com.edwardsbean.registry.fail.FailStrategy;
import com.edwardsbean.registry.pool.PoolStrategy;
import com.edwardsbean.registry.route.RouteStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理多个服务
 * Created by edwardsbean on 14-9-9.
 */
public class ServiceManager {
    private static final Logger log = LoggerFactory.getLogger(ServiceManager.class);
    private static Map<String, Service> services = new HashMap<>();

    protected static synchronized void addService(ServiceDefine serviceDefine, List<ServiceInstance> serviceInstances, PoolStrategy poolStrategy) throws Exception{
        if (getService(serviceDefine.serviceName) == null) {
            Service service = new Service();
            service.serviceDefine = serviceDefine;
            service.instances = serviceInstances;
            service.routeStrategy = (RouteStrategy) serviceDefine.getRouteClass().newInstance();
            service.poolStrategy = poolStrategy;
            service.failStrategy = (FailStrategy) serviceDefine.getFailClass().newInstance();

            for (ServiceInstance serviceInstance : serviceInstances) {
                log.info("初始化服务实例客户端连接池");
                log.info("创建" + serviceDefine.serviceName + "服务实例:" + serviceInstance);
                serviceInstance.createClientPool(serviceDefine.getServiceClass(),poolStrategy);
            }
            services.put(serviceDefine.serviceName, service);
        }
    }

    protected static synchronized void updateServiceInstances(String serviceName, List<ServiceInstance> serviceInstances) {
        Service service = services.get(serviceName);

    }

    protected static synchronized Service getService(String serviceName){
        return services.get(serviceName);
    }

    protected synchronized void removeServiceInstance(String serviceName, ServiceInstance serviceInstance) {
        Service service = services.get(serviceName);
        if (service != null) {
            if (service.instances.remove(serviceInstance)) {
                serviceInstance.destory();
            }
        }
    }
}
