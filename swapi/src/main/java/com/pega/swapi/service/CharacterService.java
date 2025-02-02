package com.pega.swapi.service;

import com.pega.swapi.model.Character;
import com.pega.swapi.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CharacterService {

    private final String BASE_URL = "https://swapi.dev/api/people/";

    @Autowired
    private CharacterRepository characterRepository;

    public List<Character> getAllCharacters() {
        return characterRepository.findAll(); // Fetch all characters from DB
    }

    public List<Character> searchCharacterByName(String name) {
        Optional<Character> cachedCharacter = characterRepository.findByName(name);

        if (cachedCharacter.isPresent()) {
            return List.of(cachedCharacter.get()); // Return from DB
        }

        // Fetch from API if not in DB
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL + "?search=" + name, Map.class);
        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            List<Character> characters = mapToCharacterList(results);
            characters.forEach(characterRepository::save); // Save to DB
            return characters;
        }

        return List.of();
    }

    private List<Character> mapToCharacterList(List<Map<String, Object>> results) {
        return results.stream().map(this::mapToCharacter).toList();
    }

    private Character mapToCharacter(Map<String, Object> data) {
        Character character = new Character();
        character.setName((String) data.get("name"));
        character.setHeight((String) data.get("height"));
        character.setMass((String) data.get("mass"));
        character.setHairColor((String) data.get("hair_color"));
        character.setSkinColor((String) data.get("skin_color"));
        character.setEyeColor((String) data.get("eye_color"));
        character.setBirthYear((String) data.get("birth_year"));
        character.setGender((String) data.get("gender"));
        character.setImageUrl(getImageUrlForCharacter((String) data.get("name")));
        return character;
    }

    private String getImageUrlForCharacter(String name) {
        switch (name) {
            case "Luke Skywalker":
                return "https://starwars-visualguide.com/assets/img/characters/1.jpg";
            case "Darth Vader":
                return "https://starwars-visualguide.com/assets/img/characters/4.jpg";
            default:
                return "https://starwars-visualguide.com/assets/img/placeholder.jpg";
        }
    }
}
