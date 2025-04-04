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

import com.example.ex6.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class Controller {

    private final UserService userService;
    private final RestTemplate restTemplate;

    @Autowired
    public Controller(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    // Login - send login data to RESTAPP
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        
        // Data to be sent to RESTAPP
        String url = "http://localhost:8081/restapp/login";  // Replace with RESTAPP login URL

        // Prepare the request data (login details)
        String requestBody = "email=" + email + "&password=" + password;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Make the POST request to RESTAPP
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Process the response from RESTAPP
        if (response.getStatusCode() == HttpStatus.OK) {
            session.setAttribute("email", email);
            session.setAttribute("isAdmin", "User Admin");  // Use actual data from response if needed

            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials or server error.");
        }

//TEST GIT

    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Session destroyed, user logged out!");
    }

    // Other methods ...
}
