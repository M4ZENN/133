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

    // Test route
    @GetMapping("/")
    public String getNothing() {
        return "";
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Optional<UserDTO> userDTO = userService.login(email, password);

        if (userDTO.isPresent()) {
            UserDTO user = userDTO.get();

            String role = user.getIsAdmin() ? "Admin" : "Client";
            System.out.println("User " + user.getEmail() + " with id : " + user.getId() + " logged in as " + role + ".");

            // Returning the userDTO in the response body.
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body(null); // Or include a custom error message if needed.
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
