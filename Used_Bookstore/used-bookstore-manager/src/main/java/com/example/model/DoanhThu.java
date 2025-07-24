package com.example.model;

public class DoanhThu {
    private String ngay;
    private int tongDon;
    private double tongTien;

    public DoanhThu(String ngay, int tongDon, double tongTien) {
        this.ngay = ngay;
        this.tongDon = tongDon;
        this.tongTien = tongTien;
    }

    public String getNgay() {
        return ngay;
    }

    public int getTongDon() {
        return tongDon;
    }

    public double getTongTien() {
        return tongTien;
    }
}
