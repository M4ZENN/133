package com.example.ex6.service;
import com.example.ex6.dto.CatDTO;
import com.example.ex6.model.Pays;
import com.example.ex6.model.Skieur;
import com.example.ex6.repository.PaysRepository;
import com.example.ex6.repository.SkieurRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkieurService {

    private final SkieurRepository skieurRepository;
    private final PaysRepository paysRepository;

    @Autowired
    public SkieurService(SkieurRepository skieurRepository, PaysRepository paysRepository) {
        this.skieurRepository = skieurRepository;
        this.paysRepository = paysRepository;
    }

    public Iterable<CatDTO> findAllSkieurs() {
        Iterable<Skieur> skieurs = skieurRepository.findAll();
        List<CatDTO> skieurDTOs = new ArrayList<>();
        for (Skieur skieur : skieurs) {
            CatDTO skieurDTO = new CatDTO(
                    skieur.getId(),
                    skieur.getName(),
                    skieur.getPosition(),
                    skieur.getPays() != null ? skieur.getPays().getNom() : null);
            skieurDTOs.add(skieurDTO);
        }
        return skieurDTOs;
    }

    @Transactional
    public String addNewSkieur(String name, Integer position, Integer paysId) {
        Pays pays = paysRepository.findById(paysId).orElse(null);
        if (pays == null) {
            return "Pays not found";
        }
        Skieur newSkieur = new Skieur();
        newSkieur.setName(name);
        newSkieur.setPosition(position);
        newSkieur.setPays(pays);
        skieurRepository.save(newSkieur);
        return "Saved";
    }
}