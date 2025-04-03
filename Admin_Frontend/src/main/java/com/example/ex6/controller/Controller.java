package com.example.ex6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ex6.dto.SkieurDTO;
import com.example.ex6.service.PaysService;
import com.example.ex6.service.SkieurService;

//NSAIUD

@RestController
public class Controller {

    private final SkieurService skieurService;
    private final PaysService paysService;

    @Autowired
    public Controller(SkieurService skieurService, PaysService paysService) {
        this.skieurService = skieurService;
        this.paysService = paysService;
    }

    // Handler pour GET
    @GetMapping("/")
    public String getNothing() {
        return "";
    }

    @PostMapping(path = "/addPays")
    public @ResponseBody String addNewPays(@RequestParam String name) {
        return paysService.addNewPays(name);
    }

    @PostMapping(path = "/addSkieur")
    public @ResponseBody String addNewSkieur(@RequestParam String name, @RequestParam Integer position,
            @RequestParam Integer paysId) {
        return skieurService.addNewSkieur(name, position, paysId);
    }

    @GetMapping(path = "/getSkieur")
    public @ResponseBody Iterable<SkieurDTO> getAllUsers() {
        // This returns a JSON or XML with the users
        return skieurService.findAllSkieurs();
    }
}