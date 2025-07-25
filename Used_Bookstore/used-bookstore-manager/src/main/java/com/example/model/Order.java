package com.example.model;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private int id; // 💡 Nếu cần lấy id đơn hàng từ DB
    private String customerName;
    private String phone;
    private String email;
    private String address;
    private LocalDate orderDate;
    private List<OrderItem> items;
    private int createdByUserId; // 💡 Lưu người tạo đơn

    public Order(String customerName, String phone, String email, String address, List<OrderItem> items) {
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.orderDate = LocalDate.now();
        this.items = items;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public LocalDate getOrderDate() { return orderDate; }
    public List<OrderItem> getItems() { return items; }

    public int getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(int userId) { this.createdByUserId = userId; }

    public double getTotal() {
        return items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }
}
