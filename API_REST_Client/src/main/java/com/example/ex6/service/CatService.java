package com.example.ex6.service;

import com.example.ex6.dto.CatDTO;
import com.example.ex6.model.Cat;
import com.example.ex6.model.Breed;
import com.example.ex6.repository.CatRepository;
import com.example.ex6.repository.BreedRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    // Method to get all cats with their basic details
    public Iterable<CatDTO> findAllCats() {
        Iterable<Cat> cats = catRepository.findAll();
        List<CatDTO> catDTOs = new ArrayList<>();
        for (Cat cat : cats) {
            CatDTO catDTO = new CatDTO(
                    cat.getId(),
                    cat.getName(),
                    cat.getBirthdate(),
                    cat.getBreed() != null ? cat.getBreed().getName() : null,  // Assuming Breed has a 'name' attribute
                    cat.getFunFact(),
                    cat.getDescription(),
                    cat.getIsPurchased()
            );
            catDTOs.add(catDTO);
        }
        return catDTOs;
    }

    // Method to add a new cat
    @Transactional
    public String addNewCat(String name, String funFact, String description, Boolean isPurchased, Integer breedId) {
        // Check if the breed exists
        Optional<Breed> breedOpt = breedRepository.findById(breedId);
        if (!breedOpt.isPresent()) {
            return "Breed not found";
        }

        // Create a new Cat object and set the attributes
        Cat newCat = new Cat();
        newCat.setName(name);
        newCat.setFunFact(funFact);
        newCat.setDescription(description);
        newCat.setIsPurchased(isPurchased);
        newCat.setBreed(breedOpt.get());

        // Save the new cat to the repository
        catRepository.save(newCat);
        return "Cat saved successfully!";
    }

    // Method to update an existing cat
    @Transactional
    public String updateCat(Integer id, Cat updatedCat) {
        Optional<Cat> existingCatOpt = catRepository.findById(id);
        if (existingCatOpt.isPresent()) {
            Cat existingCat = existingCatOpt.get();

            // Update the cat fields
            existingCat.setName(updatedCat.getName());
            existingCat.setFunFact(updatedCat.getFunFact());
            existingCat.setDescription(updatedCat.getDescription());
            existingCat.setIsPurchased(updatedCat.getIsPurchased());

            // Check if the breed exists and set it
            if (updatedCat.getBreed() != null) {
                Optional<Breed> breedOpt = breedRepository.findById(updatedCat.getBreed().getId());
                breedOpt.ifPresent(existingCat::setBreed);
            }

            // Save the updated cat
            catRepository.save(existingCat);
            return "Cat updated successfully!";
        } else {
            return "Cat not found!";
        }
    }

    // Method to delete a cat by id
    @Transactional
    public String deleteCat(Integer id) {
        if (catRepository.existsById(id)) {
            catRepository.deleteById(id);
            return "Cat deleted successfully!";
        } else {
            return "Cat not found!";
        }
    }
}
