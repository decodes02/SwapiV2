package com.pega.swapi.controller;


import com.pega.swapi.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FilmController {


    @Autowired
    private FilmService filmService;


    @GetMapping("/films")
    public String getFilms(@RequestParam(required = false) String title, Model model) {
        if (title != null && !title.isEmpty()) {
            model.addAttribute("films", filmService.searchFilmByTitle(title));
        } else {
            model.addAttribute("films", filmService.getAllFilms());
        }
        model.addAttribute("title", title); // Pass the search parameter to the template
        return "films";
    }
}
