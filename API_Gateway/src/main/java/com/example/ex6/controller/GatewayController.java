package com.example.ex6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    private final RestTemplate restTemplate;

    @Autowired
    public GatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Reusable method to forward requests to external services
    private ResponseEntity<String> forwardRequest(String url, HttpMethod method, String requestBody) {
        // Prepare the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Wrap the request body and headers in an HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Forward the request using RestTemplate
        return restTemplate.exchange(url, method, entity, String.class);
    }

    // Login - forward login data to RESTAPP
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password,
            HttpSession session) {
        String url = "http://localhost:8081/user/login";
        String requestBody = "email=" + email + "&password=" + password;

        // Forward the login request and get the response
        ResponseEntity<String> response = forwardRequest(url, HttpMethod.POST, requestBody);

        // If the login is successful, manage session data
        if (response.getStatusCode() == HttpStatus.OK) {
            session.setAttribute("email", email);
            session.setAttribute("isAdmin", "User Admin"); // Dynamically set this from the response if needed
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials or server error.");
        }
    }

    // Logout - invalidate session
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Destroy session
        return ResponseEntity.ok("Session destroyed, user logged out!");
    }

    // Add a new cat - forward to the Cat Service
    @PostMapping("/addCat")
    public ResponseEntity<String> addCat(@RequestParam String name,
            @RequestParam String birthdate,
            @RequestParam Integer buyerId,
            @RequestParam Integer breedId,
            @RequestParam String funFact,
            @RequestParam String description,
            @RequestParam(required = false) Boolean isPurchased) {
        String url = "http://localhost:8082/addCat"; // Replace with the actual URL of the cat API

        // Constructing the request body based on new parameters
        String requestBody = "name=" + name + "&birthdate=" + birthdate +
                "&buyerId=" + buyerId + "&breedId=" + breedId +
                "&funFact=" + funFact + "&description=" + description;

        // Optionally add isPurchased to the request body if it's provided
        if (isPurchased != null) {
            requestBody += "&isPurchased=" + isPurchased;
        }

        // Forward the request to the Cat Service
        ResponseEntity<String> response = forwardRequest(url, HttpMethod.POST, requestBody);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("Cat added successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to add cat.");
        }
    }

    // Buy a cat - forward to the Cat Service
    @PostMapping("/cat/buy")
    public ResponseEntity<String> buyCat(@RequestParam String name) {
        String url = "http://localhost:8082/buyCat"; // Replace with actual Cat Buy URL
        String requestBody = "name=" + name;

        // Forward the request to the Cat Service
        ResponseEntity<String> response = forwardRequest(url, HttpMethod.POST, requestBody);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("Cat bought successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to buy cat.");
        }
    }

    // Other methods can follow the same pattern...
}
