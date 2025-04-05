package com.dpkprojects.app.photoapp.api.ApiGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Configuration
public class GlobalFilterConfiguration {
    final Logger logger = LoggerFactory.getLogger(GlobalFilterConfiguration.class);
    @Bean
    @Order(1) //order annotation tells in which order the filters should execute .. low order number high priority for pre and less priority for post,
            // post will execute at last all post filter for this filter
    public GlobalFilter secondPreFilter() {

        return (exchange, chain) -> {
            logger.info("second pre filter executed");
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
              logger.info("second post filter executed");
            }));
        };
    }
    @Bean
    @Order(3)
    public GlobalFilter thirdPreFilter() {

        return (exchange, chain) -> {
            logger.info("third pre filter executed");
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
              logger.info("third post filter executed");
            }));
        };
    }

    @Bean
    @Order(2)
    public GlobalFilter fourthPreFilter() {

        return (exchange, chain) -> {
            logger.info("fourth pre filter executed");
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                logger.info("fourth post filter executed");
            }));
        };
    }
}
