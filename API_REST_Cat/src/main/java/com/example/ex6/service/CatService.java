package com.example.ex6.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ex6.dto.CatDTO;
import com.example.ex6.model.Breed;
import com.example.ex6.model.Cat;
import com.example.ex6.repository.BreedRepository;
import com.example.ex6.repository.CatRepository;

@Service
public class CatService {

    private final CatRepository catRepository;
    private final BreedRepository breedRepository;

    @Autowired
    public CatService(CatRepository catRepository, BreedRepository breedRepository) {
        this.catRepository = catRepository;
        this.breedRepository = breedRepository;
    }

    // ✅ Récupère tous les chats dans la base de données
    public Iterable<Cat> findAllCats() {
        return catRepository.findAll(); // ✅ Retourne tous les chats
    }

    // ✅ Ajoute un nouveau chat avec les informations fournies
    public String addNewCat(String name, String birthdate, Integer buyerId, Integer breedId,
            String funFact, String description, Boolean isPurchased) {

        // ✅ Conversion de la date de naissance de String en Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedBirthdate;
        try {
            parsedBirthdate = new Date(sdf.parse(birthdate).getTime());
        } catch (Exception e) {
            return "Format de date invalide. Veuillez utiliser 'yyyy-MM-dd'.";
        }

        // ✅ Recherche la race par ID
        Breed breed = breedRepository.findById(breedId).orElse(null);
        if (breed == null) {
            return "Race non trouvée";
        }

        // ✅ Création et sauvegarde du chat dans la base de données
        Cat cat = new Cat();
        cat.setName(name);
        cat.setBirthdate(parsedBirthdate);
        cat.setBuyerFk(buyerId);
        cat.setBreed(breed);
        cat.setFunFact(funFact);
        cat.setDescription(description);
        cat.setIsPurchased(isPurchased != null ? isPurchased : false); // ✅ Définit l'état d'achat

        catRepository.save(cat); // ✅ Sauvegarde du chat
        return "Chat enregistré avec succès";
    }

    // ✅ Met à jour les informations d'un chat existant
    public String updateCatInformation(Integer id, String name, String birthdate, Integer buyerId, Integer breedId,
            String funFact, String description) {

        // ✅ Conversion de la date de naissance en Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedBirthdate;
        try {
            parsedBirthdate = new Date(sdf.parse(birthdate).getTime());
        } catch (Exception e) {
            return "Format de date invalide. Veuillez utiliser 'yyyy-MM-dd'.";
        }

        // ✅ Recherche du chat existant par ID
        Optional<Cat> existingCat = catRepository.findById(id);
        if (existingCat.isPresent()) {
            Cat cat = existingCat.get();
            cat.setName(name);
            cat.setBirthdate(parsedBirthdate);
            cat.setBuyerFk(buyerId);

            // ✅ Recherche la race par ID
            Breed breed = breedRepository.findById(breedId).orElse(null);
            if (breed == null) {
                return "Race non trouvée";
            }
            cat.setBreed(breed);
            cat.setFunFact(funFact);
            cat.setDescription(description);

            catRepository.save(cat);
            return "Chat mis à jour avec succès";
        }
        return "Chat non trouvé";
    }

    // ✅ Marque un chat comme acheté

    public String purchaseCat(Integer id, Integer buyerId) {
System.out.println("ID du chat : " + id + ", ID de l'acheteur : " + buyerId); // ✅ Affiche les IDs pour le débogage
        Optional<Cat> cat = catRepository.findById(id);
        if (cat.isPresent()) {
            Cat existingCat = cat.get();
            existingCat.setIsPurchased(true); // ✅ Le chat est marqué comme acheté
            existingCat.setBuyerFk(buyerId);
            catRepository.save(existingCat);
            return "Chat acheté avec succès";
        }
        return "Chat non trouvé";
    }

    // ✅ Supprime un chat en fonction de son ID
    public String deleteCat(Integer id) {
        Optional<Cat> existingCat = catRepository.findById(id);
        if (existingCat.isPresent()) {
            catRepository.delete(existingCat.get()); // ✅ Suppression du chat
            return "Chat supprimé avec succès";
        }
        return "Chat non trouvé";
    }

    // ✅ Récupère toutes les races de chats
    public Iterable<Breed> findAllBreeds() {
        return breedRepository.findAll();
    }

    // ✅ Récupère les détails d'un chat spécifique
    public Map<String, String> getCat(Integer id) {
        Map<String, String> catDetails = new HashMap<>();
        Optional<Cat> cat = catRepository.findById(id);

        if (cat.isPresent()) {
            Cat existingCat = cat.get();
            catDetails.put("id", existingCat.getId().toString());
            catDetails.put("name", existingCat.getName());
            catDetails.put("birthdate", existingCat.getBirthdate().toString());
            catDetails.put("breed", existingCat.getBreed() != null ? existingCat.getBreed().getName() : "Inconnue");
            catDetails.put("funFact", existingCat.getFunFact());
            catDetails.put("description", existingCat.getDescription());
            catDetails.put("isPurchased",
                    existingCat.getIsPurchased() != null && existingCat.getIsPurchased() ? "Oui" : "Non");

            return catDetails;
        }

        return null;
    }
}
