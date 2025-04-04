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

    // Retrieve all users
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

    // Authentication method
    public Optional<User> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
    
        if (user.isPresent()) {
            System.out.println("User found: " + user.get().getEmail());
            System.out.println("Stored Password: " + user.get().getPassword());  // This is the hashed password
            System.out.println("Entered Password: " + password);  // This is the plain text password

            // Check if the entered password matches the stored hashed password
            if (hashPassword(password).equals(user.get().getPassword())) {

                System.out.println("Passwords match! Returning user.");
                return user;
            } else {
                System.out.println("Passwords do NOT match!");
                System.out.println("Entered Hashed Password : " + hashPassword(password)); 
            }
        } else {
        System.out.println("No user found with this email.");
        }

        return Optional.empty();
    }

    public static String hashPassword(String password) {
        try {
            // Create a MessageDigest instance for SHA-256 (you can use other algorithms)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Convert the hashed bytes into a hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }
            
            return hexString.toString(); // Return the hashed password as a hexadecimal string
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
/*
    @Transactional
    public ResponseEntity<String> addNewUser(
            String firstName,
            String lastName,
            String birthDate,
            String email,
            String phoneNumber,
            String password,
            boolean isAdmin) {

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email already in use.");
        }

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest().body("Phone number already in use.");
        }

        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setBirthDate(birthDate);
        newUser.setEmail(email);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setPassword(password); // Hashing recommended
        newUser.setAdmin(isAdmin);

        userRepository.save(newUser);
        return ResponseEntity.ok("User saved successfully.");
    }
*/
    
}
