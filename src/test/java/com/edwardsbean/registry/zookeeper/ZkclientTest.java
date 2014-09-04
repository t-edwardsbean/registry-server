package com.edwardsbean.registry.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by edwardsbean on 2014/9/4 0004.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ZkclientTest {
    @Value("${rs.registry.hostname}") private String hostname;
    @Value("${rs.registry.port}") private String port;
    private ZkClient zkClient;

    @Before
    public void setUp() throws Exception {
        //默认30秒超时。自动重连，并维护session
        zkClient = new ZkClient(hostname+":"+port,30000);

    }

    //递归创建节点
    @Test
    public void testCreate() throws Exception {
        zkClient.createPersistent("/services/user/a",true);
    }



}
