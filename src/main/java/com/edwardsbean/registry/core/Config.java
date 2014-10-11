package com.edwardsbean.registry.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by edwardsbean on 14-10-11.
 */
@Component
public class Config {
    @Value("{rs.service.name}")public String serviceName;
}
