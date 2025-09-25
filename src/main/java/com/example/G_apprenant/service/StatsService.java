package com.example.G_apprenant.service;

import java.time.LocalDate;
import com.example.G_apprenant.dto.StatsOverviewResponse;

public interface StatsService {
    /**
     * Calcule les KPI pour une période.
     * @param period Type de période: day|week|month|custom (par défaut: month)
     * @param startDate utilisé si period=custom
     * @param endDate utilisé si period=custom
     */
    StatsOverviewResponse getOverview(String period, LocalDate startDate, LocalDate endDate);
}
