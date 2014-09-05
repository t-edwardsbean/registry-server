package com.edwardsbean.registry.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ZookeeperServicePubliserTest {
    @Autowired
    ZookeeperServicePubliser zookeeperServicePubliser;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testPublish() throws Exception {
        zookeeperServicePubliser.publishService();
    }
}