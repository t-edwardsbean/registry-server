package com.edwardsbean.registry.pool;

import org.apache.avro.ipc.NettyTransceiver;

/**
 * Avro rpc 客户端，需要被池化的对象
 * Created by edwardsbean on 14-9-10.
 */
public class AvroClient<E> {
    public NettyTransceiver transceiver;
    public E proxy;
}
