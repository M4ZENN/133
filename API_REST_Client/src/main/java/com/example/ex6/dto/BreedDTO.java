package com.example.ex6.dto;

public class BreedDTO {
    private Integer id;
    private String name;
    private String description;

    // Constructor that directly accepts Date and converts it
    public BreedDTO(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Default constructor (useful for frameworks like Spring)
    public BreedDTO() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
