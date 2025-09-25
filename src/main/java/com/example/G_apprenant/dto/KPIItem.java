package com.example.G_apprenant.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class KPIItem {
    private String label;
    private BigDecimal value;            // valeur sur la période courante
    private BigDecimal previousValue;    // valeur sur la période précédente
    private Double percentageChange;     // (value - previous)/previous * 100
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDate previousPeriodStart;
    private LocalDate previousPeriodEnd;
    private String unit; // €, inscrits, €/étudiant

    public KPIItem() {}

    public KPIItem(String label, BigDecimal value, BigDecimal previousValue, Double percentageChange,
                   LocalDate periodStart, LocalDate periodEnd,
                   LocalDate previousPeriodStart, LocalDate previousPeriodEnd,
                   String unit) {
        this.label = label;
        this.value = value;
        this.previousValue = previousValue;
        this.percentageChange = percentageChange;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.previousPeriodStart = previousPeriodStart;
        this.previousPeriodEnd = previousPeriodEnd;
        this.unit = unit;
    }

    // Getters & setters
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
    public BigDecimal getPreviousValue() { return previousValue; }
    public void setPreviousValue(BigDecimal previousValue) { this.previousValue = previousValue; }
    public Double getPercentageChange() { return percentageChange; }
    public void setPercentageChange(Double percentageChange) { this.percentageChange = percentageChange; }
    public LocalDate getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }
    public LocalDate getPreviousPeriodStart() { return previousPeriodStart; }
    public void setPreviousPeriodStart(LocalDate previousPeriodStart) { this.previousPeriodStart = previousPeriodStart; }
    public LocalDate getPreviousPeriodEnd() { return previousPeriodEnd; }
    public void setPreviousPeriodEnd(LocalDate previousPeriodEnd) { this.previousPeriodEnd = previousPeriodEnd; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
