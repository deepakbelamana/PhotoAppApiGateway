package com.dpkprojects.app.photoapp.api.ApiGateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {


    @Override
    //this method will contain the main logic
    public GatewayFilter apply(Config config) {
        return null;
    }

    public static class Config{
        //put configuration properties here to configure the behaviour of our custom filter
    }
}
