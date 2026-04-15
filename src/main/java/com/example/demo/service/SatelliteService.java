package com.example.demo.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Satellite;
import com.example.demo.repository.SatelliteRepository;

@Service
public class SatelliteService {

    private final SatelliteRepository repository;

    @Autowired
    public SatelliteService(final SatelliteRepository repository) {
        this.repository = repository;
    }

    public Page<Satellite> getSatellites(final int pageNumber, final int size) {
        return repository.findAll(PageRequest.of(pageNumber, size));
    }

    public Optional<Satellite> getSatellite(final UUID id) {
        return repository.findById(id);
    }

    public Satellite saveSatellite(final Satellite satellite) {
        return repository.save(satellite);
    }

    public void deleteSatellite(final UUID id) {
        repository.deleteById(id);
    }
}


