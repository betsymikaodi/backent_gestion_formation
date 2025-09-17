package com.example.G_apprenant.controller;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.G_apprenant.entity.Formation;
import com.example.G_apprenant.service.FormationService;
import com.example.G_apprenant.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/formations")
@Tag(name = "Formation Management", description = "APIs pour la gestion des formations")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:8081"})
public class FormationController {

    @Autowired
    private FormationService formationService;

    @GetMapping
    @Operation(summary = "Obtenir toutes les formations", description = "Récupère la liste de toutes les formations")
    public ResponseEntity<List<Formation>> getAllFormations() {
        List<Formation> formations = formationService.findAll();
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Obtenir les formations avec pagination")
    public ResponseEntity<Page<Formation>> getAllFormationsPaginated(
            @Parameter(description = "Numéro de la page (commence à 0)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Champ de tri")
            @RequestParam(defaultValue = "nom") String sortBy,
            @Parameter(description = "Direction du tri (asc ou desc)")
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Formation> formations = formationService.findAll(pageable);
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une formation par ID")
    public ResponseEntity<Formation> getFormationById(
            @Parameter(description = "ID de la formation")
            @PathVariable Long id) {
        Formation formation = formationService.findById(id);
        return ResponseEntity.ok(formation);
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle formation")
    public ResponseEntity<Formation> createFormation(
            @Valid @RequestBody Formation formation) {
        Formation savedFormation = formationService.save(formation);
        return new ResponseEntity<>(savedFormation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une formation")
    public ResponseEntity<Formation> updateFormation(
            @Parameter(description = "ID de la formation")
            @PathVariable Long id,
            @Valid @RequestBody Formation formationDetails) {
        Formation updatedFormation = formationService.update(id, formationDetails);
        return ResponseEntity.ok(updatedFormation);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une formation")
    public ResponseEntity<Void> deleteFormation(
            @Parameter(description = "ID de la formation")
            @PathVariable Long id) {
        formationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Recherches spécifiques

    @GetMapping("/search/nom")
    @Operation(summary = "Rechercher formations par nom")
    public ResponseEntity<List<Formation>> searchByNom(
            @Parameter(description = "Nom à rechercher")
            @RequestParam String nom) {
        List<Formation> formations = formationService.findByNomContaining(nom);
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/search/prix")
    @Operation(summary = "Rechercher formations par fourchette de prix")
    public ResponseEntity<List<Formation>> searchByPrixRange(
            @Parameter(description = "Prix minimum")
            @RequestParam(required = false) BigDecimal fraisMin,
            @Parameter(description = "Prix maximum")
            @RequestParam(required = false) BigDecimal fraisMax) {
        
        List<Formation> formations;
        if (fraisMin != null && fraisMax != null) {
            formations = formationService.findByFraisBetween(fraisMin, fraisMax);
        } else if (fraisMin != null) {
            formations = formationService.findByFraisGreaterThanEqual(fraisMin);
        } else if (fraisMax != null) {
            formations = formationService.findByFraisLessThanEqual(fraisMax);
        } else {
            formations = formationService.findAll();
        }
        
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/search/duree")
    @Operation(summary = "Rechercher formations par durée")
    public ResponseEntity<List<Formation>> searchByDureeRange(
            @Parameter(description = "Durée minimum en heures")
            @RequestParam(required = false) Integer dureeMin,
            @Parameter(description = "Durée maximum en heures")
            @RequestParam(required = false) Integer dureeMax) {
        
        List<Formation> formations;
        if (dureeMin != null && dureeMax != null) {
            formations = formationService.findByDureeBetween(dureeMin, dureeMax);
        } else if (dureeMin != null) {
            formations = formationService.findByDureeGreaterThanEqual(dureeMin);
        } else if (dureeMax != null) {
            formations = formationService.findByDureeLessThanEqual(dureeMax);
        } else {
            formations = formationService.findAll();
        }
        
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/populaires")
    @Operation(summary = "Obtenir les formations les plus populaires")
    public ResponseEntity<List<Formation>> getFormationsPopulaires(
            @Parameter(description = "Nombre de formations à retourner")
            @RequestParam(defaultValue = "5") int limit) {
        List<Formation> formations = formationService.findMostPopular(limit);
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/moins-cheres")
    @Operation(summary = "Obtenir les formations les moins chères")
    public ResponseEntity<List<Formation>> getFormationsMoinsCheres(
            @Parameter(description = "Nombre de formations à retourner")
            @RequestParam(defaultValue = "5") int limit) {
        List<Formation> formations = formationService.findCheapest(limit);
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Obtenir les formations avec places disponibles")
    public ResponseEntity<List<Formation>> getFormationsDisponibles(
            @Parameter(description = "Nombre maximum de places par formation")
            @RequestParam(defaultValue = "50") int maxPlaces) {
        List<Formation> formations = formationService.findWithAvailableSpots(maxPlaces);
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/{id}/inscriptions/count")
    @Operation(summary = "Compter les inscriptions confirmées pour une formation")
    public ResponseEntity<Long> countInscriptionsConfirmees(
            @Parameter(description = "ID de la formation")
            @PathVariable Long id) {
        long count = formationService.countConfirmedInscriptions(id);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/moyenne-prix")
    @Operation(summary = "Obtenir le prix moyen des formations")
    public ResponseEntity<BigDecimal> getPrixMoyen() {
        BigDecimal prixMoyen = formationService.getAveragePrice();
        return ResponseEntity.ok(prixMoyen);
    }

    @GetMapping("/stats/moyenne-duree")
    @Operation(summary = "Obtenir la durée moyenne des formations")
    public ResponseEntity<Double> getDureeMoyenne() {
        Double dureeMoyenne = formationService.getAverageDuration();
        return ResponseEntity.ok(dureeMoyenne);
    }

    // Gestion des exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}