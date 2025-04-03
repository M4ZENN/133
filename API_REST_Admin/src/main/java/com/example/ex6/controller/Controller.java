package com.example.ex6.controller;

import com.example.ex6.dto.SkieurDTO;
import com.example.ex6.model.Pays;
import com.example.ex6.model.Skieur;
import com.example.ex6.repository.PaysRepository;
import com.example.ex6.repository.SkieurRepository;
import com.example.ex6.service.PaysService;
import com.example.ex6.service.SkieurService;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        session.setAttribute("username", username);
    
        // Initialize visit counter if not already set
        if (session.getAttribute("visites") == null) {
            session.setAttribute("visites", 0);
        }

        return ResponseEntity.ok("User " + username + " logged in.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Session détruite, utilisateur déconnecté !");
    }

    @GetMapping("/visites")
    public ResponseEntity<String> getVisites(HttpSession session) {
        Integer visites = (Integer) session.getAttribute("visites");
        if (visites == null) {
            return ResponseEntity.badRequest().body("Aucune session active !");
        }
        visites++;
        session.setAttribute("visites", visites);
        return ResponseEntity.ok("Nombre de visites : " + visites);
    }
}