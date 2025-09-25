package com.example.G_apprenant.dto;

public class StatsOverviewResponse {
    private KPIItem totalRevenue;
    private KPIItem totalEnrollments;
    private KPIItem avgRevenuePerStudent;

    public StatsOverviewResponse() {}

    public StatsOverviewResponse(KPIItem totalRevenue, KPIItem totalEnrollments, KPIItem avgRevenuePerStudent) {
        this.totalRevenue = totalRevenue;
        this.totalEnrollments = totalEnrollments;
        this.avgRevenuePerStudent = avgRevenuePerStudent;
    }

    public KPIItem getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(KPIItem totalRevenue) { this.totalRevenue = totalRevenue; }

    public KPIItem getTotalEnrollments() { return totalEnrollments; }
    public void setTotalEnrollments(KPIItem totalEnrollments) { this.totalEnrollments = totalEnrollments; }

    public KPIItem getAvgRevenuePerStudent() { return avgRevenuePerStudent; }
    public void setAvgRevenuePerStudent(KPIItem avgRevenuePerStudent) { this.avgRevenuePerStudent = avgRevenuePerStudent; }
}
