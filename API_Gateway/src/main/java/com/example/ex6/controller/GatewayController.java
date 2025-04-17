package com.example.ex6.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    // ✅ Vérifie si l'utilisateur est un administrateur dans la session
    public boolean isAdmin(HttpSession session) {
        return (Boolean) session.getAttribute("isAdmin");
    }

    // ✅ Méthode générique pour transmettre les requêtes aux services externes
    private <T> ResponseEntity<T> forwardRequest(String url, HttpMethod method, String requestBody,
            Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // ✅ Définit le type de contenu

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers); // ✅ Crée l'entité HTTP

        return restTemplate.exchange(url, method, entity, responseType); // ✅ Effectue l'appel avec RestTemplate
    }

    // ✅ Gère la connexion de l'utilisateur, transmet la requête au service REST et
    // gère la session
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password,
            HttpSession session) {
        String url = "http://host.docker.internal:8081/login"; // ✅ URL de connexion
        String requestBody = "email=" + email + "&password=" + password; // ✅ Corps de la requête

        ResponseEntity<UserDTO> response = forwardRequest(url, HttpMethod.POST, requestBody, UserDTO.class); // ✅
                                                                                                             // Transmet
                                                                                                             // la
                                                                                                             // requête

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            UserDTO user = response.getBody();

            session.setAttribute("email", user.getEmail());
            session.setAttribute("userId", user.getId());
            session.setAttribute("isAdmin", user.getIsAdmin() ? "Admin" : "Client");

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message",
                    "Logged in as " + user.getEmail() + " with role " + (user.getIsAdmin() ? "Admin" : "Client"));
            responseBody.put("user", user); // ✅ Inclut les données de l'utilisateur

            return ResponseEntity.ok(responseBody); // ✅ Retourne la réponse avec succès
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid credentials or server error.");
            return ResponseEntity.badRequest().body(errorResponse); // ✅ Retourne une erreur si échec
        }
    }

    // ✅ Déconnecte l'utilisateur et invalide la session
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate(); // ✅ Invalide la session
        Map<String, String> response = new HashMap<>();
        response.put("message", "Session destroyed, user logged out!");
        return ResponseEntity.ok(response); // ✅ Retourne un message de succès
    }

    // ✅ Récupère tous les chats, transmet la requête au service Cat
    @GetMapping("/getCats")
    public ResponseEntity<List<CatDTO>> getAllCats() {
        String url = "http://host.docker.internal:8082/getCats";
        ResponseEntity<CatDTO[]> response = restTemplate.getForEntity(url, CatDTO[].class); // ✅ Récupère la liste des
                                                                                            // chats

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<CatDTO> cats = Arrays.asList(response.getBody());
            return ResponseEntity.ok(cats); // ✅ Retourne la liste des chats
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    // ✅ Ajoute un nouveau chat en transmettant la requête au service Cat
    @PostMapping("/addCat")
    public ResponseEntity<String> addCat(@RequestParam String name,
            @RequestParam String birthdate,
            @RequestParam Integer breedId,
            @RequestParam String funFact,
            @RequestParam String description,
            @RequestParam(required = false) Boolean isPurchased) {
        String url = "http://host.docker.internal:8082/addCat";
        String requestBody = "name=" + name + "&birthdate=" + birthdate +
                "&buyerId=0" + "&breedId=" + breedId +
                "&funFact=" + funFact + "&description=" + description;

        if (isPurchased != null) {
            requestBody += "&isPurchased=" + isPurchased;
        }

        try {
            ResponseEntity<CatDTO> response = forwardRequest(url, HttpMethod.POST, requestBody, CatDTO.class);
            CatDTO cat = response.getBody();
            if (cat != null) {
                System.out.println("Cat added: " + cat.getName() + " (" + cat.getId() + ")");
            }
            return ResponseEntity.status(response.getStatusCode())
                    .body("Cat added successfully!"); // ✅ Retourne un message de succès
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add cat: " + e.getMessage()); // ✅ Erreur en cas de
                                                                                             // problème
        }
    }

    // ✅ Met à jour les informations d'un chat
    @PutMapping("/updateCatInformation")
    public ResponseEntity<String> updateCatInformation(@RequestParam Integer id,
            @RequestParam String name,
            @RequestParam String birthdate,
            @RequestParam Integer buyerId,
            @RequestParam Integer breedId,
            @RequestParam String funFact,
            @RequestParam String description) {
        String url = "http://host.docker.internal:8082/updateCatInformation";
        String requestBody = "id=" + id + "&name=" + name + "&birthdate=" + birthdate +
                "&buyerId=" + buyerId + "&breedId=" + breedId +
                "&funFact=" + funFact + "&description=" + description;

        try {
            ResponseEntity<CatDTO> response = forwardRequest(url, HttpMethod.PUT, requestBody, CatDTO.class);
            return ResponseEntity.ok("Cat information updated successfully!"); // ✅ Retourne un message de succès
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update cat information."); // ✅ Erreur en cas de problème
        }
    }

    // ✅ Met à jour l'achat d'un chat (marque comme acheté)
    @PutMapping("/updatePurchase")
    public ResponseEntity<Map<String, String>> updatePurchase(@RequestParam Integer id,
            @RequestParam Integer buyerId) {
        String url = "http://host.docker.internal:8082/updatePurshase";
        String requestBody = "id=" + id + "&buyerId=" + buyerId;

        Map<String, String> responseBody = new HashMap<>();

        try {
            ResponseEntity<CatDTO> response = forwardRequest(url, HttpMethod.PUT, requestBody, CatDTO.class);
            responseBody.put("message", "Cat purchased successfully!");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseBody); // ✅ Retourne un
                                                                                                   // message de succès
        } catch (Exception e) {
            responseBody.put("message", "Failed to update cat purchase.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body(responseBody); // ✅ Erreur en cas de problème
        }
    }

    // ✅ Supprime un chat
    @DeleteMapping(path = "/deleteCat")
    public ResponseEntity<Map<String, String>> deleteCat(@RequestParam Integer id) {
        String url = "http://host.docker.internal:8082/deleteCat?id=" + id;
        ResponseEntity<String> response = forwardRequest(url, HttpMethod.DELETE, null, String.class);

        Map<String, String> responseBody = new HashMap<>();

        if (response.getStatusCode() == HttpStatus.OK) {
            responseBody.put("message", "Cat deleted successfully!");
            return ResponseEntity.ok(responseBody); // ✅ Retourne un message de succès
        } else {
            responseBody.put("message", "Failed to delete cat.");
            return ResponseEntity.badRequest().body(responseBody); // ✅ Erreur en cas de problème
        }
    }

    // ✅ Récupère toutes les races de chats
    @GetMapping("/getBreeds")
    public ResponseEntity<String> getBreeds() {
        String url = "http://host.docker.internal:8082/getBreeds";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody()); // ✅ Retourne les races de chats
        } else {
            return ResponseEntity.badRequest().body("Failed to retrieve breeds.");
        }
    }

    // ✅ Récupère un chat spécifique par ID
    @GetMapping("/getCat")
    public ResponseEntity<CatDTO> getCat(@RequestParam Integer id) {
        String url = "http://host.docker.internal:8082/getCat?id=" + id;

        ResponseEntity<CatDTO> response = restTemplate.exchange(url, HttpMethod.GET, null, CatDTO.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody()); // ✅ Retourne le chat demandé
        } else {
            return ResponseEntity.badRequest().body(null); // ✅ Erreur si chat non trouvé
        }
    }
}
