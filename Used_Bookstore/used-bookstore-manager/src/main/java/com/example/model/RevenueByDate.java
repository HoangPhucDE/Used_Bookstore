package com.example.model;

import java.time.LocalDate;

public class RevenueByDate {
    private LocalDate date;
    private int invoiceCount;
    private double totalRevenue;

    public RevenueByDate(LocalDate date, int invoiceCount, double totalRevenue) {
        this.date = date;
        this.invoiceCount = invoiceCount;
        this.totalRevenue = totalRevenue;
    }

    public LocalDate getDate() { return date; }
    public int getInvoiceCount() { return invoiceCount; }
    public double getTotalRevenue() { return totalRevenue; }
}
