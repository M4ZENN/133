package com.example.ex6.controller;

import com.example.ex6.model.Cat;
import com.example.ex6.service.CatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        // This returns a list of all cats in the system
        return catService.findAllCats();
    }

    // Handler to add a new cat
    @PostMapping(path = "/addCat")
    public @ResponseBody String addNewCat(@RequestBody Cat cat) {
        // This will add a new cat to the database
        return catService.addNewCat(cat);
    }

    // Handler to update an existing cat
    @PutMapping(path = "/updateCat/{id}")
    public @ResponseBody String updateCat(@PathVariable Integer id, @RequestBody Cat updatedCat) {
        // This will update an existing cat based on the provided id
        return catService.updateCat(id, updatedCat);
    }

    // Handler to delete a cat
    @DeleteMapping(path = "/deleteCat/{id}")
    public @ResponseBody String deleteCat(@PathVariable Integer id) {
        // This will delete a cat based on the provided id
        return catService.deleteCat(id);
    }
}
