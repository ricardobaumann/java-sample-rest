package com.github.ricbau.conventions.clients;

import com.github.ricbau.conventions.domain.dtos.AddressValidationResult;
import com.github.ricbau.conventions.domain.dtos.ValidateAddressCommand;
import com.github.ricbau.conventions.domain.exceptions.AddressValidationException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("When an address is being validated, and the external service is unstable")
@ExtendWith(MockitoExtension.class)
class ValidateAddressClientRetryTest {
    private final int maxAttempts = 2;

    @Mock
    private RestTemplate externalValidationTemplate;
    @Spy
    private Retry retry = RetryRegistry
            .of(
                    RetryConfig.custom()
                            .maxAttempts(maxAttempts)
                            .build())
            .retry("test");

    @InjectMocks
    private ValidateAddressClient validateAddressClient;

    @Test
    @DisplayName("it should retry the request, and return fail after the maxAttempts")
    void retryAndFail() {
        //Given
        doThrow(new HttpClientErrorException(HttpStatus.BAD_GATEWAY))
                .when(externalValidationTemplate).postForObject(
                        "/validation",
                        new ValidateAddressClient.ExternalServiceCommand("street 12207, berlin DEU"),
                        ValidateAddressClient.ExternalServiceResult.class);

        //When //Then
        Assertions.assertThat(validateAddressClient.isValid(new ValidateAddressCommand(
                "street", "12207", "berlin", "DEU"
        )).getLeft()).isInstanceOf(AddressValidationException.class)
                .hasCauseInstanceOf(HttpClientErrorException.class);

        verify(externalValidationTemplate, times(maxAttempts)).postForObject(
                "/validation",
                new ValidateAddressClient.ExternalServiceCommand("street 12207, berlin DEU"),
                ValidateAddressClient.ExternalServiceResult.class);
    }

    @Test
    @DisplayName("it should retry the request, and return success if the service answers within the max attempts")
    void retryAndSucceed() {
        //Given
        when(externalValidationTemplate.postForObject(
                "/validation",
                new ValidateAddressClient.ExternalServiceCommand("street 12207, berlin DEU"),
                ValidateAddressClient.ExternalServiceResult.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_GATEWAY))
                .thenReturn(new ValidateAddressClient.ExternalServiceResult("some msg", true));

        //When //Then
        Assertions.assertThat(validateAddressClient.isValid(new ValidateAddressCommand(
                "street", "12207", "berlin", "DEU"
        )).get()).isEqualTo(new AddressValidationResult(true));

        verify(externalValidationTemplate, times(maxAttempts)).postForObject(
                "/validation",
                new ValidateAddressClient.ExternalServiceCommand("street 12207, berlin DEU"),
                ValidateAddressClient.ExternalServiceResult.class);
    }
}