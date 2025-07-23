package com.example.controller;

import com.example.model.RevenueByBook;
import com.example.model.RevenueByEmployee;
import com.example.model.RevenueByTime;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.util.List;

public class StatisticsController {

    @FXML private TableView<RevenueByTime> timeTable;
    @FXML private TableColumn<RevenueByTime, LocalDate> dateCol;
    @FXML private TableColumn<RevenueByTime, Double> revenueCol;

    @FXML private TableView<RevenueByBook> bookTable;
    @FXML private TableColumn<RevenueByBook, String> bookNameCol;
    @FXML private TableColumn<RevenueByBook, Integer> quantityCol;

    @FXML private TableView<RevenueByEmployee> employeeTable;
    @FXML private TableColumn<RevenueByEmployee, String> employeeNameCol;
    @FXML private TableColumn<RevenueByEmployee, Double> employeeRevenueCol;
    @FXML private TableColumn<RevenueByEmployee, Integer> invoiceCountCol;

    @FXML
    public void initialize() {
        // Mapping table columns
        dateCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getDate()));
        revenueCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getRevenue()));

        bookNameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getBookName()));
        quantityCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getQuantity()));

        employeeNameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmployeeName()));
        employeeRevenueCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getRevenue()));
        invoiceCountCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getInvoiceCount()));

        loadMockData();
    }

    private void loadMockData() {
        timeTable.setItems(FXCollections.observableArrayList(
                new RevenueByTime(LocalDate.of(2024, 7, 19), 1_500_000),
                new RevenueByTime(LocalDate.of(2024, 7, 20), 2_200_000),
                new RevenueByTime(LocalDate.of(2024, 7, 21), 3_100_000)
        ));

        bookTable.setItems(FXCollections.observableArrayList(
                new RevenueByBook("Đắc nhân tâm", 120),
                new RevenueByBook("7 thói quen hiệu quả", 95),
                new RevenueByBook("Lập trình Java cơ bản", 78)
        ));

        employeeTable.setItems(FXCollections.observableArrayList(
                new RevenueByEmployee("Nguyễn Văn A", 5_000_000, 25),
                new RevenueByEmployee("Trần Thị B", 3_500_000, 18),
                new RevenueByEmployee("Lê Văn C", 4_200_000, 21)
        ));
    }

    @FXML
    private void handleExportExcel() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Export Excel (mock) thành công!");
        alert.showAndWait();
    }

    @FXML
    private void handleExportPDF() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Export PDF (mock) thành công!");
        alert.showAndWait();
    }
}
