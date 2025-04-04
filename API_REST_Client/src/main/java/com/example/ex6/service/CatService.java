package com.example.ex6.service;

import com.example.ex6.dto.CatDTO;
import com.example.ex6.model.Breed;
import com.example.ex6.model.Cat;
import com.example.ex6.repository.CatRepository;
import com.example.ex6.repository.BreedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
public class CatService {

    private final CatRepository catRepository;
    private final BreedRepository breedRepository;

    @Autowired
    public CatService(CatRepository catRepository, BreedRepository breedRepository) {
        this.catRepository = catRepository;
        this.breedRepository = breedRepository;
    }

    public Iterable<Cat> findAllCats() {
        return catRepository.findAll();
    }

    // Refactored addNewCat method to accept parameters directly
    public String addNewCat(String name, String birthdate, Integer buyerId, Integer breedId,
            String funFact, String description, Boolean isPurchased) {
        // Convert birthdate from string to Date using SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedBirthdate;
        try {
            parsedBirthdate = new Date(sdf.parse(birthdate).getTime());
        } catch (Exception e) {
            return "Invalid date format. Please use 'yyyy-MM-dd'.";
        }

        // Find the breed by breedId
        Breed breed = breedRepository.findById(breedId).orElse(null);
        if (breed == null) {
            return "Breed not found";
        }

        // Create a new Cat object and set properties
        Cat cat = new Cat();
        cat.setName(name);
        cat.setBirthdate(parsedBirthdate);
        cat.setBuyerFk(buyerId); // Assuming Buyer ID is linked with the cat
        cat.setBreed(breed);
        cat.setFunFact(funFact);
        cat.setDescription(description);
        cat.setIsPurchased(isPurchased != null ? isPurchased : false); // Default to false if not provided

        // Save the cat in the repository
        catRepository.save(cat);
        return "Cat saved successfully";
    }

    public String updateCat(Integer id, Cat updatedCat) {
        Optional<Cat> existingCat = catRepository.findById(id);
        if (existingCat.isPresent()) {
            Cat cat = existingCat.get();
            cat.setName(updatedCat.getName());
            cat.setBirthdate(updatedCat.getBirthdate());
            cat.setBreed(updatedCat.getBreed());
            cat.setFunFact(updatedCat.getFunFact());
            cat.setDescription(updatedCat.getDescription());
            cat.setImage(updatedCat.getImage());
            cat.setIsPurchased(updatedCat.getIsPurchased());
            catRepository.save(cat);
            return "Updated";
        }
        return "Cat not found";
    }

    public String purchaseCat(Integer id, Integer buyerId) {
        Optional<Cat> cat = catRepository.findById(id);
        if (cat.isPresent()) {
            Cat existingCat = cat.get();
            existingCat.setIsPurchased(true); // Mark the cat as purchased
            // existingCat.setBuyerId(buyerId); // Store the buyer ID
            catRepository.save(existingCat);
            return "Cat purchased successfully";
        }
        return "Cat not found";
    }

    public String deleteCat(Integer id) {
        catRepository.deleteById(id);
        return "Deleted";
    }
}
