CREATE DATABASE IF NOT EXISTS used_bookstore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE Used_Bookstore;

-- 1. Bảng tài khoản
CREATE TABLE taikhoan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    mat_khau VARCHAR(255) NOT NULL,
    vai_tro ENUM('admin', 'user', 'khach') NOT NULL,
    loai_nguoi_dung ENUM('nhanvien', 'khachhang') NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    trang_thai BOOLEAN NOT NULL DEFAULT TRUE
);

-- 2. Bảng nhân viên
CREATE TABLE nhanvien (
    ma_nv INT AUTO_INCREMENT PRIMARY KEY,
    ho_ten VARCHAR(100) NOT NULL,
    ngay_sinh DATE,
    sdt VARCHAR(20),
    chuc_vu VARCHAR(50),
    trang_thai BOOLEAN DEFAULT TRUE,
    id_taikhoan INT UNIQUE,
    FOREIGN KEY (id_taikhoan) REFERENCES taikhoan(id) ON DELETE SET NULL
);

-- 3. Bảng khách hàng
CREATE TABLE khachhang (
    ma_kh INT AUTO_INCREMENT PRIMARY KEY,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    sdt VARCHAR(20),
    dia_chi TEXT,
    id_taikhoan INT UNIQUE,
    FOREIGN KEY (id_taikhoan) REFERENCES taikhoan(id) ON DELETE SET NULL
);

-- 4. Bảng sách
CREATE TABLE sach (
    ma_sach INT AUTO_INCREMENT PRIMARY KEY,
    ten_sach VARCHAR(255) NOT NULL,
    tac_gia VARCHAR(100),
    the_loai VARCHAR(50),
    nxb VARCHAR(100),
    nam_xb YEAR,
    gia_nhap DOUBLE NOT NULL CHECK (gia_nhap >= 0),
    gia_ban DOUBLE NOT NULL CHECK (gia_ban >= 0),
    tinh_trang ENUM('moi', 'cu', 'tot', 'trung_binh', 'kem') DEFAULT 'cu',
    hinh_anh TEXT
);

-- 5. Bảng giỏ hàng
CREATE TABLE giohang (
    ma_kh INT,
    ma_sach INT,
    so_luong INT NOT NULL CHECK (so_luong > 0),
    ngay_them DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (ma_kh, ma_sach),
    FOREIGN KEY (ma_kh) REFERENCES khachhang(ma_kh) ON DELETE CASCADE,
    FOREIGN KEY (ma_sach) REFERENCES sach(ma_sach) ON DELETE CASCADE
);

-- 6. Bảng đơn hàng
CREATE TABLE donhang (
    ma_don INT AUTO_INCREMENT PRIMARY KEY,
    loai_don ENUM('online', 'offline', 'trahang', 'nhap_kho') NOT NULL,
    nguoi_tao_id INT NOT NULL,
    ten_kh VARCHAR(100),
    sdt VARCHAR(20),
    email VARCHAR(100),
    dia_chi TEXT,
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP,
    tong_tien DOUBLE NOT NULL CHECK (tong_tien >= 0),
    trang_thai ENUM('cho_duyet', 'dang_giao', 'hoan_thanh', 'huy') DEFAULT 'cho_duyet',
    FOREIGN KEY (nguoi_tao_id) REFERENCES taikhoan(id) ON DELETE CASCADE
);

-- 7. Bảng chi tiết đơn hàng
CREATE TABLE chitiet_donhang (
    ma_don INT,
    ma_sach INT,
    so_luong INT NOT NULL CHECK (so_luong > 0),
    don_gia DOUBLE NOT NULL CHECK (don_gia >= 0),
    PRIMARY KEY (ma_don, ma_sach),
    FOREIGN KEY (ma_don) REFERENCES donhang(ma_don) ON DELETE CASCADE,
    FOREIGN KEY (ma_sach) REFERENCES sach(ma_sach) ON DELETE CASCADE
);

USE Used_Bookstore;

-- 1. Tài khoản
INSERT INTO taikhoan (username, mat_khau, vai_tro, loai_nguoi_dung, email, trang_thai) VALUES
('admin_quyen', '123456@Admin', 'admin', 'nhanvien', 'quyen.admin@example.com', TRUE),
('leminh_nhanvien', '123456@Nv', 'user', 'nhanvien', 'leminh.tuan@example.com', TRUE),
('trananh_1990', '123456@Khach', 'khach', 'khachhang', 'anhdung.tran@gmail.com', TRUE),
('phamhoa_admin', 'hoa123@Admin', 'admin', 'nhanvien', 'phamhoa@example.com', TRUE),
('phuocnv', 'phuoc123@Nv', 'user', 'nhanvien', 'phuoc.nv@example.com', TRUE),
('vananh_khach', 'vananh123@Khach', 'khach', 'khachhang', 'vananh.kh@example.com', TRUE),
('tuananh_khach', 'tuananh123@Khach', 'khach', 'khachhang', 'tuananh.kh@example.com', TRUE);

-- 2. Nhân viên
INSERT INTO nhanvien (ho_ten, ngay_sinh, sdt, chuc_vu, trang_thai, id_taikhoan) VALUES
('Nguyễn Thị Quyên', '1985-05-20', '0901123456', 'Quản lý', TRUE, 1),
('Lê Minh Tuấn', '1992-09-12', '0987123456', 'Bán hàng', TRUE, 2),
('Phạm Thị Hoa', '1989-06-20', '0911000011', 'Quản lý kho', TRUE, 4),
('Lê Phước', '1992-02-14', '0911000012', 'Thu ngân', TRUE, 5);

-- 3. Khách hàng
INSERT INTO khachhang (ho_ten, email, sdt, dia_chi, id_taikhoan) VALUES
('Trần Anh Dũng', 'anhdung.tran@gmail.com', '0912121212', '123 Pasteur, P.6, Q.3, TP.HCM', 3),
('Nguyễn Vân Anh', 'vananh.kh@example.com', '0911222333', '45 Nguyễn Trãi, Q.5, TP.HCM', 6),
('Đỗ Tuấn Anh', 'tuananh.kh@example.com', '0911444555', '88 Lý Thường Kiệt, Q.10, TP.HCM', 7);

-- 4. Sách
INSERT INTO sach (ten_sach, tac_gia, the_loai, nxb, nam_xb, gia_nhap, gia_ban, tinh_trang, hinh_anh) VALUES
('Dám Nghĩ Lớn', 'David J. Schwartz', 'Phát triển bản thân', 'NXB Trẻ', 2021, 50000, 85000, 'tot', NULL),
('Nhà Giả Kim', 'Paulo Coelho', 'Tiểu thuyết', 'NXB Văn Học', 2019, 60000, 95000, 'cu', NULL),
('Lập Trình Python Cơ Bản', 'Nguyễn Văn Long', 'Giáo trình', 'NXB Lao Động', 2022, 80000, 120000, 'moi', NULL),
('Tư Duy Nhanh Và Chậm', 'Daniel Kahneman', 'Tâm lý học', 'NXB Thế Giới', 2020, 85000, 135000, 'moi', NULL),
('Tuổi Trẻ Đáng Giá Bao Nhiêu', 'Rosie Nguyễn', 'Phát triển bản thân', 'NXB Trẻ', 2019, 50000, 88000, 'cu', NULL),
('Muôn Kiếp Nhân Sinh', 'Nguyên Phong', 'Tâm linh', 'NXB Tổng Hợp', 2021, 70000, 120000, 'tot', NULL),
('Sherlock Holmes Toàn Tập', 'Arthur Conan Doyle', 'Trinh thám', 'NXB Văn Học', 2022, 90000, 150000, 'moi', NULL),
('Lược Sử Thời Gian', 'Stephen Hawking', 'Khoa học', 'NXB Trẻ', 2018, 95000, 160000, 'moi', NULL);

-- 5. Giỏ hàng
INSERT INTO giohang (ma_kh, ma_sach, so_luong) VALUES
(1, 1, 1),
(1, 3, 1),
(2, 4, 1),
(2, 5, 2),
(3, 6, 1);

-- 6. Đơn hàng
INSERT INTO donhang (loai_don, nguoi_tao_id, ten_kh, sdt, email, dia_chi, tong_tien, trang_thai) VALUES
('online', 3, 'Trần Anh Dũng', '0912121212', 'anhdung.tran@gmail.com', '123 Pasteur, P.6, Q.3, TP.HCM', 205000, 'cho_duyet'),
('online', 6, 'Nguyễn Vân Anh', '0911222333', 'vananh.kh@example.com', '45 Nguyễn Trãi, Q.5, TP.HCM', 223000, 'cho_duyet'),
('online', 7, 'Đỗ Tuấn Anh', '0911444555', 'tuananh.kh@example.com', '88 Lý Thường Kiệt, Q.10, TP.HCM', 120000, 'cho_duyet');

-- 7. Chi tiết đơn hàng
INSERT INTO chitiet_donhang (ma_don, ma_sach, so_luong, don_gia) VALUES
(1, 1, 1, 85000),
(1, 3, 1, 120000),
(2, 4, 1, 135000),
(2, 5, 1, 88000),
(3, 6, 1, 120000);