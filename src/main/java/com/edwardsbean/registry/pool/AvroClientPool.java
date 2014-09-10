package com.edwardsbean.registry.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Avro rpc 客户端工厂，用于管理要被池化的对象的借出和归还
 * Created by edwardsbean on 14-9-10.
 */
public class AvroClientPool<E> extends GenericObjectPool<AvroClient<E>> implements ClientPool<E> {

    private static final Logger log = LoggerFactory.getLogger(AvroClientPool.class);
    private String serviceName;

    //GenericObjectPool,用于配置连接池
    public AvroClientPool(String serviceName, PooledObjectFactory<AvroClient<E>> factory, GenericObjectPoolConfig config) {
        super(factory, config);
        this.serviceName = serviceName;
        log.debug("创建" + serviceName + "服务客户端连接池");
    }

    @Override
    public AvroClient<E> openClient() {
        try {
            return this.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void returnClient(AvroClient<E> client) {
        this.returnObject(client);
    }

    @Override
    public void clearAll() {
        this.clear();
    }

    @Override
    public void destory() {
        this.clear();
        if (!this.isClosed()) {
            this.close();
        }
        log.debug("销毁连接池" + serviceName);
    }
}
