package com.github.ricbau.conventions.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
Configuration properties should be used to hold and group application configuration
 */
@Data
@Component
@ConfigurationProperties(prefix = "w4k.conventions.external-validation")
public class AddressValidationProperties {
    private String url;
}
