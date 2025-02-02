package com.pega.swapi.controller;


import com.pega.swapi.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CharacterController {


    @Autowired
    private CharacterService characterService;


    @GetMapping("/characters")
    public String getCharacters(@RequestParam(required = false) String name, Model model) {
        if (name != null && !name.isEmpty()) {
            model.addAttribute("characters", characterService.searchCharacterByName(name));
        } else {
            model.addAttribute("characters", characterService.getAllCharacters());
        }
        model.addAttribute("name", name); // Pass the search parameter to the template
        return "characters";
    }
}
