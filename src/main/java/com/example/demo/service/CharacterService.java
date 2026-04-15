package com.example.demo.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Character;
import com.example.demo.repository.CharacterRepository;

@Service
public class CharacterService {

    private final CharacterRepository repository;

    @Autowired
    public CharacterService(final CharacterRepository repository) {
        this.repository = repository;
    }

    public Page<Character> getCharacters(final int pageNumber, final int size) {
        return repository.findAll(PageRequest.of(pageNumber, size));
    }

    public Optional<Character> getCharacter(final UUID id) {
        return repository.findById(id);
    }

    public Character saveCharacter(final Character character) {
        return repository.save(character);
    }

    public void deleteCharacter(final UUID id) {
        repository.deleteById(id);
    }
}


