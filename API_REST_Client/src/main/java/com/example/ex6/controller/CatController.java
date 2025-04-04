package com.example.ex6.controller;

import com.example.ex6.model.Cat;
import com.example.ex6.service.CatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CatController {

    private final CatService catService;

    @Autowired
    public CatController(CatService catService) {
        this.catService = catService;
    }

    // Handler to get all cats
    @GetMapping("/getCats")
    public @ResponseBody Iterable<Cat> getAllCats() {
        return catService.findAllCats();
    }

    // Handler to add a new cat
    @PostMapping(path = "/addCat")
    public @ResponseBody String addNewCat(@RequestParam String name,
            @RequestParam String birthdate,
            @RequestParam Integer buyerId,
            @RequestParam Integer breedId,
            @RequestParam String funFact,
            @RequestParam String description,
            @RequestParam(required = false) Boolean isPurchased) {
        return catService.addNewCat(name, birthdate, buyerId, breedId, funFact, description, isPurchased);
    }

    // Handler to update an existing cat
    @PutMapping(path = "/updateCatInformation")
    public @ResponseBody String updateCat(@RequestParam Integer id,
            @RequestParam String name,
            @RequestParam String birthdate,
            @RequestParam Integer buyerId,
            @RequestParam Integer breedId,
            @RequestParam String funFact,
            @RequestParam String description) {
        return catService.updateCatInformation(id, name, birthdate, buyerId, breedId, funFact, description);
    }

    // Handler to update an existing cat
    @PutMapping(path = "/updatePurshase")
    public @ResponseBody String updateCat(@RequestParam Integer id,
            @RequestParam Integer buyerId) {
        return catService.purchaseCat(id, buyerId);
    }

    // Handler to delete a cat
    @DeleteMapping(path = "/deleteCat")
    public @ResponseBody String deleteCat(@RequestParam Integer id) {
        return catService.deleteCat(id);
    }
}
