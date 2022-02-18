package com.github.ricbau.conventions.domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Address(
        @Id
        String id,
        String streetName,
        String zipCode,
        String cityName,
        String countryCode,
        Boolean valid) {
}
