package com.example.G_apprenant.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.G_apprenant.dto.StatsOverviewResponse;
import com.example.G_apprenant.service.StatsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/stats")
@Tag(name = "Statistiques", description = "KPIs finances et inscriptions")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:8081"})
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping(value = "/overview", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "KPIs principaux: revenus, inscriptions, revenu moyen/étudiant",
               description = "Paramètre period=day|week|month|custom (défaut: month). Si custom, fournir startDate et endDate (yyyy-MM-dd)")
    public ResponseEntity<StatsOverviewResponse> getOverview(
        @Parameter(description = "Type de période: day|week|month|custom")
        @RequestParam(name = "period", required = false, defaultValue = "month") String period,

        @Parameter(description = "Date de début si period=custom (yyyy-MM-dd)")
        @RequestParam(name = "startDate", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

        @Parameter(description = "Date de fin si period=custom (yyyy-MM-dd)")
        @RequestParam(name = "endDate", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        StatsOverviewResponse resp = statsService.getOverview(period, startDate, endDate);
        return ResponseEntity.ok(resp);
    }
}
