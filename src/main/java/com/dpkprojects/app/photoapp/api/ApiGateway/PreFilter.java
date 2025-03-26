package com.dpkprojects.app.photoapp.api.ApiGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * pre filter are executed before api-gateway perform
 * routing to destination microservice
 */
@Component
public class PreFilter implements GlobalFilter {
    final Logger logger = LoggerFactory.getLogger(PreFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("pre filter testing..!");
        return chain.filter(exchange);
    }
}
