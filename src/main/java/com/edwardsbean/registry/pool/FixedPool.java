package com.edwardsbean.registry.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by edwardsbean on 14-9-10.
 */
public class FixedPool implements PoolStrategy {
    private int maxSize;
    public FixedPool(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * 创建固定大小的连接池
     * <p>
     * 连接池的大小固定，由maxSize指定，当池中没有空闲的链接，线程将阻塞，直到有空闲的链接
     * </p>
     * @param clazz
     * @param hostname
     * @param port
     * @param <E>
     * @return
     */
    @Override
    public <E> ClientPool<E> createPool(Class<E> clazz, String hostname, int port) {
        AvroClientFactory<E> factory = new AvroClientFactory<>(clazz, hostname, port);
        GenericObjectPoolConfig cfg = new GenericObjectPoolConfig();
        // timeout 10s
        cfg.setMaxWaitMillis(1000 * 10);

        cfg.setTestWhileIdle(false);
        cfg.setTestOnBorrow(true);
        cfg.setTestOnReturn(false);
        cfg.setTestWhileIdle(false);

        // last in fist out / stack, default by fifo / queue
        cfg.setLifo(true);
        // disblae jmx
        cfg.setJmxEnabled(false);
        cfg.setMaxTotal(maxSize);
        cfg.setMaxIdle(maxSize);
        return new AvroClientPool<E>(clazz.getSimpleName(), factory, cfg);
    }
}
