package com.example.model;

public class BestSellingBook {
    private String title;
    private String author;
    private int quantitySold;

    public BestSellingBook(String title, String author, int quantitySold) {
        this.title = title;
        this.author = author;
        this.quantitySold = quantitySold;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getQuantitySold() { return quantitySold; }
}
