// StarshipService.java
package com.pega.swapi.service;

import com.pega.swapi.model.Starship;
import com.pega.swapi.repository.StarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StarshipService {
    private final String BASE_URL = "https://swapi.dev/api/starships/";

    @Autowired
    private StarshipRepository starshipRepository;

    public List<Starship> getAllStarships() {
        List<Starship> cachedStarships = starshipRepository.findAll();
        if (!cachedStarships.isEmpty()) {
            return cachedStarships;
        }
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL, Map.class);
        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            List<Starship> starships = mapToStarshipList(results);
            starships.forEach(starshipRepository::save);
            return starships;
        }
        return List.of();
    }

    public List<Starship> searchStarshipByName(String name) {
        Optional<Starship> cachedStarship = starshipRepository.findByName(name);
        if (cachedStarship.isPresent()) {
            return List.of(cachedStarship.get());
        }
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL + "?search=" + name, Map.class);
        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            List<Starship> starships = mapToStarshipList(results);
            starships.forEach(starshipRepository::save);
            return starships;
        }
        return List.of();
    }

    private List<Starship> mapToStarshipList(List<Map<String, Object>> results) {
        return results.stream().map(this::mapToStarship).toList();
    }

    private Starship mapToStarship(Map<String, Object> data) {
        Starship starship = new Starship();
        starship.setName((String) data.get("name"));
        starship.setModel((String) data.get("model"));
        starship.setManufacturer((String) data.get("manufacturer"));
        starship.setStarshipClass((String) data.get("starship_class"));
        starship.setCrew((String) data.get("crew"));
        return starship;
    }
}
