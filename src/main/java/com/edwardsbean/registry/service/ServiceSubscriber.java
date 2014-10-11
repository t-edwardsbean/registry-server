package com.edwardsbean.registry.service;

import com.edwardsbean.registry.pool.PoolStrategy;

/**
 * Created by edwardsbean on 14-10-11.
 */
public interface ServiceSubscriber {
    public void subscribeService(Class<?> serviceClass, PoolStrategy poolStrategy) throws Exception;
    public <E> E getServiceClient(Class<E> serviceClass);
}
