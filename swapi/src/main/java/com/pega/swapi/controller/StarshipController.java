package com.pega.swapi.controller;


import com.pega.swapi.service.StarshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class StarshipController {


    @Autowired
    private StarshipService starshipService;


    @GetMapping("/starships")
    public String getStarships(@RequestParam(required = false) String name, Model model) {
        if (name != null && !name.isEmpty()) {
            model.addAttribute("starships", starshipService.searchStarshipByName(name));
        } else {
            model.addAttribute("starships", starshipService.getAllStarships());
        }
        model.addAttribute("name", name); // Pass the search parameter to the template
        return "starships";
    }
}
