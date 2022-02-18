package com.github.ricbau.conventions.domain.dtos;

public record ValidateAddressCommand(String streetName, String zipCode, String cityName,
                                     String countryCode) {
}
