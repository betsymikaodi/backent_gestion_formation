package com.example.G_apprenant.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.G_apprenant.entity.Formation;
import com.example.G_apprenant.service.FormationService;
import com.example.G_apprenant.dto.FormationRequest;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/formations")
@Tag(name = "Formation Management", description = "APIs pour la gestion des formations")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:8081"})
public class FormationController {
    private final FormationService service;

    public FormationController(FormationService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Créer une nouvelle formation")
    public ResponseEntity<Formation> create(@Valid @RequestBody FormationRequest request) {
        Formation formation = new Formation();
        formation.setNom(request.getNom());
        formation.setDescription(request.getDescription());
        formation.setDuree(request.getDuree());
        formation.setFrais(request.getFrais());
        
        Formation savedFormation = service.save(formation);
        return new ResponseEntity<>(savedFormation, HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir toutes les formations")
    public ResponseEntity<List<Formation>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtenir une formation par ID")
    public ResponseEntity<Formation> getById(
            @Parameter(description = "ID de la formation")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Mettre à jour une formation")
    public ResponseEntity<Formation> update(
            @Parameter(description = "ID de la formation")
            @PathVariable Long id, 
            @Valid @RequestBody FormationRequest request) {
        
        // Créer un objet Formation à partir du FormationRequest
        Formation formation = new Formation();
        formation.setNom(request.getNom());
        formation.setDescription(request.getDescription());
        formation.setDuree(request.getDuree());
        formation.setFrais(request.getFrais());
        
        return ResponseEntity.ok(service.update(id, formation));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une formation")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID de la formation")
            @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
