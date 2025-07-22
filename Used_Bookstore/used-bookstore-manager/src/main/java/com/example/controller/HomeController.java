package com.example.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class HomeController {

    // Khai báo các button navigation và content pane
    @FXML private Button homeBtn;
    @FXML private Button bookBtn;
    @FXML private Button employeeBtn;
    @FXML private Button userBtn;
    @FXML private Button salesBtn;
    @FXML private Button statsBtn;
    @FXML private StackPane contentPane;

    // Style cho button active và inactive
    private final String ACTIVE_STYLE = "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                                       "-fx-text-fill: white; " +
                                       "-fx-background-radius: 8; " +
                                       "-fx-padding: 12 20; " +
                                       "-fx-font-size: 14px; " +
                                       "-fx-font-weight: 500; " +
                                       "-fx-alignment: center-left;";

    private final String INACTIVE_STYLE = "-fx-background-color: transparent; " +
                                         "-fx-text-fill: #2c3e50; " +
                                         "-fx-background-radius: 8; " +
                                         "-fx-padding: 12 20; " +
                                         "-fx-font-size: 14px; " +
                                         "-fx-font-weight: 500; " +
                                         "-fx-alignment: center-left;";

    @FXML
    public void initialize() {

        goHome(); // Gọi hàm đầy đủ để load luôn trang dashboard

    }

    // Method để set button active
    private void setActiveButton(Button activeButton) {
        // Reset tất cả button về inactive
        resetAllButtons();
        // Set button được chọn thành active
        activeButton.setStyle(ACTIVE_STYLE);
    }

    // Reset tất cả button về trạng thái inactive
    private void resetAllButtons() {
        if (homeBtn != null) homeBtn.setStyle(INACTIVE_STYLE);
        if (bookBtn != null) bookBtn.setStyle(INACTIVE_STYLE);
        if (employeeBtn != null) employeeBtn.setStyle(INACTIVE_STYLE);
        if (userBtn != null) userBtn.setStyle(INACTIVE_STYLE);
        if (salesBtn != null) salesBtn.setStyle(INACTIVE_STYLE);
        if (statsBtn != null) statsBtn.setStyle(INACTIVE_STYLE);
    }

    @FXML
    private void goHome() {
        setActiveButton(homeBtn);
        loadHomePage();
    }

    @FXML
    private void goBook() {
        setActiveButton(bookBtn);
        loadPage("/com/example/view/BookManagement.fxml");
    }

    @FXML
    private void goEmployee() {
        setActiveButton(employeeBtn);
        loadPage("/com/example/view/EmployeeManagement.fxml");
    }

    @FXML
    private void goUser() {
        setActiveButton(userBtn);
        loadPage("/com/example/view/UserManagement.fxml");
    }

    @FXML
    private void goSales() {
        setActiveButton(salesBtn);
        loadPage("/com/example/view/SalesView.fxml");
    }

    @FXML
    private void goStats() {
        setActiveButton(statsBtn);
        loadPage("/com/example/view/Statistics.fxml");
    }

    // Method để load trang
    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Không thể load trang: " + fxmlPath);
        }
    }

    // Method để load trang chủ mặc định
    private void loadHomePage() {
        contentPane.getChildren().clear();
        // Thêm lại welcome content
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/Dashboard.fxml"));
            contentPane.getChildren().add(loader.load());
        } catch (IOException e) {
            // Nếu không có file welcome, tạo content mặc định
            javafx.scene.control.Label welcomeLabel = new javafx.scene.control.Label("🎉 Chào mừng đến với Hệ thống Quản lý Thư viện");
            welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
            contentPane.getChildren().add(welcomeLabel);
        }
    }

    @FXML
private void logout() {
    try {
        // Load lại giao diện Login.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/Login.fxml"));
        javafx.scene.Parent root = loader.load();

        // Lấy Stage hiện tại và thay Scene
        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        javafx.stage.Stage currentStage = (javafx.stage.Stage) contentPane.getScene().getWindow();
        currentStage.setScene(scene);
        currentStage.setTitle("Đăng nhập");
        currentStage.centerOnScreen();
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Không thể chuyển về màn hình đăng nhập.");
    }
}

}
// 