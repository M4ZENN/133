package com.example.ex6.controller;

import java.util.HashMap;
import java.util.Map;

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

import com.example.ex6.dto.CatDTO;
import com.example.ex6.dto.UserDTO;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    private final RestTemplate restTemplate;

    @Autowired
    public GatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isAdmin(HttpSession session) {
        return (Boolean) session.getAttribute("isAdmin"); 
    }

    // Reusable method to forward requests to external services (now generic)
    private <T> ResponseEntity<T> forwardRequest(String url, HttpMethod method, String requestBody,
            Class<T> responseType) {
        // Prepare the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Wrap the request body and headers in an HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Forward the request using RestTemplate and expect a response of type T
        return restTemplate.exchange(url, method, entity, responseType);
    }

    // Login - forward login data to RESTAPP
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password,
            HttpSession session) {
        String url = "http://host.docker.internal:8081/login";
        String requestBody = "email=" + email + "&password=" + password;

        // Forward the login request and expect a UserDTO response
        ResponseEntity<UserDTO> response = forwardRequest(url, HttpMethod.POST, requestBody, UserDTO.class);

        // If the login is successful, manage session data
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            UserDTO user = response.getBody();

            // Store user-related data in session (adjust as needed)
            session.setAttribute("email", user.getEmail());
            session.setAttribute("userId", user.getId());
            session.setAttribute("isAdmin", user.getIsAdmin() ? "Admin" : "Client");

            // Create a response body with both success message and user info
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message",
                    "Logged in as " + user.getEmail() + " with role " + (user.getIsAdmin() ? "Admin" : "Client"));
            responseBody.put("user", user); // Include the UserDTO object in the response

            // Return a success response with user info
            return ResponseEntity.ok(responseBody);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid credentials or server error.");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Logout - invalidate session
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Session destroyed, user logged out!");
        return ResponseEntity.ok(response);
    }

    // Get all cats - forward to the Cat Service
    @GetMapping("/getCats")
    public ResponseEntity<String> getAllCats() {
        String url = "http://host.docker.internal:8082/getCats"; // URL of the get all cats endpoint

        // Forward the GET request to the Cat Service
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.badRequest().body("Failed to retrieve cats.");
        }
    }

    /// Add a new cat - forward to the Cat Service
    @PostMapping("/addCat")
    public ResponseEntity<String> addCat(@RequestParam String name,
            @RequestParam String birthdate,
            @RequestParam Integer breedId,
            @RequestParam String funFact,
            @RequestParam String description,
            @RequestParam(required = false) Boolean isPurchased) {
        String url = "http://host.docker.internal:8082/addCat"; // Replace with the actual URL of the cat API

        // Constructing the request body based on new parameters
        String requestBody = "name=" + name + "&birthdate=" + birthdate +
                "&buyerId=" + 0 + "&breedId=" + breedId +
                "&funFact=" + funFact + "&description=" + description;

        // Optionally add isPurchased to the request body if it's provided
        if (isPurchased != null) {
            requestBody += "&isPurchased=" + isPurchased;
        }

        try {
            // Forward the request to the Cat Service
            ResponseEntity<String> response = forwardRequest(url, HttpMethod.POST, requestBody, String.class);

            // Return the actual response from the service
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add cat: " + e.getMessage());
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
        String url = "http://host.docker.internal:8082/updateCatInformation"; // URL of the update cat information
                                                                              // endpoint
        String requestBody = "id=" + id + "&name=" + name + "&birthdate=" + birthdate +
                "&buyerId=" + buyerId + "&breedId=" + breedId +
                "&funFact=" + funFact + "&description=" + description;

        // Forward the request to the Cat Service
        ResponseEntity<String> response = forwardRequest(url, HttpMethod.PUT, requestBody, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("Cat information updated successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to update cat information.");
        }
    }

    // Update purchase status - forward to the Cat Service
    @PutMapping("/updatePurchase")
    public ResponseEntity<Map<String, String>> updatePurchase(@RequestParam Integer id,
            @RequestParam Integer buyerId) {
        String url = "http://host.docker.internal:8082/updatePurshase"; // URL of the update purchase endpoint
        String requestBody = "id=" + id + "&buyerId=" + buyerId;

        // Forward the request to the Cat Service
        ResponseEntity<String> response = forwardRequest(url, HttpMethod.PUT, requestBody, String.class);

        Map<String, String> responseBody = new HashMap<>();

        if (response.getStatusCode() == HttpStatus.OK) {
            responseBody.put("message", "Cat purchased successfully!");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseBody);
        } else {
            responseBody.put("message", "Failed to update cat purchase.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseBody);
        }
    }

    @DeleteMapping(path = "/deleteCat")
    public ResponseEntity<Map<String, String>> deleteCat(@RequestParam Integer id) {
        String url = "http://host.docker.internal:8082/deleteCat?id=" + id;
        ResponseEntity<String> response = forwardRequest(url, HttpMethod.DELETE, null, String.class);

        Map<String, String> responseBody = new HashMap<>();

        if (response.getStatusCode() == HttpStatus.OK) {
            responseBody.put("message", "Cat deleted successfully!");
            return ResponseEntity.ok(responseBody);
        } else {
            responseBody.put("message", "Failed to delete cat.");
            return ResponseEntity.badRequest().body(responseBody);
        }
    }

    // Get all breeds - forward to the Cat Service
    @GetMapping("/getBreeds")
    public ResponseEntity<String> getBreeds() {
        String url = "http://host.docker.internal:8082/getBreeds"; // URL of the get all breeds endpoint

        // Forward the GET request to the Cat Service
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.badRequest().body("Failed to retrieve breeds.");
        }
    }

    @GetMapping("/getCat")
    public ResponseEntity<CatDTO> getCat(@RequestParam Integer id) {
        String url = "http://host.docker.internal:8082/getCat?id=" + id;
        System.out.println("Received request for cat with ID: " + id);

        // Forward the GET request to the Cat Service
        ResponseEntity<CatDTO> response = restTemplate.exchange(url, HttpMethod.GET, null, CatDTO.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.badRequest().body(null); // Or you can provide a custom error message
        }
    }
    
}
