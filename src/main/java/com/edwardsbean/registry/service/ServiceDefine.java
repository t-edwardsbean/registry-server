package com.edwardsbean.registry.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by edwardsbean on 14-9-5.
 */
@Repository("serviceDefine")
public class ServiceDefine implements java.io.Serializable {
    @Value("${rs.service.name}")public String serviceName;
    @Value(("${rs.service.interface}"))public String serviceClass;
    public String routeClass;
    public String failClass;
    public String proxyClass;
    @Value("${rs.service.route}")public String route;

    private Class<?> proxyClazz;
    private Class<?> routeClazz;
    private Class<?> failClazz;

    public String getServiceClass() {
        return serviceClass;
    }

    public String getServiceName() {
        return serviceName;
    }


    protected Class<?> getProxyClass() {
        return this.proxyClazz;
    }

    protected Class<?> getRouteClass() {
        return this.routeClazz;
    }

    protected Class<?> getFailClass() {
        return this.failClazz;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
