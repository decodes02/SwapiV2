package com.pega.swapi.repository;

import com.pega.swapi.model.Starship;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface StarshipRepository extends MongoRepository<Starship, String> {
    Optional<Starship> findByName(String name);
}