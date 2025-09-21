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

    @GetMapping
    public ResponseEntity<java.util.List<Paiement>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paiement> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }



    @GetMapping("/inscription/{id}")
    public ResponseEntity<List<Paiement>> getByInscription(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByInscription(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Paiement> update(@PathVariable Long id, @RequestBody com.example.G_apprenant.dto.PaiementUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req.getMontant(), req.getModePaiement(), req.getModule()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
