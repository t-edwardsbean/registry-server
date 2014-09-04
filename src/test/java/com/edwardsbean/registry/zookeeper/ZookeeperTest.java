package com.edwardsbean.registry.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ZookeeperTest {
    private ZooKeeper zk;
    @Value("${rs.registry.hostname}") private String hostname;
    @Value("${rs.registry.port}") private String port;

    @Before
    public void setUp() throws Exception {
        zk = new ZooKeeper(hostname + ":" + port, 3000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
            }
        });
    }

    //创建services节点
    @Test
    public void testCreate() throws Exception {
        String path = "/services";
        //添加节点携带数据
        byte[] initData = "services".getBytes() ;
        zk.create(path,initData, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    //节点是否存在
    @Test
    public void testExist() throws Exception {
        String path = "/services";
        assert zk.exists(path,false) != null;

    }

    //获取services节点数据
    @Test
    public void testGetData() throws Exception {
        String path = "/services";
        Stat stat = new Stat();
        byte[] data = zk.getData(path, false, stat);
        assert "services".equals(new String(data));
    }



}