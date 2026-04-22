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
        return "home";
    }

    @GetMapping("/characterlab")
    public String characterlab(Model model) {
        return "characterlab";
    }

    @GetMapping("/explore")
    public String explore(Model model) {
        return "explore";
    }
}