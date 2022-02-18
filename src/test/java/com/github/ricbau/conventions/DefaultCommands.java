package com.github.ricbau.conventions;

import com.github.ricbau.conventions.domain.dtos.CreateAddressCommand;

import java.util.stream.Stream;

public class DefaultCommands {

    public static CreateAddressCommand validCreateAddressCommand() {
        return new CreateAddressCommand(
                "foo bar street",
                "10159",
                "Berlin",
                "DEU"
        );
    }


    public static Stream<CreateAddressCommand> invalidCommands() {
        return Stream.of(
                new CreateAddressCommand(
                        null,
                        "10159",
                        "Berlin",
                        "DEU"
                ),
                new CreateAddressCommand(
                        "",
                        "10159",
                        "Berlin",
                        "DEU"
                ),
                new CreateAddressCommand(
                        "foo bar street",
                        null,
                        "Berlin",
                        "DEU"
                ),
                new CreateAddressCommand(
                        "foo bar street",
                        "",
                        "Berlin",
                        "DEU"
                ),
                new CreateAddressCommand(
                        "foo bar street",
                        "1234",
                        "Berlin",
                        "DEU"
                ),
                new CreateAddressCommand(
                        "foo bar street",
                        "10159",
                        null,
                        "DEU"
                ),
                new CreateAddressCommand(
                        "foo bar street",
                        "10159",
                        "",
                        "DEU"
                ),
                new CreateAddressCommand(
                        "foo bar street",
                        "10159",
                        "Berlin",
                        null
                ),
                new CreateAddressCommand(
                        "foo bar street",
                        "10159",
                        "Berlin",
                        "AB"
                )
        );
    }
}
