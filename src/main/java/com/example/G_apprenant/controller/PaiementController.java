package com.example.G_apprenant.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.G_apprenant.dto.PaiementRequest;
import com.example.G_apprenant.entity.Paiement;
import com.example.G_apprenant.service.PaiementService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/paiements")
public class PaiementController {
    private final PaiementService service;

    public PaiementController(PaiementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Paiement> create(@Valid @RequestBody PaiementRequest req) {
        return ResponseEntity.ok(service.create(req.getInscriptionId(), req.getMontant(), req.getModePaiement(), req.getModule()));
    }

    @GetMapping("/inscription/{id}")
    public ResponseEntity<List<Paiement>> getByInscription(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByInscription(id));
    }
}
