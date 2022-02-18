package com.github.ricbau.conventions.services;

import com.github.ricbau.conventions.DefaultObjects;
import com.github.ricbau.conventions.clients.ValidateAddressClient;
import com.github.ricbau.conventions.domain.dtos.AddressValidationResult;
import com.github.ricbau.conventions.domain.dtos.CreateAddressCommand;
import com.github.ricbau.conventions.domain.dtos.ValidateAddressCommand;
import com.github.ricbau.conventions.domain.entities.Address;
import com.github.ricbau.conventions.domain.exceptions.AddressCreateException;
import com.github.ricbau.conventions.repositories.AddressRepo;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("When an address is being created")
@ExtendWith(MockitoExtension.class)
class CreateAddressServiceTest {
    @Mock
    private AddressRepo addressRepo;
    @Mock
    private ValidateAddressClient validateAddressClient;
    @InjectMocks
    private CreateAddressService createAddressService;
    @Captor
    private ArgumentCaptor<Address> addressArgumentCaptor;

    @Test
    @DisplayName("it should persist it and return the result")
    void create() {
        //Given
        Address someAddress = DefaultObjects.someAddress();

        when(validateAddressClient.isValid(new ValidateAddressCommand(
                someAddress.streetName(),
                someAddress.zipCode(),
                someAddress.cityName(),
                someAddress.countryCode()
        ))).thenReturn(Either.right(new AddressValidationResult(
                true
        )));

        when(addressRepo.save(any()))
                .thenReturn(someAddress);

        //When //Then
        assertThat(createAddressService.create(
                new CreateAddressCommand(
                        someAddress.streetName(),
                        someAddress.zipCode(),
                        someAddress.cityName(),
                        someAddress.countryCode()
                )
        ).get()).usingRecursiveComparison()
                .ignoringFields("id", "valid")
                .isEqualTo(someAddress);

        verify(addressRepo).save(addressArgumentCaptor.capture());
        Address capturedAddress = addressArgumentCaptor.getValue();

        assertThat(capturedAddress)
                .usingRecursiveComparison()
                .ignoringFields("id", "valid")
                .isEqualTo(someAddress);

        assertThat(capturedAddress.valid()).isTrue();
    }

    @Test
    @DisplayName("it should return an exception in case of failure")
    void failure() {
        //Given
        when(validateAddressClient.isValid(new ValidateAddressCommand(
                "foo bar street",
                "10159",
                "Berlin",
                "DEU"
        ))).thenReturn(Either.right(new AddressValidationResult(
                true
        )));

        doThrow(new RuntimeException("Failed")).when(addressRepo).save(any());

        //When //Then
        assertThat(createAddressService.create(
                new CreateAddressCommand(
                        "foo bar street",
                        "10159",
                        "Berlin",
                        "DEU"
                )
        ).getLeft()).isInstanceOf(AddressCreateException.class);
    }
}