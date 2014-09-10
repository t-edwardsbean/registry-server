package com.edwardsbean.registry.zookeeper;

import com.edwardsbean.registry.constant.Registry;
import com.edwardsbean.registry.service.ServiceDefine;
import com.edwardsbean.registry.service.ServiceInstance;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by edwardsbean on 2014/9/4 0004.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ZkclientTest {
    private ZkClient zkClient;
    @Value("${rs.registry.host}") private String host;

    @Before
    public void setUp() throws Exception {
        //自动重连，并维护session
        zkClient = new ZkClient(host,2000, 2000, new SerializableSerializer());


    }

    //递归创建节点
    @Test
    public void testCreate() throws Exception {
        zkClient.createPersistent("/services/user-center",true);
    }

    @Test
    public void testSerializeTest() throws Exception {
        ServiceDefine serviceDefine = new ServiceDefine();
        serviceDefine.setServiceName("A");
        String servicePath = Registry.ROOT + "/" + serviceDefine.getServiceName();
        zkClient.createPersistent(servicePath,true);
        zkClient.writeData(servicePath,serviceDefine);
        ServiceDefine returnDefine = zkClient.readData(servicePath);
        assert returnDefine.getServiceName().equals(serviceDefine.getServiceName());
    }

    @Test
    public void testSequence() throws Exception {
        zkClient.createEphemeralSequential("/services/user-center","haha");

    }

    @Test
    public void testGetChildren() throws Exception {

        List<String> paths = zkClient.getChildren(Registry.ROOT);
        for (String path : paths) {
            ServiceInstance serviceInstance = zkClient.readData(path);
        }
    }
}
