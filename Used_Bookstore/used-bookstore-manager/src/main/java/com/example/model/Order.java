// Order.java
package com.example.model;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private String customerName;
    private String phone;
    private String email;
    private String address;
    private LocalDate orderDate;
    private List<OrderItem> items;

    public Order(String customerName, String phone, String email, String address, List<OrderItem> items) {
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.orderDate = LocalDate.now();
        this.items = items;
    }

    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public LocalDate getOrderDate() { return orderDate; }
    public List<OrderItem> getItems() { return items; }

    public double getTotal() {
        return items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }
}
