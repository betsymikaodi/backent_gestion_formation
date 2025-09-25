package com.example.G_apprenant.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.G_apprenant.service.impl.ApprenantExportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/apprenants")
@Tag(name = "Apprenant Export", description = "Exportation CSV/XLSX des apprenants")
public class ApprenantExportController {
    private final ApprenantExportService exportService;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public ApprenantExportController(ApprenantExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/export/csv/all")
    @Operation(summary = "Exporter tous les apprenants en CSV")
    public ResponseEntity<byte[]> exportAllToCsv() {
        try {
            byte[] csvData = exportService.exportAllToCsv();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", generateFilename("apprenants_complet", "csv"));
            headers.setContentLength(csvData.length);
            
            return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/csv/page")
    @Operation(summary = "Exporter une page d'apprenants en CSV")
    public ResponseEntity<byte[]> exportPageToCsv(
            @Parameter(description = "Numéro de page (commence à 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Champ de tri") @RequestParam(defaultValue = "idApprenant") String sortBy,
            @Parameter(description = "Direction du tri") @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            byte[] csvData = exportService.exportPageToCsv(pageable);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", generateFilename("apprenants_page_" + (page + 1), "csv"));
            headers.setContentLength(csvData.length);
            
            return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/excel/all")
    @Operation(summary = "Exporter tous les apprenants en Excel")
    public ResponseEntity<byte[]> exportAllToExcel() {
        try {
            byte[] excelData = exportService.exportAllToExcel();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", generateFilename("apprenants_complet", "xlsx"));
            headers.setContentLength(excelData.length);
            
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/excel/page")
    @Operation(summary = "Exporter une page d'apprenants en Excel")
    public ResponseEntity<byte[]> exportPageToExcel(
            @Parameter(description = "Numéro de page (commence à 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Champ de tri") @RequestParam(defaultValue = "idApprenant") String sortBy,
            @Parameter(description = "Direction du tri") @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            byte[] excelData = exportService.exportPageToExcel(pageable);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", generateFilename("apprenants_page_" + (page + 1), "xlsx"));
            headers.setContentLength(excelData.length);
            
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Génère un nom de fichier avec horodatage
     */
    private String generateFilename(String prefix, String extension) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        return String.format("%s_%s.%s", prefix, timestamp, extension);
    }
}
