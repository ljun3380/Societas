package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeControllerJS {
    @GetMapping("/homejs")
    public String home(Model model) {
        return "homejs";
    }
    
}
