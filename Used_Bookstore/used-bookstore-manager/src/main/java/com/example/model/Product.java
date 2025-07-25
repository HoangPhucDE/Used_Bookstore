package com.example.model;

public class Product {
    private String name;
    private double price;
    private String imageUrl;

    public Product(String name, double price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return name + " - " + price + "đ";
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
