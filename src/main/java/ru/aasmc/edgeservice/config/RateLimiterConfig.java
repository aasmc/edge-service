package ru.aasmc.edgeservice.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {
    @Bean
    public KeyResolver keyResolver() {
        // until add authentication, rate limiting is applied
        // to requests using a constant key
        return exchange -> Mono.just("anonymous");
    }
}
