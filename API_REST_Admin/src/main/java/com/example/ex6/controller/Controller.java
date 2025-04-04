package com.example.ex6.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ex6.model.User;
import com.example.ex6.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class Controller {

    private final UserService userService;

    @Autowired
    public Controller(UserService userService) {
        this.userService = userService;
    }

    // Test route
    @GetMapping("/")
    public String getNothing() {
        return "";
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Optional<User> user = userService.login(email, password);
        if (user.isPresent()) {
            session.setAttribute("email", email);
            session.setAttribute("isAdmin", user.get().isAdmin()); // Store the user role (admin/client)
            
            // Track the number of visits if not already set
            if (session.getAttribute("visites") == null) {
                session.setAttribute("visites", 0);
            }
            
            String role = user.get().isAdmin() ? "Admin" : "Client";
            return ResponseEntity.ok("User " + email + " logged in as " + role + ".");
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials.");
        }
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Session destroyed, user logged out!");
    }

    // Session visit count
    @GetMapping("/visites")
    public ResponseEntity<String> getVisites(HttpSession session) {
        Integer visites = (Integer) session.getAttribute("visites");
        if (visites == null) {
            return ResponseEntity.badRequest().body("No active session!");
        }
        visites++;
        session.setAttribute("visites", visites);
        return ResponseEntity.ok("Number of visits: " + visites);
    }

    /* 
    // Add a new user
    @PostMapping("/add")
    public ResponseEntity<String> addNewUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String birthDate,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String password,
            @RequestParam(defaultValue = "false") boolean isAdmin) {
        return userService.addNewUser(firstName, lastName, birthDate, email, phoneNumber, password, isAdmin);
    }
    */

    /*
    // Get all users
    @GetMapping("/all")
    public ResponseEntity<Iterable<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }
    */
}
