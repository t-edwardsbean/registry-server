package com.edwardsbean.registry.fail;

import com.edwardsbean.registry.service.ServiceInstance;

import java.util.List;

/**
 * Created by edwardsbean on 14-9-9.
 */
public interface FailStrategy {
    public ServiceInstance failover(List<ServiceInstance> onlineInstances);
}
