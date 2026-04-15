package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Image;

import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
        // Mocking some data for your gallery
        List<Image> images = Arrays.asList(
            new Image("Sputnik 1.jpg", "Sputnik 1: First artificial Earth character"),
            new Image("Sputnik 2.webp", "Sputnik 2: First dog in space"),
            new Image("Vanshield.webp", "Vanshield: US Fail"),
            new Image("Explorer 1.jpg", "Explorer 1: First American character" )
        );

        // This "imageList" string MUST match the th:each in your HTML
        model.addAttribute("imageList", images);

        return "home";
    }
}