package com.example.model;

import java.time.LocalDate;

public class RevenueByTime {
    private LocalDate date;
    private double revenue;

    public RevenueByTime(LocalDate date, double revenue) {
        this.date = date;
        this.revenue = revenue;
    }

    public LocalDate getDate() { return date; }
    public double getRevenue() { return revenue; }
}
