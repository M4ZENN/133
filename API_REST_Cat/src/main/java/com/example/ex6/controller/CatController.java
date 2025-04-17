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

    // ✅ Récupère tous les chats
    @GetMapping("/getCats")
    public Iterable<Cat> getAllCats() {
        return catService.findAllCats(); // Retourne tous les chats
    }

    // ✅ Récupère un chat par son ID
    @GetMapping("/getCat")
    public ResponseEntity<Map<String, String>> getCat(@RequestParam Integer id) {
        Map<String, String> response = new HashMap<>();

        if (id == null) {
            response.put("message", "Requête invalide. L'ID est requis.");
            return ResponseEntity.badRequest().body(response); // Erreur si l'ID est manquant
        }

        Map<String, String> catDetails = catService.getCat(id); // Récupère les détails du chat
        if (catDetails != null) {
            return ResponseEntity.ok(catDetails); // Retourne les détails du chat si trouvé
        } else {
            response.put("message", "Chat non trouvé.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Retourne une erreur si chat non trouvé
        }
    }

    // ✅ Ajoute un nouveau chat
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
        response.put("message", result); // Retourne le résultat de l'ajout du chat

        return ResponseEntity.ok(response); // Retourne la réponse avec le message
    }

    // ✅ Met à jour un chat existant
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
        response.put("message", result); // Retourne le message de mise à jour

        return ResponseEntity.ok(response); // Retourne la réponse avec le message
    }

    // ✅ Met à jour le statut d'achat d'un chat
    @PutMapping(path = "/updatePurshase")
    public ResponseEntity<Map<String, String>> updatePurchase(@RequestParam Integer id,
            @RequestParam Integer buyerId) {

        String result = catService.purchaseCat(id, buyerId);

        Map<String, String> response = new HashMap<>();
        response.put("message", result); // Retourne le message de mise à jour de l'achat

        return ResponseEntity.ok(response); // Retourne la réponse avec le message
    }

    // ✅ Supprime un chat
    @DeleteMapping(path = "/deleteCat")
    public ResponseEntity<Map<String, String>> deleteCat(@RequestParam Integer id) {
        String result = catService.deleteCat(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", result); // Retourne le message de suppression

        return ResponseEntity.ok(response); // Retourne la réponse avec le message
    }

    // ✅ Récupère toutes les races de chats
    @GetMapping("/getBreeds")
    public Iterable<Breed> getAllBreeds() {
        return catService.findAllBreeds(); // Retourne toutes les races de chats
    }
}
