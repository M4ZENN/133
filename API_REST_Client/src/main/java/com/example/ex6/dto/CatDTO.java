package com.example.ex6.dto;

public class CatDTO {
    private Integer id;
    private String name;
    private String birthdate;
    private String breedName;
    private String funFact;

    public CatDTO(Integer id, String name, String birthdate, String breedName, String funFact, String buyer) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.breedName = breedName;
        this.funFact = funFact;
    }

    // Getters and setters
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

    
}
