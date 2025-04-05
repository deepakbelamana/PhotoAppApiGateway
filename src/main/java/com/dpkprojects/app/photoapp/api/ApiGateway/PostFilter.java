package com.dpkprojects.app.photoapp.api.ApiGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class PostFilter implements GlobalFilter {
    final Logger logger = LoggerFactory.getLogger(PostFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        /*
        this is the difference for pre and post filters
        Mono.fromRunnable execute right after routing
        in pre filter we just return chain.filter
         */
        return chain.filter(exchange).then(Mono.fromRunnable(()->{
            logger.info("post filter executed");
        }));
    }
}
