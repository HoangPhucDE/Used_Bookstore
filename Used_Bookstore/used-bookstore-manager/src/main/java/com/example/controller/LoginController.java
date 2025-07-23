package com.example.controller;

import java.io.IOException;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {

    // 🔧 Thông tin kết nối CSDL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/used_bookstore";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;

    // 🎯 Xử lý đăng nhập
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin đăng nhập!");
            return;
        }

        boolean loginSuccess = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM taikhoan WHERE username = ? AND mat_khau = ?")) {

                stmt.setString(1, username);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        loginSuccess = true;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            showAlert("Lỗi", "Không tìm thấy JDBC Driver!");
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            showAlert("Lỗi", "Lỗi kết nối CSDL: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (loginSuccess) {
            showAlert("Thông báo", "Đăng nhập thành công!");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/View/Home.fxml"));
                Parent homeRoot = loader.load();
                Scene homeScene = new Scene(homeRoot);
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                currentStage.setScene(homeScene);
                currentStage.setTitle("Trang chủ");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể tải giao diện chính.");
            }
        } else {
            showAlert("Lỗi", "Tên đăng nhập hoặc mật khẩu không đúng.");
        }
    }

    // 🛟 Thông báo chưa hỗ trợ khôi phục mật khẩu
    @FXML
    private void handleForgotPassword() {
        showAlert("Thông báo", "Tính năng khôi phục mật khẩu sẽ được cập nhật sớm!");
    }

    // 💫 Hiệu ứng hover cho nút login
    @FXML
    private void handleMouseEntered(MouseEvent event) {
        loginButton.setStyle("-fx-background-color: linear-gradient(to right, #5a6fd8, #6a42a0);"
                + "-fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold; "
                + "-fx-font-size: 14px; -fx-padding: 12 0; -fx-cursor: hand; -fx-scale-x: 1.02; -fx-scale-y: 1.02;");
    }

    @FXML
    private void handleMouseExited(MouseEvent event) {
        loginButton.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2);"
                + "-fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold; "
                + "-fx-font-size: 14px; -fx-padding: 12 0; -fx-cursor: hand; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    // 📢 Hiển thị hộp thoại cảnh báo
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 🚀 Khởi tạo ban đầu
    public void initialize() {
        usernameField.requestFocus();

        usernameField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> handleLogin());

        addInputValidation();
    }

    // 🎨 Style khi focus và khi không focus
    private void addInputValidation() {
        String focusStyle = "-fx-background-color: #ffffff; -fx-border-color: #667eea;"
                + "-fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;"
                + "-fx-padding: 10 15; -fx-font-size: 14px;";
        String normalStyle = "-fx-background-color: #f8f9fa; -fx-border-color: #e0e0e0;"
                + "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12 15;"
                + "-fx-font-size: 14px;";

        usernameField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            usernameField.setStyle(isNowFocused ? focusStyle : normalStyle);
        });

        passwordField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            passwordField.setStyle(isNowFocused ? focusStyle : normalStyle);
        });
    }
}