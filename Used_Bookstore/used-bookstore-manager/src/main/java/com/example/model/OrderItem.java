// OrderItem.java
package com.example.model;

public class OrderItem {
    private String bookTitle;
    private int quantity;
    private double unitPrice;

    public OrderItem(String bookTitle, int quantity, double unitPrice) {
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getBookTitle() { return bookTitle; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotalPrice() { return quantity * unitPrice; }
}
