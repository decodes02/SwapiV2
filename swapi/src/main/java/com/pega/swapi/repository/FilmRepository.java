package com.pega.swapi.repository;

import com.pega.swapi.model.Film;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface FilmRepository extends MongoRepository<Film, String> {
    Optional<Film> findByTitle(String title);
}