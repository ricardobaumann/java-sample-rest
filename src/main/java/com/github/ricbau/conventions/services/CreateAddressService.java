package com.github.ricbau.conventions.services;

import com.github.ricbau.conventions.clients.ValidateAddressClient;
import com.github.ricbau.conventions.domain.dtos.CreateAddressCommand;
import com.github.ricbau.conventions.domain.dtos.ValidateAddressCommand;
import com.github.ricbau.conventions.domain.entities.Address;
import com.github.ricbau.conventions.domain.exceptions.AddressCreateException;
import com.github.ricbau.conventions.repositories.AddressRepo;
import com.github.ricbau.conventions.usecases.CreateAddressUseCase;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CreateAddressService implements CreateAddressUseCase {

    private final AddressRepo addressRepo;
    private final ValidateAddressClient validateAddressClient;

    @Override
    public Either<AddressCreateException, Address> create(CreateAddressCommand createAddressCommand) {
        /*
        Service builds up and manipulate domain information, orchestrate other services and repositories
         */
        return validateAddressClient.isValid(
                        new ValidateAddressCommand(
                                createAddressCommand.streetName(),
                                createAddressCommand.zipCode(),
                                createAddressCommand.cityName(),
                                createAddressCommand.countryCode()
                        ))
                .map(result -> new Address(
                        UUID.randomUUID().toString(),
                        createAddressCommand.streetName(),
                        createAddressCommand.zipCode(),
                        createAddressCommand.cityName(),
                        createAddressCommand.countryCode(),
                        result.valid()
                ))
                .toTry().andThenTry(addressRepo::save)
                .toEither()
                .mapLeft(AddressCreateException::new);
    }

}
