package com.example.demo.controller;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Character;
import com.example.demo.service.CharacterService;

@Controller
public class HomeController {
    
    static final int DEFAULT_PAGE_SIZE = 10;

    private CharacterService characterService = null;

    public HomeController(final CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/characterlab")
    public String characterlab(Model model) {
        model.addAttribute("character", new Character());
        return "characterlab";
    }

    @PostMapping(value={"/charactersave", "/charactersave/"})
    public String charactersave(final Model model, @ModelAttribute final Character character, final BindingResult errors, @RequestParam(value = "image", required = false) final MultipartFile imageFile) {
        Character toSave = character;
        if (character.getId() != null) {
            // Editing existing character, load it to preserve fields not in form
            Optional<Character> existing = characterService.getCharacter(character.getId());
            if (existing.isPresent()) {
                toSave = existing.get();
                // Update fields from form
                toSave.setName(character.getName());
                toSave.setEmail(character.getEmail());
                toSave.setDescription(character.getDescription());
            }
        }
        // Apply defaults for new characters
        if (character.getAuthor() == null || character.getAuthor().isBlank()) {
            toSave.setAuthor("Anonymous");
        }
        toSave.setVotes(0);
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                toSave.setImage(imageFile.getBytes());
            }
        } catch (Exception e) {
            // Handle exception, perhaps add error to model
        }
        // Save the trading card entity to the database
        characterService.saveCharacter(toSave);
        return "redirect:explorer";
    }

    @GetMapping("/explorer")
    public String explore(final Model model, @RequestParam(value = "page", defaultValue = "0") final int pageNumber, 
            @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE + "") final int pageSize) {
        final Page<Character> page = characterService.getCharacters(pageNumber, pageSize);
        
        final int currentPageNumber = page.getNumber();
        final int previousPageNumber = page.hasPrevious() ? currentPageNumber - 1 : -1;
        final int nextPageNumber = page.hasNext() ? currentPageNumber + 1 : -1;

        model.addAttribute("characters", page.getContent());
        model.addAttribute("previousPageNumber", previousPageNumber);
        model.addAttribute("nextPageNumber", nextPageNumber);
        model.addAttribute("currentPageNumber", currentPageNumber);
        model.addAttribute("pageSize", pageSize);
        return "explorer";
    }
}