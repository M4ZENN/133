package com.example.ex6.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ex6.model.Breed;
import com.example.ex6.model.Cat;
import com.example.ex6.service.CatService;

@RestController
public class CatController {

    private final CatService catService;

    @Autowired
    public CatController(CatService catService) {
        this.catService = catService;
    }

    // Handler to get all cats
    @GetMapping("/getCats")
    public Iterable<Cat> getAllCats() {
        return catService.findAllCats();
    }

    @GetMapping("/getCat")
    public ResponseEntity<Map<String, String>> getCat(@RequestParam Integer id) {
        Map<String, String> response = new HashMap<>();
        
        if (id == null) {
            response.put("message", "Invalid request. ID is required.");
            return ResponseEntity.badRequest().body(response);
        }
        
        Map<String, String> catDetails = catService.getCat(id); // Fetching cat details
        if (catDetails != null) {
            return ResponseEntity.ok(catDetails); // Return cat details if found
        } else {
            response.put("message", "Cat not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Return not found if cat doesn't exist
        }
    }

    // Handler to add a new cat
    @PostMapping(path = "/addCat")
    public ResponseEntity<Map<String, String>> addNewCat(@RequestParam String name,
            @RequestParam String birthdate,
            @RequestParam Integer buyerId,
            @RequestParam Integer breedId,
            @RequestParam String funFact,
            @RequestParam String description,
            @RequestParam(required = false) Boolean isPurchased) {
        
        String result = catService.addNewCat(name, birthdate, buyerId, breedId, funFact, description, isPurchased);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        
        return ResponseEntity.ok(response);
    }

    // Handler to update an existing cat
    @PutMapping(path = "/updateCatInformation")
    public ResponseEntity<Map<String, String>> updateCat(@RequestParam Integer id,
            @RequestParam String name,
            @RequestParam String birthdate,
            @RequestParam Integer buyerId,
            @RequestParam Integer breedId,
            @RequestParam String funFact,
            @RequestParam String description) {
        
        String result = catService.updateCatInformation(id, name, birthdate, buyerId, breedId, funFact, description);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        
        return ResponseEntity.ok(response);
    }

    // Handler to update purchase status
    @PutMapping(path = "/updatePurshase")
    public ResponseEntity<Map<String, String>> updatePurchase(@RequestParam Integer id,
            @RequestParam Integer buyerId) {
        
        String result = catService.purchaseCat(id, buyerId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/deleteCat")
    public ResponseEntity<Map<String, String>> deleteCat(@RequestParam Integer id) {
        String result = catService.deleteCat(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getBreeds")
    public Iterable<Breed> getAllBreeds() {
        return catService.findAllBreeds();
    }
    
}