package com.example.G_apprenant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.G_apprenant.dto.ImportResult;
import com.example.G_apprenant.service.ApprenantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/apprenants")
@Tag(name = "Apprenant Import", description = "Import CSV/XLSX pour les apprenants")
public class ApprenantImportController {
    private final ApprenantService service;

    public ApprenantImportController(ApprenantService service) {
        this.service = service;
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Importer des apprenants via CSV ou XLSX")
    public ResponseEntity<ImportResult> importApprenants(@RequestPart("file") MultipartFile file) {
        try {
            ImportResult result = service.importApprenants(file.getInputStream(), file.getOriginalFilename());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ImportResult err = new ImportResult();
            err.addError(0, "Erreur import fichier");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }
}
