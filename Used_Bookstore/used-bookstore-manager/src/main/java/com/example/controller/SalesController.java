package com.example.controller;

import com.example.DataBaseConnection.DatabaseConnection;
import com.example.model.Book;
import com.example.model.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.ArrayList;
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
    private final List<Book> allBooks = new ArrayList<>();

    @FXML
    public void initialize() {
        loadBooksFromDatabase();

        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        orderTable.setItems(cartItems);
    }

    private void loadBooksFromDatabase() {
        String query = "SELECT ma_sach, ten_sach, tac_gia, the_loai, gia_ban, so_luuong_ton, danh_gia FROM sach WHERE so_luuong_ton > 0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("ma_sach"),
                        rs.getString("ten_sach"),
                        rs.getString("tac_gia"),
                        rs.getString("the_loai"),
                        rs.getDouble("gia_ban"),
                        rs.getInt("so_luuong_ton"),
                        rs.getDouble("danh_gia")
                );
                allBooks.add(book);
                bookCombo.getItems().add(book.getTitle());
            }

        } catch (SQLException e) {
            showAlert("Lỗi", "Không thể tải sách từ CSDL: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddItem() {
        String bookTitle = bookCombo.getValue();
        String qtyText = quantityField.getText();

        if (bookTitle == null || qtyText.isEmpty()) {
            showAlert("Lỗi", "Vui lòng chọn sách và nhập số lượng.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Số lượng không hợp lệ.");
            return;
        }

        Book selectedBook = allBooks.stream()
                .filter(book -> book.getTitle().equals(bookTitle))
                .findFirst()
                .orElse(null);

        if (selectedBook == null) {
            showAlert("Lỗi", "Không tìm thấy sách.");
            return;
        }

        if (quantity > selectedBook.getStock()) {
            showAlert("Lỗi", "Số lượng đặt vượt quá số lượng tồn kho. Hiện có: " + selectedBook.getStock());
            return;
        }

        cartItems.add(new OrderItem(bookTitle, quantity, selectedBook.getPrice()));
        updateTotal();
        quantityField.clear();
    }

    private void updateTotal() {
        double total = cartItems.stream().mapToDouble(OrderItem::getTotalPrice).sum();
        totalLabel.setText(String.format("%.0f VNĐ", total));
    }

    @FXML
    public void handleSubmitOrder() {
        if (cartItems.isEmpty() || nameField.getText().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập thông tin và thêm sách vào đơn.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            String insertOrder = "INSERT INTO don_hang (ten_kh, sdt, email, dia_chi, tong_tien, nguoi_tao_id, ngay_tao) VALUES (?, ?, ?, ?, ?, ?, NOW())";
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                double total = cartItems.stream().mapToDouble(OrderItem::getTotalPrice).sum();

                orderStmt.setString(1, nameField.getText());
                orderStmt.setString(2, phoneField.getText());
                orderStmt.setString(3, emailField.getText());
                orderStmt.setString(4, addressField.getText());
                orderStmt.setDouble(5, total);
                orderStmt.setInt(6, LoginController.curentUserId);

                orderStmt.executeUpdate();

                ResultSet generatedKeys = orderStmt.getGeneratedKeys();
                if (!generatedKeys.next()) {
                    conn.rollback();
                    showAlert("Lỗi", "Không thể tạo đơn hàng.");
                    return;
                }

                int orderId = generatedKeys.getInt(1);

                // Thêm chi tiết đơn hàng
                String insertItem = "INSERT INTO chi_tiet_don_hang (id_don_hang, id_sach, so_luong, don_gia) VALUES (?, ?, ?, ?)";
                String updateStock = "UPDATE sach SET so_luuong_ton = so_luuong_ton - ? WHERE ma_sach = ?";

                try (PreparedStatement itemStmt = conn.prepareStatement(insertItem);
                     PreparedStatement stockStmt = conn.prepareStatement(updateStock)) {

                    for (OrderItem item : cartItems) {
                        Book book = allBooks.stream()
                                .filter(b -> b.getTitle().equals(item.getBookTitle()))
                                .findFirst()
                                .orElseThrow();

                        // Thêm vào chi tiết đơn
                        itemStmt.setInt(1, orderId);
                        itemStmt.setInt(2, book.getId());
                        itemStmt.setInt(3, item.getQuantity());
                        itemStmt.setDouble(4, item.getUnitPrice());
                        itemStmt.addBatch();

                        // Cập nhật tồn kho
                        stockStmt.setInt(1, item.getQuantity());
                        stockStmt.setInt(2, book.getId());
                        stockStmt.addBatch();
                    }

                    itemStmt.executeBatch();
                    stockStmt.executeBatch();
                }

                conn.commit();
                showAlert("Thành công", "Đơn hàng đã được lưu. Tổng tiền: " + total + " VNĐ");
                cartItems.clear();
                updateTotal();

            } catch (SQLException e) {
                conn.rollback();
                showAlert("Lỗi", "Không thể lưu đơn hàng: " + e.getMessage());
            }

        } catch (SQLException e) {
            showAlert("Lỗi", "Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
