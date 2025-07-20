package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DashboardController {

    @FXML private Label totalBooksLabel;
    @FXML private Label totalUsersLabel;
    @FXML private Label totalSalesLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label todayBooksLabel;
    @FXML private Label todayUsersLabel;
    @FXML private Label todaySalesLabel;
    @FXML private Label todayRevenueLabel;
    @FXML private PieChart categoryChart;
    @FXML private AreaChart<String, Number> revenueChart;
    @FXML private BarChart<String, Number> bookChart;

    @FXML
    public void initialize() {
        setupStatistics();
        setupCategoryChart();
        setupRevenueChart();
        setupBookChart();
    }

    private void setupStatistics() {
        // Mock data cho thống kê tổng quát
        totalBooksLabel.setText("2,345");
        totalUsersLabel.setText("567");
        totalSalesLabel.setText("1,234");
        totalRevenueLabel.setText("45,678,900 ₫");
        
        // Mock data cho hôm nay
        todayBooksLabel.setText("23");
        todayUsersLabel.setText("12");
        todaySalesLabel.setText("45");
        todayRevenueLabel.setText("3,456,000 ₫");
    }

    private void setupCategoryChart() {
        if (categoryChart != null) {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Tiểu thuyết", 35),
                new PieChart.Data("Giáo trình", 25),
                new PieChart.Data("Kỹ năng sống", 20),
                new PieChart.Data("Khoa học", 15),
                new PieChart.Data("Khác", 5)
            );
            categoryChart.setData(pieChartData);
            categoryChart.setTitle("Phân bố sách theo thể loại");
        }
    }

    private void setupRevenueChart() {
        if (revenueChart != null) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Doanh thu");
            
            series.getData().add(new XYChart.Data<>("T1", 2500000));
            series.getData().add(new XYChart.Data<>("T2", 3200000));
            series.getData().add(new XYChart.Data<>("T3", 2800000));
            series.getData().add(new XYChart.Data<>("T4", 4100000));
            series.getData().add(new XYChart.Data<>("T5", 3600000));
            series.getData().add(new XYChart.Data<>("T6", 4500000));
            series.getData().add(new XYChart.Data<>("T7", 3900000));
            
            revenueChart.getData().clear();
            revenueChart.getData().add(series);
            revenueChart.setTitle("Doanh thu 7 ngày qua");
        }
    }

    private void setupBookChart() {
        if (bookChart != null) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Số lượng bán");
            
            series.getData().add(new XYChart.Data<>("Đắc nhân tâm", 45));
            series.getData().add(new XYChart.Data<>("Harry Potter", 38));
            series.getData().add(new XYChart.Data<>("Lập trình Java", 32));
            series.getData().add(new XYChart.Data<>("Tư duy nhanh chậm", 28));
            series.getData().add(new XYChart.Data<>("Nhà giả kim", 25));
            
            bookChart.getData().clear();
            bookChart.getData().add(series);
            bookChart.setTitle("Top 5 sách bán chạy");
        }
    }

    @FXML
    private void refreshDashboard() {
        // Làm mới dữ liệu dashboard
        setupStatistics();
        setupCategoryChart();
        setupRevenueChart();
        setupBookChart();
        
        System.out.println("Dashboard đã được làm mới!");
    }

    @FXML
    private void exportReport() {
        // Xuất báo cáo
        System.out.println("Xuất báo cáo thống kê...");
    }

    @FXML
    private void viewDetailedStats() {
        // Xem thống kê chi tiết
        System.out.println("Chuyển đến trang thống kê chi tiết...");
    }
}