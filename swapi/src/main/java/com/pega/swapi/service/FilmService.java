// FilmService.java
package com.pega.swapi.service;
import com.pega.swapi.model.Film;
import com.pega.swapi.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FilmService {
    private final String BASE_URL = "https://swapi.dev/api/films/";

    @Autowired
    private FilmRepository filmRepository;

    public List<Film> getAllFilms() {
        List<Film> cachedFilms = filmRepository.findAll();
        if (!cachedFilms.isEmpty()) {
            return cachedFilms;
        }
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL, Map.class);
        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            List<Film> films = mapToFilmList(results);
            films.forEach(filmRepository::save);
            return films;
        }
        return List.of();
    }

    public List<Film> searchFilmByTitle(String title) {
        Optional<Film> cachedFilm = filmRepository.findByTitle(title);
        if (cachedFilm.isPresent()) {
            return List.of(cachedFilm.get());
        }
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(BASE_URL + "?search=" + title, Map.class);
        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            List<Film> films = mapToFilmList(results);
            films.forEach(filmRepository::save);
            return films;
        }
        return List.of();
    }

    private List<Film> mapToFilmList(List<Map<String, Object>> results) {
        return results.stream().map(this::mapToFilm).toList();
    }

    private Film mapToFilm(Map<String, Object> data) {
        Film film = new Film();
        film.setTitle((String) data.get("title"));
        film.setEpisodeId(String.valueOf(data.get("episode_id")));
        film.setDirector((String) data.get("director"));
        film.setProducer((String) data.get("producer"));
        film.setReleaseDate((String) data.get("release_date"));
        return film;
    }
}
