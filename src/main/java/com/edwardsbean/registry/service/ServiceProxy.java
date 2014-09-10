package com.edwardsbean.registry.service;

import com.edwardsbean.registry.fail.FailStrategy;
import com.edwardsbean.registry.pool.AvroClient;
import com.edwardsbean.registry.pool.ClientPool;
import com.edwardsbean.registry.route.RouteStrategy;
import org.apache.avro.specific.SpecificExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Service的动态代理，每次调用rpc的方法时，动态代理会根据路由策略，失败策略，选择rpc代理实例。
 * Created by edwardsbean on 14-9-9.
 */
public final class ServiceProxy<E> implements InvocationHandler {
    private static final Logger log = LoggerFactory.getLogger(ServiceProxy.class);

    private RouteStrategy routeStrategy;
    private FailStrategy failStrategy;
    private List<ServiceInstance> instances;
    private ServiceDefine serviceDefine;

    private static final ExecutorService exec = Executors.newFixedThreadPool(5);

    public ServiceProxy(ServiceDefine serviceDefine, List<ServiceInstance> instances, RouteStrategy routeStrategy, FailStrategy failStrategy) {
        this.serviceDefine = serviceDefine;
        this.instances = instances;
        this.routeStrategy = routeStrategy;
        this.failStrategy = failStrategy;
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        if (method.getName().equals("toString")) {
            return method.invoke(proxy, args);
        }
        FutureTask<Object> task = new FutureTask<Object>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                //根据路由策略选择服务实例
                ServiceInstance instance = routeStrategy.getInstance(instances);
                if (instance != null) {
                    while (true) {
                        ClientPool<E> pool = instance.getClientPool();
                        AvroClient<E> avroClient = null;
                        try {
                            //从实例客户端连接池中请求连接
                            avroClient = pool.openClient();
                            long currentTime = System.currentTimeMillis();
                            log.debug("请求" + serviceDefine.serviceName + "服务实例" + instance + ", 请求方法:" + method.getName() + "， 请求参数:"
                                    + Arrays.toString(args));
                            //调用avro rpc client对应的方法
                            Object result = method.invoke(avroClient.proxy, args);
                            currentTime = System.currentTimeMillis() - currentTime;
                            log.debug("请求时间:" + currentTime + "毫秒, 请求结果:" + result);
                            return result;
                        } catch (Exception e) {
                            //由于使用了反射，需要用getCause获取最原生态的异常
                            Throwable exception = e.getCause();
                            //业务异常
                            if (exception != null && exception instanceof SpecificExceptionBase) {
                                log.debug("业务异常：" + exception);
                                return exception;
                                //其他异常视为通信异常
                            } else {
                                //TODO 设置服务实例离线
                                //ServiceSubscriber.offlineServiceInstance(serviceDefine,
                                //instance);

                                log.debug("与服务端通信异常：" + e.getMessage(), e);

                                //失败转移
                                instance = failStrategy.failover(instances);
                                if (instance != null) {
                                    continue;
                                } else {
                                    break;
                                }
                            }
                            //归还连接
                        } finally {
                            if (avroClient != null) {
                                pool.returnClient(avroClient);
                            }
                        }
                    }
                }
                return new RuntimeException("服务" + serviceDefine.serviceName + "没有在线的服务实例存在");
            }
        });
        exec.execute(task);
        Object rs = task.get();

        //业务异常或者没有服务实例
        if (rs != null && Throwable.class.isAssignableFrom(rs.getClass())) {
            throw (Throwable) rs;
        }
        return rs;
    }
}