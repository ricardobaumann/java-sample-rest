package com.github.ricbau.conventions.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/*
Configuration classes are used for objects that requires more complex setup,
like http, database and message clients
 */
@Configuration
public class ExternalValidationConfig {

    @Bean
    public RestTemplate externalValidationTemplate(RestTemplateBuilder restTemplateBuilder,
                                                   AddressValidationProperties addressValidationProperties) {
        return restTemplateBuilder
                .rootUri(addressValidationProperties.getUrl())
                .build();
    }

}
