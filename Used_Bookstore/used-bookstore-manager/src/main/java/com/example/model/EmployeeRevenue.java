package com.example.model;

public class EmployeeRevenue {
    private String employeeName;
    private int invoiceCount;
    private double totalRevenue;

    public EmployeeRevenue(String employeeName, int invoiceCount, double totalRevenue) {
        this.employeeName = employeeName;
        this.invoiceCount = invoiceCount;
        this.totalRevenue = totalRevenue;
    }

    public String getEmployeeName() { return employeeName; }
    public int getInvoiceCount() { return invoiceCount; }
    public double getTotalRevenue() { return totalRevenue; }
}
