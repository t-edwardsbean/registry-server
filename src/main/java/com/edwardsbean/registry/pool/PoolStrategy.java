package com.edwardsbean.registry.pool;

/**
 * 连接池策略
 * Created by edwardsbean on 14-9-9.
 */
public interface PoolStrategy {
    public <E> ClientPool<E> createPool(Class<E> resClass, String hostname, int port);
}
