package com.example.demo.entity;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] image;

    @Column(name = "votes")
    private Integer votes;

    @Column(name = "email")
    private String email;

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String generateBase64Image() {
        return java.util.Base64.getEncoder().encodeToString(this.image);
    }
}

