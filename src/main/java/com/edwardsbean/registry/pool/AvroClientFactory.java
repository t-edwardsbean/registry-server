package com.edwardsbean.registry.pool;

import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Avro rpc 客户端工厂
 * Created by edwardsbean on 14-9-10.
 */
public class AvroClientFactory<E> extends BasePooledObjectFactory<AvroClient<E>> {
    private static final Logger log = LoggerFactory.getLogger(AvroClientFactory.class);
    private String hostname;
    private int port;
    private Class<E> clazz;

    public AvroClientFactory(Class<E> clazz, String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;
        this.clazz = clazz;
    }

    @Override
    public AvroClient<E> create() throws Exception {
        try {
            //创建Avro rpc客户端
            NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(hostname, port));
            E proxy = SpecificRequestor.getClient(clazz, client);
            AvroClient<E> avroClient = new AvroClient<>();
            avroClient.transceiver = client;
            avroClient.proxy = proxy;

            log.debug(clazz.getSimpleName() + "客户端连接池创建实例" + avroClient);
        } catch (Exception e) {
            log.error(clazz.getSimpleName() + "客户端连接池创建客户端错误:" + e.getMessage(), e);
            throw e;
        }
        return null;
    }

    //连接池需要将对象进行封装，以监控池化对象的状态（比如：最近使用时间）
    @Override
    public PooledObject<AvroClient<E>> wrap(AvroClient<E> eAvroClient) {
        return new DefaultPooledObject<AvroClient<E>>(eAvroClient);
    }

    @Override
    public void destroyObject(PooledObject<AvroClient<E>> p) throws Exception {
        p.getObject().transceiver.close();
        log.debug(clazz.getSimpleName() + "客户端连接池销毁实例" + p.getObject());
    }
}
