package com.edwardsbean.registry.fail;

import com.edwardsbean.registry.service.ServiceInstance;

import java.util.List;
import java.util.Random;

/**
 * 失败自动切换，当出现失败，随机重试其它服务器，通常用于读操作（推荐使用）
 * Created by edwardsbean on 14-9-10.
 */
public class Failover implements FailStrategy{

    private Random random = new Random(System.currentTimeMillis());

    @Override
    public ServiceInstance failover(List<ServiceInstance> onlineInstances) {
        int size = onlineInstances.size();
        switch (size) {
            case 0:
                return null;
            case 1:
                return onlineInstances.get(0);
            default:
                return onlineInstances.get(random.nextInt(size));
        }
    }
}
