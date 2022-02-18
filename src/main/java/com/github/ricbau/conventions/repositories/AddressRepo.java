package com.github.ricbau.conventions.repositories;

import com.github.ricbau.conventions.domain.entities.Address;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends MongoRepository<Address, String> {
}
