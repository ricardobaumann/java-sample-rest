package com.github.ricbau.conventions.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricbau.conventions.DefaultCommands;
import com.github.ricbau.conventions.DefaultObjects;
import com.github.ricbau.conventions.domain.dtos.CreateAddressCommand;
import com.github.ricbau.conventions.domain.exceptions.AddressCreateException;
import com.github.ricbau.conventions.usecases.CreateAddressUseCase;
import io.vavr.control.Either;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("When an address POST request is being handled")
@WebMvcTest(controllers = AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CreateAddressUseCase createAddressUseCase;
    @Autowired
    private ObjectMapper objectMapper;

    private final CreateAddressCommand validCreateAddressCommand =
            DefaultCommands.validCreateAddressCommand();

    @Test
    @SneakyThrows
    @DisplayName("it should return CREATED, with the creation result and id if successful")
    void success() {
        //Given
        when(createAddressUseCase.create(validCreateAddressCommand))
                .thenReturn(Either.right(DefaultObjects.someAddress()));

        //When //Then
        mockMvc.perform(
                        post("/addresses")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validCreateAddressCommand)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/addresses/1"))
                .andExpect(jsonPath("$.id", is("1")));
    }

    @Test
    @SneakyThrows
    @DisplayName("it should return CONFLICT in case of service failure")
    void failure() {
        //Given
        when(createAddressUseCase.create(validCreateAddressCommand))
                .thenReturn(Either.left(new AddressCreateException(new RuntimeException("boom"))));

        //When //Then
        mockMvc.perform(
                        post("/addresses")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validCreateAddressCommand)))
                .andExpect(status().isConflict());
    }

    static Stream<CreateAddressCommand> invalidCommands() {
        return DefaultCommands.invalidCommands();
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("invalidCommands")
    @DisplayName("it should return BAD REQUEST on static validation failure")
    void staticValidations(CreateAddressCommand createAddressCommand) {
        mockMvc.perform(
                        post("/addresses")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAddressCommand)))
                .andExpect(status().isBadRequest());
    }
}