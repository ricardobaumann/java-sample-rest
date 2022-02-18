package com.github.ricbau.conventions.usecases;

import com.github.ricbau.conventions.domain.dtos.CreateAddressCommand;
import com.github.ricbau.conventions.domain.entities.Address;
import com.github.ricbau.conventions.domain.exceptions.AddressCreateException;
import io.vavr.control.Either;

/*
Interfaces used to minimize the coupling between controller and service layer
 */
public interface CreateAddressUseCase {
    Either<AddressCreateException, Address> create(CreateAddressCommand createAddressCommand);
}
