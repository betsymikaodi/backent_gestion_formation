package com.example.G_apprenant.controller;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.G_apprenant.dto.InscriptionRequest;
import com.example.G_apprenant.entity.Inscription;
import com.example.G_apprenant.service.InscriptionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/inscriptions")
public class InscriptionController {
    private final InscriptionService service;

    public InscriptionController(InscriptionService service) {
        this.service = service;
    }

    @PostMapping("/enroll")
    public ResponseEntity<Inscription> enroll(@Valid @RequestBody InscriptionRequest req) {
        return ResponseEntity.ok(service.enroll(req.getApprenantId(), req.getFormationId(), req.getDroitInscription()));
    }

    @GetMapping
    public ResponseEntity<List<Inscription>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inscription> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Inscription> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirm(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Inscription> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancel(id));
    }
}
