package com.example.ex6.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CatDTO {
    private Integer id;
    private String name;
    private String birthdate;  // Store as String
    private String breedName;
    private String funFact;
    private String buyer;
    private String description;

    // Constructor with Date conversion
    public CatDTO(Integer id, String name, Date birthdate, String breedName, String funFact, String buyer, String description) {
        this.id = id;
        this.name = name;
        this.birthdate = formatDate(birthdate);  // Format date to String
        this.breedName = breedName;
        this.funFact = funFact;
        this.buyer = buyer;
        this.description = description;
    }

    // Format Date to String (e.g., "yyyy-MM-dd")
    private String formatDate(Date birthdate) {
        if (birthdate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(birthdate);
        }
        return null;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
