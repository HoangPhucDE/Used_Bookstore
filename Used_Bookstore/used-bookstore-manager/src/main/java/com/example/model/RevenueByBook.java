package com.example.model;

public class RevenueByBook {
    private String bookName;
    private int quantity;

    public RevenueByBook(String bookName, int quantity) {
        this.bookName = bookName;
        this.quantity = quantity;
    }

    public String getBookName() { return bookName; }
    public int getQuantity() { return quantity; }
}
