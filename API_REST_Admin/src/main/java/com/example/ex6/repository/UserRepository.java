package com.example.ex6.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.ex6.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByEmail(String email); // for add user 
    boolean existsByPhoneNumber(String phoneNumber); // for add user 
    Optional<User> findByEmail(String email);
}
