package com.github.ricbau.conventions;

import com.github.ricbau.conventions.domain.entities.Address;

public class DefaultObjects {

    public static Address someAddress() {
        return new Address(
                "1", "street", "zip", "city", "ABC", false
        );
    }

}
