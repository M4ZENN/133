package com.example.ex6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    // Get all cats - forward to the Cat Service
    @GetMapping("/getCats")
    public ResponseEntity<String> getAllCats() {
        String url = "http://localhost:8081/getCats"; // URL of the get all cats endpoint

        // Forward the GET request to the Cat Service
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.badRequest().body("Failed to retrieve cats.");
        }
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

    // Update cat information - forward to the Cat Service
    @PutMapping("/updateCatInformation")
    public ResponseEntity<String> updateCatInformation(@RequestParam Integer id,
            @RequestParam String name,
            @RequestParam String birthdate,
            @RequestParam Integer buyerId,
            @RequestParam Integer breedId,
            @RequestParam String funFact,
            @RequestParam String description) {
        String url = "http://localhost:8082/updateCatInformation"; // URL of the update cat information endpoint
        String requestBody = "id=" + id + "&name=" + name + "&birthdate=" + birthdate +
                "&buyerId=" + buyerId + "&breedId=" + breedId +
                "&funFact=" + funFact + "&description=" + description;

        // Forward the request to the Cat Service
        ResponseEntity<String> response = forwardRequest(url, HttpMethod.PUT, requestBody);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("Cat information updated successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to update cat information.");
        }
    }

    // Update purchase status - forward to the Cat Service
    @PutMapping("/updatePurchase")
    public ResponseEntity<String> updatePurchase(@RequestParam Integer id,
            @RequestParam Integer buyerId) {
        String url = "http://localhost:8082/updatePurshase"; // URL of the update purchase endpoint
        String requestBody = "id=" + id + "&buyerId=" + buyerId;

        // Forward the request to the Cat Service
        ResponseEntity<String> response = forwardRequest(url, HttpMethod.PUT, requestBody);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("Cat purchase updated successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to update cat purchase.");
        }
    }

    // Delete a cat - forward to the Cat Service
    @DeleteMapping("/deleteCat")
    public ResponseEntity<String> deleteCat(@RequestParam Integer id) {
        String url = "http://localhost:8082/deleteCat"; // URL of the delete cat endpoint
        String requestBody = "id=" + id;

        // Forward the request to the Cat Service
        ResponseEntity<String> response = forwardRequest(url, HttpMethod.DELETE, requestBody);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("Cat deleted successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete cat.");
        }
    }

}
