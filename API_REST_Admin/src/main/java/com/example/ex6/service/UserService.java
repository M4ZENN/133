package com.example.ex6.service;

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
    // Authentication method
    public Optional<User> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
        return user;
        }
     return Optional.empty();
    }
}
