package com.edwardsbean.registry.pool;

/**
 * AVRO连接池
 * Created by edwardsbean on 14-9-9.
 */
public interface ClientPool<E> {
    /**
     * 申请对象
     * @return
     */
    public AvroClient<E> openClient();

    /**
     * 归还对象
     * @param client
     */
    public void returnClient(AvroClient<E> client);

    /**
     * 清空连接池
     */
    public void clearAll();

    /**
     * 销毁连接池
     */
    public void destory();
}
