package com.example.G_apprenant.controller;

import java.util.List;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.G_apprenant.entity.Apprenant;
import com.example.G_apprenant.service.ApprenantService;
import com.example.G_apprenant.dto.ApprenantRequest;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/apprenants")
@Tag(name = "Apprenant Management", description = "APIs pour la gestion des apprenants")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:8081"})
public class ApprenantController {
    private final ApprenantService service;

    public ApprenantController(ApprenantService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Créer un nouvel apprenant")
    public ResponseEntity<Apprenant> create(@Valid @RequestBody ApprenantRequest request) {
        Apprenant apprenant = new Apprenant();
        apprenant.setNom(request.getNom());
        apprenant.setPrenom(request.getPrenom());
        apprenant.setEmail(request.getEmail());
        apprenant.setTelephone(request.getTelephone());
        apprenant.setAdresse(request.getAdresse());
        apprenant.setCin(request.getCin());
        
        if (request.getDateNaissance() != null && !request.getDateNaissance().isEmpty()) {
            apprenant.setDateNaissance(LocalDate.parse(request.getDateNaissance()));
        }
        
        Apprenant savedApprenant = service.create(apprenant);
        return new ResponseEntity<>(savedApprenant, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir tous les apprenants")
    public ResponseEntity<List<Apprenant>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir un apprenant par ID")
    public ResponseEntity<Apprenant> getById(
            @Parameter(description = "ID de l'apprenant")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Mettre à jour un apprenant")
    public ResponseEntity<Apprenant> update(
            @Parameter(description = "ID de l'apprenant")
            @PathVariable Long id, 
            @Valid @RequestBody Apprenant a) {
        return ResponseEntity.ok(service.update(id, a));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un apprenant")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID de l'apprenant")
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
