package com.example.controller;

import com.example.model.Order;
import com.example.model.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class SalesController {

    @FXML private ComboBox<String> bookCombo;
    @FXML private TextField quantityField;
    @FXML private TableView<OrderItem> orderTable;
    @FXML private TableColumn<OrderItem, String> colBookTitle;
    @FXML private TableColumn<OrderItem, Integer> colQuantity;
    @FXML private TableColumn<OrderItem, Double> colUnitPrice;
    @FXML private TableColumn<OrderItem, Double> colTotalPrice;

    @FXML private TextField nameField, phoneField, emailField, addressField;
    @FXML private Label totalLabel;

    private final ObservableList<OrderItem> cartItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        bookCombo.getItems().addAll("Lập trình Java", "Cấu trúc dữ liệu", "SQL cơ bản");
        
        colBookTitle.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getBookTitle()));
        colQuantity.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getQuantity()).asObject());
        colUnitPrice.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getUnitPrice()).asObject());
        colTotalPrice.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getTotalPrice()).asObject());

        orderTable.setItems(cartItems);
    }

    @FXML
    public void handleAddItem() {
        String book = bookCombo.getValue();
        String qtyText = quantityField.getText();

        if (book == null || qtyText.isEmpty()) {
            showAlert("Vui lòng chọn sách và nhập số lượng.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            showAlert("Số lượng không hợp lệ.");
            return;
        }

        double unitPrice = getPriceForBook(book); // Mock giá

        cartItems.add(new OrderItem(book, quantity, unitPrice));
        updateTotal();
        quantityField.clear();
    }

    private double getPriceForBook(String book) {
        return switch (book) {
            case "Lập trình Java" -> 120000;
            case "Cấu trúc dữ liệu" -> 90000;
            case "SQL cơ bản" -> 75000;
            default -> 50000;
        };
    }

    private void updateTotal() {
        double total = cartItems.stream().mapToDouble(OrderItem::getTotalPrice).sum();
        totalLabel.setText(String.format("%.0f VNĐ", total));
    }

    @FXML
    public void handleSubmitOrder() {
        if (cartItems.isEmpty() || nameField.getText().isEmpty()) {
            showAlert("Vui lòng nhập đầy đủ thông tin và thêm sách vào đơn.");
            return;
        }

        Order order = new Order(
                nameField.getText(),
                phoneField.getText(),
                emailField.getText(),
                addressField.getText(),
                List.copyOf(cartItems)
        );

        showAlert("Đơn hàng đã được xác nhận!\nTổng tiền: " + order.getTotal() + " VNĐ");
        cartItems.clear();
        updateTotal();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}
