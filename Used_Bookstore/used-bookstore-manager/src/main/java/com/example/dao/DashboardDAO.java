package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.example.controller.DBConnect;

public class DashboardDAO {

    public Map<String, Object> getGeneralStats() {
        Map<String, Object> stats = new HashMap<>();

        try (Connection conn = DBConnect.getConnection()) {

            // Tổng số sách
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM sach")) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("totalBooks", rs.getInt(1));
                }
            }

            // Tổng số người dùng
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM nguoidung")) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("totalUsers", rs.getInt(1));
                }
            }

            // Tổng số đơn hàng (bán)
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM hoadon_ban")) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("totalSales", rs.getInt(1));
                }
            }

            // Tổng doanh thu
            try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(tong_tien) FROM hoadon_ban")) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("totalRevenue", rs.getDouble(1));
                }
            }

            // Hôm nay: số sách mới
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM sach WHERE DATE(ngay_tao) = CURDATE()")) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("todayBooks", rs.getInt(1));
                }
            }

            // Hôm nay: người dùng mới
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM nguoidung WHERE DATE(ngay_tao) = CURDATE()")) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("todayUsers", rs.getInt(1));
                }
            }

            // Hôm nay: số đơn bán
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM hoadon_ban WHERE DATE(ngay_ban) = CURDATE()")) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("todaySales", rs.getInt(1));
                }
            }

            // Hôm nay: doanh thu
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT SUM(tong_tien) FROM hoadon_ban WHERE DATE(ngay_ban) = CURDATE()")) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    stats.put("todayRevenue", rs.getDouble(1));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stats;
    }
}
