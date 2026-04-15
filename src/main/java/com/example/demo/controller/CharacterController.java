package com.example.demo.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Character;
import com.example.demo.service.CharacterService;

@Controller
@RequestMapping("/characters")
public class CharacterController {
    static final int DEFAULT_PAGE_SIZE = 2;

    private final CharacterService characterService;

    @Autowired
    public CharacterController(final CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping(value={"/", ""})
    public String index() {
        return "redirect:/characters/list";
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
    public String view(final Model model, @RequestParam(name = "id") final String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<Character> record = characterService.getCharacter(uuid);

            model.addAttribute("character", record.orElse(new Character()));
            model.addAttribute("requestedId", id);
        } catch (IllegalArgumentException e) {
            // This handles cases where the ID in the URL isn't a valid UUID
            model.addAttribute("characters", new Character());
            model.addAttribute("error", "Invalid ID format");
        }
        return "view";

    }

    @GetMapping(value={"/add", "/add/"})
    public String add(final Model model) {
        model.addAttribute("characters", new Character());
        return "add";
    }

    @GetMapping(value={"/edit", "/edit/"})
    public String edit(final Model model, @RequestParam final UUID id) {
        final Optional<Character> record = characterService.getCharacter(id);

        model.addAttribute("character", record.isPresent() ? record.get() : new Character());
        model.addAttribute("id", id);

        return "edit";
    }

    @PostMapping(value={"/save", "/save/"})
    public String save(final Model model, @ModelAttribute final Character character, final BindingResult errors) {
        // Save the trading card entity to the database
        characterService.saveCharacter(character);
        return "redirect:list";
    }

    @GetMapping(value={"/delete", "/delete/"})
    public String delete(final Model model, @RequestParam final UUID id) {
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




