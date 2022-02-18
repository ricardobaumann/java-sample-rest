package com.github.ricbau.conventions.clients;

import com.github.ricbau.conventions.aspects.MeasureAndLog;
import com.github.ricbau.conventions.domain.exceptions.AddressValidationException;
import com.github.ricbau.conventions.domain.dtos.AddressValidationResult;
import com.github.ricbau.conventions.domain.dtos.ValidateAddressCommand;
import io.github.resilience4j.retry.Retry;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@AllArgsConstructor
public class ValidateAddressClient {

    /*
    The restTemplate setup is made on a @Configuration object, never on a service class
     */
    private final RestTemplate externalValidationTemplate;
    private final Retry validateAddressRetry;

    /*
     * It encapsulates everything related to the external service, and returns only what matters for our domain.
     * The validateAddressCommand object wraps all the parameters this service requires
     */
    @MeasureAndLog
    public Either<AddressValidationException, AddressValidationResult> isValid(ValidateAddressCommand validateAddressCommand) {
        return callService(validateAddressCommand)
                .map(response -> {
                    log.info("Address validation result: {}", response);
                    return new AddressValidationResult(
                            response.valid()
                    );
                })
                .toEither()
                .mapLeft(AddressValidationException::new);
    }

    private Try<ExternalServiceResult> callService(ValidateAddressCommand validateAddressCommand) {
        return Try.of(
                Retry.decorateCheckedSupplier(validateAddressRetry,
                        () -> externalValidationTemplate.postForObject("/validation",
                                buildExternalCommand(validateAddressCommand),
                                ExternalServiceResult.class)));
    }

    private ExternalServiceCommand buildExternalCommand(ValidateAddressCommand validateAddressCommand) {
        return new ExternalServiceCommand(
                String.format("%s %s, %s %s",
                        validateAddressCommand.streetName(),
                        validateAddressCommand.zipCode(),
                        validateAddressCommand.cityName(),
                        validateAddressCommand.countryCode()));
    }

    public record ExternalServiceCommand(String fullAddress) {
    }

    public record ExternalServiceResult(String message, Boolean valid) {
    }
}
