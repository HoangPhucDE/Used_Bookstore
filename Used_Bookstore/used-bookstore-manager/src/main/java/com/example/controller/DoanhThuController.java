package com.example.controller;
import com.example.DataBaseConnection.DatabaseConnection;
import com.example.model.DoanhThu;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.FileOutputStream;
import java.sql.*;

public class DoanhThuController {

    @FXML
    private TableView<DoanhThu> tableView;
    @FXML
    private TableColumn<DoanhThu, String> colNgay;
    @FXML
    private TableColumn<DoanhThu, Integer> colTongDon;
    @FXML
    private TableColumn<DoanhThu, Double> colTongTien;
    @FXML
    private Button btnExport;

    private ObservableList<DoanhThu> doanhThuList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Thiết lập cột
        colNgay.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNgay()));
        colTongDon.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTongDon()).asObject());
        colTongTien.setCellValueFactory(
                data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTongTien()).asObject());

        // Load từ DB
        loadDoanhThuFromDB();
    }

    private void loadDoanhThuFromDB() {
        doanhThuList.clear();
        String query = """
        SELECT DATE(ngay_tao) AS ngay, 
               COUNT(*) AS tongDon, 
               SUM(tong_tien) AS tongTien 
        FROM donhang 
        WHERE trang_thai = 'hoan_thanh'
        GROUP BY DATE(ngay_tao)
        ORDER BY ngay DESC
    """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String ngay = rs.getString("ngay");
                int tongDon = rs.getInt("tongDon");
                double tongTien = rs.getDouble("tongTien");

                doanhThuList.add(new DoanhThu(ngay, tongDon, tongTien));
            }

            tableView.setItems(doanhThuList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải dữ liệu từ cơ sở dữ liệu.");
        }
    }

    @FXML
    public void exportPDF() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Lưu file PDF");
            fileChooser.setInitialFileName("doanhthu.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));

            java.io.File file = fileChooser.showSaveDialog(null);
            if (file == null)
                return;

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            document.add(new Paragraph("BÁO CÁO DOANH THU\n\n"));
            PdfPTable table = new PdfPTable(3);
            table.addCell("Ngày");
            table.addCell("Tổng đơn");
            table.addCell("Tổng tiền");

            for (DoanhThu dt : doanhThuList) {
                table.addCell(dt.getNgay());
                table.addCell(String.valueOf(dt.getTongDon()));
                table.addCell(String.format("%.0f", dt.getTongTien()));
            }

            document.add(table);
            document.close();

            showAlert("Thành công", "Xuất PDF thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Xuất PDF thất bại.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}