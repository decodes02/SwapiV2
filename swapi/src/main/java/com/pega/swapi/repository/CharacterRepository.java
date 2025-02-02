package com.pega.swapi.repository;

import com.pega.swapi.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CharacterRepository extends MongoRepository<Character, String> {
    Optional<Character> findByName(String name);
}
