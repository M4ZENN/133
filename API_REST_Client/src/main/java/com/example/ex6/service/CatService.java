package com.example.ex6.service;

import com.example.ex6.model.Breed;
import com.example.ex6.model.Cat;
import com.example.ex6.repository.CatRepository;
import com.example.ex6.repository.BreedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
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

    public String addNewCat(String name, String birthdate, Integer breedId, String funFact, String description) {
        Breed breed = breedRepository.findById(breedId).orElse(null);
        if (breed == null) {
            return "breed not found";
        }

        Cat newCat = new Cat();
        newCat.setName(name);
        newCat.setBirthdate(birthdate); // Assuming birthdate is a String, you might want to parse it to a Date object
        newCat.setBreed(breed); // Assuming breed is a String, you might want to fetch the Breed object from the database
        newCat.setFunFact(funFact);
        newCat.setDescription(description);
        catRepository.save(newCat);
        return "saved";
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
            existingCat.setIsPurchased(true);  // Mark the cat as purchased
           // existingCat.setBuyerId(buyerId);   // Store the buyer ID
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
