package com.example.controller;

import java.io.IOException;

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

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

   @FXML
private void handleLogin() {
    String username = usernameField.getText();
    String password = passwordField.getText();

    if (username.isEmpty() || password.isEmpty()) {
        showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin đăng nhập!");
        return;
    }

    if (username.equals("admin") && password.equals("123456")) {
        // Nếu đăng nhập đúng, chuyển sang Home.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/Home.fxml"));
            Parent homeRoot = loader.load();
            Scene homeScene = new Scene(homeRoot);

            // Lấy stage hiện tại và set scene mới
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


    @FXML
    private void handleForgotPassword() {
        showAlert("Thông báo", "Tính năng khôi phục mật khẩu sẽ được cập nhật sớm!");
    }

    @FXML
    private void handleMouseEntered(MouseEvent event) {
        loginButton.setStyle("-fx-background-color: linear-gradient(to right, #5a6fd8, #6a42a0); -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 0; -fx-cursor: hand; -fx-scale-x: 1.02; -fx-scale-y: 1.02;");
    }

    @FXML
    private void handleMouseExited(MouseEvent event) {
        loginButton.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 0; -fx-cursor: hand; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method để focus vào username field khi form được load
    public void initialize() {
        // Set focus vào username field
        usernameField.requestFocus();

        // Add enter key functionality
        usernameField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> handleLogin());

        // Add input validation styling
        addInputValidation();
    }

    private void addInputValidation() {
        // Style khi focus
        String focusStyle = "-fx-background-color: #ffffff; -fx-border-color: #667eea; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 15; -fx-font-size: 14px;";
        String normalStyle = "-fx-background-color: #f8f9fa; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12 15; -fx-font-size: 14px;";

        usernameField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                usernameField.setStyle(focusStyle);
            } else {
                usernameField.setStyle(normalStyle);
            }
        });

        passwordField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                passwordField.setStyle(focusStyle);
            } else {
                passwordField.setStyle(normalStyle);
            }
        });
    }
}