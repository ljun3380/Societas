package com.example.demo.entity;

public class Image {
    private String name;
    private String description;

    // Constructor
    public Image(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters (Thymeleaf needs these to "see" the data!)
    public String getName() { return name; }
    public String getDescription() { return description; }
}