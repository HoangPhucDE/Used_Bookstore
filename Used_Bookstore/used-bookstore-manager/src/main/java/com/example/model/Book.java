package com.example.model;

import javafx.beans.property.*;

public class Book {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty category;
    private final DoubleProperty price;
    private final IntegerProperty stock;
    private final DoubleProperty rating;

    public Book(int id, String title, String author, String category, double price, int stock, double rating) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.category = new SimpleStringProperty(category);
        this.price = new SimpleDoubleProperty(price);
        this.stock = new SimpleIntegerProperty(stock);
        this.rating = new SimpleDoubleProperty(rating);
    }

    // Getters
    public int getId() { return id.get(); }
    public String getTitle() { return title.get(); }
    public String getAuthor() { return author.get(); }
    public String getCategory() { return category.get(); }
    public double getPrice() { return price.get(); }
    public int getStock() { return stock.get(); }
    public double getRating() { return rating.get(); }

    // Property getters for TableView binding
    public IntegerProperty idProperty() { return id; }
    public StringProperty titleProperty() { return title; }
    public StringProperty authorProperty() { return author; }
    public StringProperty categoryProperty() { return category; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty stockProperty() { return stock; }
    public DoubleProperty ratingProperty() { return rating; }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setTitle(String title) { this.title.set(title); }
    public void setAuthor(String author) { this.author.set(author); }
    public void setCategory(String category) { this.category.set(category); }
    public void setPrice(double price) { this.price.set(price); }
    public void setStock(int stock) { this.stock.set(stock); }
    public void setRating(double rating) { this.rating.set(rating); }
}