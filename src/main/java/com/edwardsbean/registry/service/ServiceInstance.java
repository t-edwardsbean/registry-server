package com.edwardsbean.registry.service;

import com.edwardsbean.registry.pool.ClientPool;
import com.edwardsbean.registry.pool.PoolStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created by edwardsbean on 14-9-5.
 */
@Repository("serviceInstance")
public class ServiceInstance implements Serializable {
    @Value("${rs.service.hostname}")public String hostname;
    @Value("${rs.service.port}")public int port;
    public byte status = Status_Offline;
    public float weight = 1.0f; // 权重, 主要针对带权重的路由策略
    private ClientPool<?> pool;
//    private ClientPool<?> pool;

    protected void destory(){

    }

    public static final byte Status_Online = 1;
    public static final byte Status_Offline = 0;

    /**
     * 单个服务实例的连接池
     * @param serviceClass
     * @param poolStrategy
     * @param <E>
     */
    protected <E> void createClientPool(Class<E> serviceClass, PoolStrategy poolStrategy) {
        if (pool == null) {
            this.pool = poolStrategy.createPool(serviceClass,hostname,port);
        }
    }

    protected <E> ClientPool<E> getClientPool(){
        return (ClientPool<E>) pool;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(128);
        sb.append("hostname:").append(hostname).append(", port:").append(port);
        return sb.toString();
    }
}
