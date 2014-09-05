package com.edwardsbean.registry.service;

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

//    private ClientPool<?> pool;

    public static final byte Status_Online = 1;
    public static final byte Status_Offline = 0;
}
