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

import com.example.demo.entity.Satellite;
import com.example.demo.service.SatelliteService;

@Controller
@RequestMapping("/satellites")
public class SatelliteController {
    static final int DEFAULT_PAGE_SIZE = 2;

    private final SatelliteService satelliteService;

    @Autowired
    public SatelliteController(final SatelliteService satelliteService) {
        this.satelliteService = satelliteService;
    }

    @GetMapping(value={"/", ""})
    public String index() {
        return "redirect:/satellites/list";
    }

    @GetMapping(value={"/list", "/list/"})
    public String list(final Model model, @RequestParam(value = "page", defaultValue = "0") final int pageNumber, 
            @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE + "") final int pageSize) {
        final Page<Satellite> page = satelliteService.getSatellites(pageNumber, pageSize);
        
        final int currentPageNumber = page.getNumber();
        final int previousPageNumber = page.hasPrevious() ? currentPageNumber - 1 : -1;
        final int nextPageNumber = page.hasNext() ? currentPageNumber + 1 : -1;

        model.addAttribute("satellites", page.getContent());
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
            Optional<Satellite> record = satelliteService.getSatellite(uuid);

            model.addAttribute("satellite", record.orElse(new Satellite()));
            model.addAttribute("requestedId", id);
        } catch (IllegalArgumentException e) {
            // This handles cases where the ID in the URL isn't a valid UUID
            model.addAttribute("satellites", new Satellite());
            model.addAttribute("error", "Invalid ID format");
        }
        return "view";

    }

    @GetMapping(value={"/add", "/add/"})
    public String add(final Model model) {
        model.addAttribute("satellites", new Satellite());
        return "add";
    }

    @GetMapping(value={"/edit", "/edit/"})
    public String edit(final Model model, @RequestParam final UUID id) {
        final Optional<Satellite> record = satelliteService.getSatellite(id);

        model.addAttribute("satellite", record.isPresent() ? record.get() : new Satellite());
        model.addAttribute("id", id);

        return "edit";
    }

    @PostMapping(value={"/save", "/save/"})
    public String save(final Model model, @ModelAttribute final Satellite Satellite, final BindingResult errors) {
        // Save the trading card entity to the database
        satelliteService.saveSatellite(Satellite);
        return "redirect:list";
    }

    @GetMapping(value={"/delete", "/delete/"})
    public String delete(final Model model, @RequestParam final UUID id) {
        final Optional<Satellite> record = satelliteService.getSatellite(id);

        model.addAttribute("satellite", record.isPresent() ? record.get() : new Satellite());
        model.addAttribute("id", id);

        return "delete";
    }

    @PostMapping(value={"/delete", "/delete/"})
    public String deletion(final Model model, @RequestParam final UUID id) {
        satelliteService.deleteSatellite(id);
        return "redirect:list";
    }

}




