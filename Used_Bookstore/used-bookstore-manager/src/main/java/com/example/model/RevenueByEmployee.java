package com.example.model;

public class RevenueByEmployee {
    private String employeeName;
    private double revenue;
    private int invoiceCount;

    public RevenueByEmployee(String employeeName, double revenue, int invoiceCount) {
        this.employeeName = employeeName;
        this.revenue = revenue;
        this.invoiceCount = invoiceCount;
    }

    public String getEmployeeName() { return employeeName; }
    public double getRevenue() { return revenue; }
    public int getInvoiceCount() { return invoiceCount; }
}
