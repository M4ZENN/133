package com.example.ex6.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.ex6.dto.UserDTO;
import com.example.ex6.service.UserService;
import jakarta.servlet.http.HttpSession;

@RestController
public class Controller {

    private final UserService userService;

    @Autowired
    public Controller(UserService userService) {
        this.userService = userService;
    }

    // ✅ Route de test ou de base (retourne une chaîne vide)
    @GetMapping("/")
    public String getNothing() {
        return "";
    }

    // ✅ Gère la connexion de l'utilisateur et retourne un UserDTO si succès
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestParam String email, @RequestParam String password,
            HttpSession session) {
        Optional<UserDTO> userDTO = userService.login(email, password);

        if (userDTO.isPresent()) {
            UserDTO user = userDTO.get();

            String role = user.getIsAdmin() ? "Admin" : "Client";
            System.out.println("Utilisateur " + user.getEmail() + " avec l'ID : " + user.getId()
                    + " connecté en tant que " + role + ".");

            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ✅ Déconnecte l'utilisateur en invalidant la session
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Session détruite, utilisateur déconnecté !");
    }

    // ✅ Retourne et incrémente le nombre de visites dans la session
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
