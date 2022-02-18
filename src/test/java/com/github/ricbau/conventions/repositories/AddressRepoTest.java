package com.github.ricbau.conventions.repositories;

import com.github.ricbau.conventions.DefaultObjects;
import com.github.ricbau.conventions.domain.entities.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(properties = "spring.mongodb.embedded.version=3.5.5")
@ExtendWith(SpringExtension.class)
class AddressRepoTest {

    @Autowired
    private AddressRepo addressRepo;

    @BeforeEach
    void setUp() {
        addressRepo.deleteAll();
        assertThat(addressRepo.count()).isZero();
    }

    @Test
    @DisplayName("it should persist an address")
    void persist() {
        //Given
        Address address = DefaultObjects.someAddress();
        addressRepo.save(address);

        //When //Then
        assertThat(addressRepo.findById("1"))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(address);
    }
}