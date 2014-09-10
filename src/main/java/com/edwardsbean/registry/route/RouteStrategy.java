package com.edwardsbean.registry.route;

import com.edwardsbean.registry.service.ServiceInstance;

import java.util.List;

/**
 * 路由策略，用于策略性筛选同一个服务下，多个服务实例如何获取
 * Created by edwardsbean on 14-9-9.
 */
public interface RouteStrategy {
    public ServiceInstance getInstance(List<ServiceInstance> serviceInstances);
}
