package com.edwardsbean.registry.route;

import com.edwardsbean.registry.service.ServiceInstance;

import java.util.List;

/**
 * 轮询的路由策略，所有在线服务实例拥有相同的权重
 * Created by edwardsbean on 14-9-10.
 */
public class RoundRobin implements RouteStrategy{

    private int index = 0;

    @Override
    public ServiceInstance getInstance(List<ServiceInstance> serviceInstances) {
        if (serviceInstances != null && !serviceInstances.isEmpty()) {
            int size = serviceInstances.size();
            if (size > 0) {
                index++;
                if (index >= size) {
                    index = 0;
                }
                return serviceInstances.get(index);
            }
        }
        return null;
    }
}
