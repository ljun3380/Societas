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
public class CharacterController {
    static final int DEFAULT_PAGE_SIZE = 10;

    private final CharacterService characterService;

    public CharacterController(final CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping(value={"/", ""})
    public String index() {
        //return "redirect:list";
        return "redirect:home";
    }

    @GetMapping(value={"/list", "/list/"})
    public String list(final Model model, @RequestParam(value = "page", defaultValue = "0") final int pageNumber, 
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

        return "list";
    }

    @GetMapping("/view")
    public String view(final Model model, @RequestParam(name = "id", required = false) final String id) {
        if (id == null || id.trim().isEmpty()) {
            return "redirect:list";
        }
        try {
            UUID uuid = UUID.fromString(id);
            Optional<Character> record = characterService.getCharacter(uuid);

            Character character = record.orElse(new Character());
            if (character.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(character.getImage());
                model.addAttribute("base64Image", base64Image);
            }
            model.addAttribute("character", character);
            model.addAttribute("requestedId", id);
        } catch (IllegalArgumentException e) {
            // This handles cases where the ID in the URL isn't a valid UUID
            model.addAttribute("character", new Character());
            model.addAttribute("error", "Invalid ID format");
        }
        return "view";

    }

    @GetMapping(value={"/add", "/add/"})
    public String add(final Model model) {
        model.addAttribute("character", new Character());
        return "add";
    }

    @GetMapping(value={"/edit", "/edit/"})
    public String edit(final Model model, @RequestParam(required = false) final UUID id) {
        if (id == null) {
            return "redirect:list";
        }
        final Optional<Character> record = characterService.getCharacter(id);

        model.addAttribute("character", record.isPresent() ? record.get() : new Character());
        model.addAttribute("id", id);

        return "edit";
    }

    @PostMapping(value={"/save", "/save/"})
    public String save(final Model model, @ModelAttribute final Character character, final BindingResult errors, @RequestParam(value = "image", required = false) final MultipartFile imageFile) {
        Character toSave = character;
        if (character.getId() != null) {
            // Editing existing character, load it to preserve fields not in form
            Optional<Character> existing = characterService.getCharacter(character.getId());
            if (existing.isPresent()) {
                toSave = existing.get();
                // Update fields from form
                toSave.setName(character.getName());
                toSave.setAuthor(character.getAuthor());
                toSave.setEmail(character.getEmail());
                toSave.setDescription(character.getDescription());
                toSave.setVotes(character.getVotes());
            }
        }
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                toSave.setImage(imageFile.getBytes());
            }
        } catch (Exception e) {
            // Handle exception, perhaps add error to model
        }
        // Save the trading card entity to the database
        characterService.saveCharacter(toSave);
        return "redirect:list";
    }

    @GetMapping(value={"/delete", "/delete/"})
    public String delete(final Model model, @RequestParam(required = false) final UUID id) {
        if (id == null) {
            return "redirect:list";
        }
        final Optional<Character> record = characterService.getCharacter(id);

        model.addAttribute("character", record.isPresent() ? record.get() : new Character());
        model.addAttribute("id", id);

        return "delete";
    }

    @PostMapping(value={"/delete", "/delete/"})
    public String deletion(final Model model, @RequestParam final UUID id) {
        characterService.deleteCharacter(id);
        return "redirect:list";
    }

}




