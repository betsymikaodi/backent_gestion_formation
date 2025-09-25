package com.example.G_apprenant.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.G_apprenant.dto.KPIItem;
import com.example.G_apprenant.dto.StatsOverviewResponse;
import com.example.G_apprenant.repository.InscriptionRepository;
import com.example.G_apprenant.repository.PaiementRepository;
import com.example.G_apprenant.service.StatsService;

@Service
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final PaiementRepository paiementRepo;
    private final InscriptionRepository inscriptionRepo;

    public StatsServiceImpl(PaiementRepository paiementRepo, InscriptionRepository inscriptionRepo) {
        this.paiementRepo = paiementRepo;
        this.inscriptionRepo = inscriptionRepo;
    }

    @Override
    public StatsOverviewResponse getOverview(String period, LocalDate startDate, LocalDate endDate) {
        // Déterminer les bornes de période courante et précédente
        PeriodRange current = resolveRange(period, startDate, endDate);
        PeriodRange previous = previousRange(current);

        // 1) Revenus totaux
        BigDecimal revenueCurrent = paiementRepo.sumMontantBetween(current.start, current.end);
        BigDecimal revenuePrevious = paiementRepo.sumMontantBetween(previous.start, previous.end);
        double revenuePct = percentChange(revenueCurrent, revenuePrevious);
        KPIItem kpiRevenue = new KPIItem(
            "Revenus totaux",
            revenueCurrent.setScale(2, RoundingMode.HALF_UP),
            revenuePrevious.setScale(2, RoundingMode.HALF_UP),
            revenuePct,
            current.start, current.end,
            previous.start, previous.end,
            "€"
        );

        // 2) Total des inscriptions
        long insCurrent = inscriptionRepo.countByDateInscriptionBetween(current.start, current.end);
        long insPrevious = inscriptionRepo.countByDateInscriptionBetween(previous.start, previous.end);
        double insPct = percentChange(new BigDecimal(insCurrent), new BigDecimal(insPrevious));
        KPIItem kpiIns = new KPIItem(
            "Total inscriptions",
            new BigDecimal(insCurrent),
            new BigDecimal(insPrevious),
            insPct,
            current.start, current.end,
            previous.start, previous.end,
            "inscrits"
        );

        // 3) Revenu moyen par étudiant (définition: revenus / nb inscriptions de la période)
        BigDecimal avgCurrent = insCurrent == 0 ? BigDecimal.ZERO : revenueCurrent.divide(new BigDecimal(insCurrent), 2, RoundingMode.HALF_UP);
        BigDecimal avgPrevious = insPrevious == 0 ? BigDecimal.ZERO : revenuePrevious.divide(new BigDecimal(insPrevious), 2, RoundingMode.HALF_UP);
        double avgPct = percentChange(avgCurrent, avgPrevious);
        KPIItem kpiAvg = new KPIItem(
            "Revenu moyen par étudiant",
            avgCurrent,
            avgPrevious,
            avgPct,
            current.start, current.end,
            previous.start, previous.end,
            "€/étudiant"
        );

        return new StatsOverviewResponse(kpiRevenue, kpiIns, kpiAvg);
    }

    private double percentChange(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            if (current == null || current.compareTo(BigDecimal.ZERO) == 0) return 0.0;
            return 100.0; // tout progrès depuis 0 => +100% conventionnellement
        }
        BigDecimal diff = current.subtract(previous);
        BigDecimal pct = diff.divide(previous, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        return pct.doubleValue();
    }

    private PeriodRange resolveRange(String period, LocalDate start, LocalDate end) {
        LocalDate today = LocalDate.now();
        if (period == null || period.isBlank()) period = "month";
        switch (period.toLowerCase()) {
            case "day": {
                LocalDate s = today;
                LocalDate e = today;
                return new PeriodRange(s, e);
            }
            case "week": {
                // Semaine en cours (lundi à dimanche)
                LocalDate s = today.minusDays((today.getDayOfWeek().getValue() + 6) % 7);
                LocalDate e = s.plusDays(6);
                return new PeriodRange(s, e);
            }
            case "custom": {
                LocalDate s = (start != null) ? start : today;
                LocalDate e = (end != null) ? end : today;
                if (e.isBefore(s)) { LocalDate tmp = s; s = e; e = tmp; }
                return new PeriodRange(s, e);
            }
            case "month":
            default: {
                LocalDate s = today.withDayOfMonth(1);
                LocalDate e = s.plusMonths(1).minusDays(1);
                return new PeriodRange(s, e);
            }
        }
    }

    private PeriodRange previousRange(PeriodRange current) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(current.start, current.end) + 1;
        LocalDate prevEnd = current.start.minusDays(1);
        LocalDate prevStart = prevEnd.minusDays(days - 1);
        return new PeriodRange(prevStart, prevEnd);
    }

    private static class PeriodRange {
        final LocalDate start;
        final LocalDate end;
        PeriodRange(LocalDate s, LocalDate e) { this.start = s; this.end = e; }
    }
}
