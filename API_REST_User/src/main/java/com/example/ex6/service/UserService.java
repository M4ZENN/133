package com.example.ex6.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ex6.dto.UserDTO;
import com.example.ex6.model.User;
import com.example.ex6.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Récupère tous les utilisateurs sous forme de DTO
    public Iterable<UserDTO> findAllUsers() {
        Iterable<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getBirthDate(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.isAdmin());
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }

    // ✅ Authentifie un utilisateur avec email et mot de passe
    public Optional<UserDTO> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            User u = user.get();
            System.out.println("Utilisateur trouvé : " + u.getEmail());
            System.out.println("Mot de passe enregistré : " + u.getPassword());
            System.out.println("Mot de passe entré : " + password);

            if (hashPassword(password).equals(u.getPassword())) {
                System.out.println("Mot de passe correct !");
                return Optional.of(u.toDTO());
            } else {
                System.out.println("Mot de passe incorrect !");
                System.out.println("Mot de passe entré (haché) : " + hashPassword(password));
            }
        } else {
            System.out.println("Aucun utilisateur trouvé avec cet email.");
        }

        return Optional.empty(); // ❌ Échec de la connexion
    }

    // ✅ Hache un mot de passe en utilisant SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString(); // Retourne le mot de passe haché en hexadécimal
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
