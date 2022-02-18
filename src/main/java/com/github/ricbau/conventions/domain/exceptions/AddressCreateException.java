package com.github.ricbau.conventions.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AddressCreateException extends RuntimeException {
    public AddressCreateException(Throwable cause) {
        super(cause);
    }
}
