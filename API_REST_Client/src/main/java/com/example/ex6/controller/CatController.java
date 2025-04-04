package com.example.ex6.controller;

import com.example.ex6.model.Cat;
import com.example.ex6.service.CatService;

import org.apache.commons.lang3.time.DateFormatUtils;
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
    public @ResponseBody String addNewCat( @RequestParam String name, 
    @RequestParam String birthdate,
    @RequestParam Integer buyerId,
    @RequestParam Integer breedId,
    @RequestParam String funFact,
    @RequestParam String description,
    @RequestParam(required = false) Boolean isPurchased) {
        return catService.addNewCat(name, birthdate, buyerId, breedId, funFact, description, isPurchased);
    }

    // Handler to update an existing cat
    @PutMapping(path = "/updateCat/{id}")
    public @ResponseBody String updateCat(@PathVariable Integer id, @RequestBody Cat updatedCat) {
        return catService.updateCat(id, updatedCat);
    }

    // Handler to delete a cat
    @DeleteMapping(path = "/deleteCat/{id}")
    public @ResponseBody String deleteCat(@PathVariable Integer id) {
        return catService.deleteCat(id);
    }
}
