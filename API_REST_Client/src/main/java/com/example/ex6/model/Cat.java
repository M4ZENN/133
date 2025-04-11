package com.example.ex6.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "t_cat")
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_cat")
    private Integer id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "birthdate")
    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @Column(name = "fk_buyer")
    private Integer buyerId; // Instead of referencing the Buyer entity, store the buyer's user ID

    @ManyToOne
    @JoinColumn(name = "fk_breed", referencedColumnName = "pk_breed")
    private Breed breed;

    @Column(name = "funFact", length = 255)
    private String funFact;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private byte[] image; // Or use byte[] if storing the actual image data

    @Column(name = "isPurchased")
    private Boolean isPurchased; // Flag to track if the cat is purchased

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

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setBuyerFk(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getBuyer() {
        return buyerId;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Boolean getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(Boolean isPurchased) {
        this.isPurchased = isPurchased;
    }
}
