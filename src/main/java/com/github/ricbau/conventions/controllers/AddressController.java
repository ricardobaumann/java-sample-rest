package com.github.ricbau.conventions.controllers;

import com.github.ricbau.conventions.usecases.CreateAddressUseCase;
import com.github.ricbau.conventions.domain.dtos.CreateAddressCommand;
import com.github.ricbau.conventions.domain.dtos.CreateAddressResult;
import com.github.ricbau.conventions.domain.entities.Address;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping("/addresses")
@AllArgsConstructor
public class AddressController {

    private final CreateAddressUseCase createAddressUseCase;

    /*
    REST endpoints are a combination of the resource path ('address'), and the http verb ('POST').
    On the path, only nouns should be used, never verbs.The action is always determined by the http verb
     */
    @PostMapping
    public ResponseEntity<CreateAddressResult> post(@Valid
                                                    /*
                                                    With the @Valid annotation, we ensure the input object will be validated,
                                                    according to the bean validation annotations
                                                     */
                                                    @RequestBody CreateAddressCommand createAddressCommand) {
        return createAddressUseCase.create(createAddressCommand)
                .map(this::toCreateResult)//Controller responsibility: to map the service layer result back to the caller
                .getOrElseThrow(Function.identity());//... and to handle service layer exceptions
    }

    private ResponseEntity<CreateAddressResult> toCreateResult(Address address) {
        return ResponseEntity.created(
                ServletUriComponentsBuilder //Optional: the link for the created entity can be returned if it is useful for API clients
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(address.id())
                        .toUri()
        ).body(new CreateAddressResult(address.id()));
    }
}
