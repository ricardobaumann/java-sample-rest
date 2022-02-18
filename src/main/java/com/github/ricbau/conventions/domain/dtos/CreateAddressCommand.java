package com.github.ricbau.conventions.domain.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/*
Here we can check the static validation configuration, using Bean Validation.
Since the controller is responsible for the static validations, those will be covered on controller tests
 */
public record CreateAddressCommand(@NotEmpty @Size(min = 1, max = 200) String streetName,
                                   @NotEmpty @Size(min = 5, max = 5) String zipCode,
                                   @NotEmpty @Size(min = 1, max = 50) String cityName,
                                   @NotEmpty @Size(min = 3, max = 3) String countryCode) {
}
