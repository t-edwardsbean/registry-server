Registry Server
-----
base on zookeeper

####zkclient
- zookeeper断链重连，维护session
- 长期监控节点变化

问题：
- retryUtilConnected阻塞
- data监测,znode数据能否实时
- znode是否使用Ephemeral类型，在session结束时自动删除(如果服务提供者真的挂了，那客户端就不会在robin调用服务时，使用该失效的服务，就不会重复报错)

----------------------------

###客户端

####反射
客户端通过注册框架XXService rpc = ServiceSubscriber.getService(XXService.class)获取服务rpc实例，
rpc被代理了两层，第一层我们用动态代理，策略性从本地连接池中选择rpc客户端实例。而rpc客户端实例是avro框架帮
我们做了代理，将底层socket传输封装了起来。

####客户端连接池
每一个服务实例，都有一个本地的连接池。使用apache common pool2封装一个连接池。

####失败策略
区分业务异常和avro通信异常