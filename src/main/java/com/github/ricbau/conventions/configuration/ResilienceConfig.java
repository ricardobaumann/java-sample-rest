package com.github.ricbau.conventions.configuration;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResilienceConfig {

    @Bean
    public RetryRegistry retryRegistry() {
        return RetryRegistry
                .of(
                        RetryConfig.custom()
                                .maxAttempts(3)
                                .waitDuration(Duration.ofMillis(200))
                                .build());
    }

    @Bean
    public Retry validateAddressRetry(RetryRegistry retryRegistry) {
        return retryRegistry.retry("validateAddressRetry");
    }


}
