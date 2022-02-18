package com.github.ricbau.conventions.clients;

import com.github.ricbau.conventions.domain.dtos.AddressValidationResult;
import com.github.ricbau.conventions.domain.dtos.ValidateAddressCommand;
import com.github.ricbau.conventions.domain.exceptions.AddressValidationException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static com.github.ricbau.conventions.clients.ValidateAddressClient.ExternalServiceCommand;
import static com.github.ricbau.conventions.clients.ValidateAddressClient.ExternalServiceResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@DisplayName("When an address is being validated")
@ExtendWith(MockitoExtension.class)
class ValidateAddressClientTest {
    @Mock
    private RestTemplate externalValidationTemplate;
    @Spy
    private Retry retry = RetryRegistry.ofDefaults().retry("test");
    @InjectMocks
    private ValidateAddressClient validateAddressClient;

    @Test
    @DisplayName(
            "it should return the address validation result" +
                    "if the call is successful")
    void isValid() {
        //Given
        when(externalValidationTemplate.postForObject(
                "/validation",
                new ExternalServiceCommand("street 12207, berlin DEU"),
                ExternalServiceResult.class))
                .thenReturn(
                        new ExternalServiceResult("some msg", true)
                );

        //When //Then
        assertThat(validateAddressClient.isValid(new ValidateAddressCommand(
                "street", "12207", "berlin", "DEU"
        )).get()).isEqualTo(new AddressValidationResult(true));
    }

    @Test
    @DisplayName(
            "it should return an error if the communication failed"
    )
    void failure() {
        //Given
        doThrow(HttpServerErrorException.class).when(externalValidationTemplate)
                .postForObject(
                        "/validation",
                        new ExternalServiceCommand("street 12207, berlin DEU"),
                        ExternalServiceResult.class);

        assertThat(validateAddressClient.isValid(new ValidateAddressCommand(
                "street", "12207", "berlin", "DEU"
        )).getLeft())
                .isInstanceOf(AddressValidationException.class)
                .hasCauseInstanceOf(HttpServerErrorException.class);
    }
}