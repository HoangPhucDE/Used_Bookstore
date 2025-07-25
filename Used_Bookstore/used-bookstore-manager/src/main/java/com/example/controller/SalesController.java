package com.example.controller;
import com.example.DatabaseConnection;
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
    @FXML private ComboBox<String> orderTypeCombo;
    @FXML private TextField quantityField;
    @FXML private TableView<OrderItem> orderTable;
    @FXML private TableColumn<OrderItem, String> colBookTitle;
    @FXML private TableColumn<OrderItem, Integer> colQuantity;
    @FXML private TableColumn<OrderItem, Double> colUnitPrice;
    @FXML private TableColumn<OrderItem, Double> colTotalPrice;
    @FXML private Button deleteItemButton;

    @FXML private TextField nameField, phoneField, emailField, addressField;
    @FXML private Label totalLabel;

    private final ObservableList<OrderItem> cartItems = FXCollections.observableArrayList();
    private final List<Book> allBooks = new ArrayList<>();

    @FXML
    public void initialize() {
        loadBooksFromDatabase();

        orderTypeCombo.getItems().addAll("online", "offline", "trahang", "nhap_kho");
        orderTypeCombo.getSelectionModel().selectFirst(); // Chọn mặc định
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        orderTable.setItems(cartItems);

        phoneField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // Khi người dùng rời khỏi ô số điện thoại
                autoFillCustomerInfo();
            }
        });

    }

    private void resetForm() {
        nameField.clear();
        phoneField.clear();
        emailField.clear();
        addressField.clear();
        quantityField.clear();
        bookCombo.getSelectionModel().clearSelection();
        orderTypeCombo.getSelectionModel().selectFirst();
    }

    private void loadBooksFromDatabase() {
        String query = "SELECT ma_sach, ten_sach, tac_gia, the_loai, gia_ban, so_luong_ton, danh_gia FROM sach WHERE so_luong_ton > 0";

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
                        rs.getInt("so_luong_ton"),
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

    @FXML
    private void handleDeleteItem() {
        OrderItem selectedItem = orderTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Xác nhận");
            confirm.setHeaderText(null);
            confirm.setContentText("Bạn có chắc chắn muốn xóa sách này khỏi giỏ hàng?");
            confirm.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    cartItems.remove(selectedItem);
                    updateTotal();
                }
            });
        } else {
            showAlert("Thông báo", "Vui lòng chọn sách để xóa.");
        }
    }

    private void updateTotal() {
        double total = cartItems.stream().mapToDouble(OrderItem::getTotalPrice).sum();
        totalLabel.setText(String.format("%.0f VNĐ", total));
    }

    private void showRecentOrder(int orderId) {
        String query = "SELECT ma_don, ten_kh, tong_tien, ngay_tao, loai_don " +
                "FROM donhang WHERE ma_don = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                StringBuilder builder = new StringBuilder();
                builder.append("🧾 Đơn hàng mới:\n\n");
                builder.append("Mã đơn: ").append(rs.getInt("ma_don")).append("\n");
                builder.append("Khách hàng: ").append(rs.getString("ten_kh")).append("\n");
                builder.append("Tổng tiền: ").append(String.format("%.0f VNĐ", rs.getDouble("tong_tien"))).append("\n");
                builder.append("Ngày tạo: ").append(rs.getString("ngay_tao")).append("\n");
                builder.append("Loại đơn: ").append(rs.getString("loai_don"));

                showAlert("✅ Đơn hàng gần đây", builder.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể hiển thị đơn hàng gần đây.");
        }
    }

//    private void confirmAndInsertCustomerIfNotExists(String name, String phone, String email, String address) {
//        String checkQuery = "SELECT id FROM khachhang WHERE sdt = ?";
//        String insertQuery = "INSERT INTO khachhang (ho_ten, sdt, email, dia_chi) VALUES (?, ?, ?, ?)";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
//
//            checkStmt.setString(1, phone);
//            ResultSet rs = checkStmt.executeQuery();
//
//            if (!rs.next()) {
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                alert.setTitle("Thêm khách hàng mới");
//                alert.setHeaderText("Số điện thoại chưa có trong hệ thống.");
//                alert.setContentText("Bạn có muốn lưu thông tin khách hàng này không?");
//                ButtonType yes = new ButtonType("Có", ButtonBar.ButtonData.YES);
//                ButtonType no = new ButtonType("Không", ButtonBar.ButtonData.NO);
//                alert.getButtonTypes().setAll(yes, no);
//
//                alert.showAndWait().ifPresent(result -> {
//                    if (result == yes) {
//                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
//                            insertStmt.setString(1, name);
//                            insertStmt.setString(2, phone);
//                            insertStmt.setString(3, email);
//                            insertStmt.setString(4, address);
//                            insertStmt.executeUpdate();
//                            showAlert("Thành công", "Đã lưu thông tin khách hàng mới.");
//                        } catch (SQLException ex) {
//                            showAlert("Lỗi", "Không thể lưu khách hàng: " + ex.getMessage());
//                        }
//                    }
//                });
//            }
//
//        } catch (SQLException e) {
//            showAlert("Lỗi", "Không thể kiểm tra khách hàng: " + e.getMessage());
//        }
//    }


    @FXML
    public void handleSubmitOrder() {
        if (cartItems.isEmpty() || nameField.getText().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập thông tin và thêm sách vào đơn.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            String insertOrder = "INSERT INTO donhang (ten_kh, sdt, email, dia_chi, tong_tien, nguoi_tao_id, ngay_tao, loai_don) VALUES (?, ?, ?, ?, ?, ?, NOW(), ?)";
            String orderType = orderTypeCombo.getValue();
            if (orderType == null || orderType.isEmpty()) {
                showAlert("Lỗi", "Vui lòng chọn loại đơn hàng.");
                return;
            }

            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                double total = cartItems.stream().mapToDouble(OrderItem::getTotalPrice).sum();


                orderStmt.setString(1, nameField.getText());
                orderStmt.setString(2, phoneField.getText());
                orderStmt.setString(3, emailField.getText());
                orderStmt.setString(4, addressField.getText());
                orderStmt.setDouble(5, total);
                orderStmt.setInt(6, LoginController.curentUserId);
                orderStmt.setString(7, orderType);

                orderStmt.executeUpdate();

                ResultSet generatedKeys = orderStmt.getGeneratedKeys();
                if (!generatedKeys.next()) {
                    conn.rollback();
                    showAlert("Lỗi", "Không thể tạo đơn hàng.");
                    return;
                }

                int orderId = generatedKeys.getInt(1);

                // Thêm chi tiết đơn hàng
                String insertItem = "INSERT INTO chitiet_donhang (ma_don, ma_sach, so_luong, don_gia) VALUES (?, ?, ?, ?)";
                String updateStock = "UPDATE sach SET so_luong_ton = so_luong_ton - ? WHERE ma_sach = ?";

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
                showRecentOrder(orderId); // Hiển thị đơn hàng vừa tạo
                // Xác nhận và thêm khách hàng nếu chưa tồn tại
//                confirmAndInsertCustomerIfNotExists(
//                        nameField.getText(),
//                        phoneField.getText(),
//                        emailField.getText(),
//                        addressField.getText()
//                );

                showAlert("Thành công", "Đơn hàng đã được lưu. Tổng tiền: " + total + " VNĐ");
                cartItems.clear();
                updateTotal();
                resetForm();
                bookCombo.getItems().clear();
                allBooks.clear();
                loadBooksFromDatabase(); // Load lại sách từ database

                showAlert("Thành công", "Đơn hàng đã được lưu. Tổng tiền: " + total + " VNĐ");
                cartItems.clear();
                updateTotal();
                resetForm();
                bookCombo.getItems().clear();
                allBooks.clear();
                loadBooksFromDatabase(); // Load lại sách từ database

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

    private void autoFillCustomerInfo() {
        String sdt = phoneField.getText().trim();
        if (sdt.isEmpty()) return;

        String query = "SELECT ten_kh, email, dia_chi FROM donhang WHERE sdt = ? ORDER BY ngay_tao DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sdt);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("ten_kh"));
                emailField.setText(rs.getString("email"));
                addressField.setText(rs.getString("dia_chi"));
            }

        } catch (SQLException e) {
            showAlert("Lỗi", "Không thể tự động điền thông tin: " + e.getMessage());
        }
    }
}
