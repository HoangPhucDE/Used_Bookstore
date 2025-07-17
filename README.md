# Used_Bookstore
Hello this's a Used_Bookstore Project in VanHien University. 
- We have 3 members:
    + Nguyen Nhat Vi (Team Leader)
    + Tran Thi Van Anh
    + Nguyen Chi Khanh
    + Nguyen Hoang Phuc

# 📘 HỆ THỐNG QUẢN LÝ VÀ BÁN SÁCH CŨ

## 🎯 Giới thiệu

Dự án xây dựng một hệ thống **toàn diện** phục vụ:
- ✅ Quản lý hiệu sách nội bộ (sách, nhân viên, tài khoản, hóa đơn, thống kê)
- ✅ Bán sách cũ cho khách hàng qua ứng dụng (JavaFX App hoặc Web)

Ứng dụng triển khai bằng **JavaFX + JDBC + MySQL**, có phân quyền rõ ràng cho admin, nhân viên và khách hàng.

---

## ⚙️ Công nghệ sử dụng

| Thành phần         | Công nghệ                    |
|--------------------|------------------------------|
| Giao diện nội bộ   | JavaFX (FXML)                |
| Giao diện khách    | JavaFX App                   |
| Kết nối CSDL       | JDBC                         |
| Cơ sở dữ liệu      | MySQL                        |
| Báo cáo & Export   | Apache POI, iText (tuỳ chọn) |

---

## 👥 Phân quyền người dùng

| Chức năng              | Admin | Nhân viên | Khách hàng |
|------------------------|-------|-----------|------------|
| Đăng nhập              | ✅    | ✅        | ✅         |
| Quản lý sách           | ✅    | ❌        | ❌         |
| Quản lý nhân viên      | ✅    | ❌        | ❌         |
| Mua sách online        | ❌    | ❌        | ✅         |
| Tạo đơn offline        | ✅    | ✅        | ❌         |
| Thống kê doanh thu     | ✅    | ❌        | ❌         |

---

## 🧩 Chức năng chính

### ✅ Quản lý sách
- Thêm/sửa/xóa sách
- Tình trạng sách: sách mới / sách cũ
- Tìm kiếm, phân loại

### ✅ Quản lý nhân viên & tài khoản
- Gắn tài khoản với nhân viên
- Phân quyền `admin`, `user`, `khach`

### ✅ Bán hàng
- Tạo đơn hàng tại quầy (offline)
- Đặt hàng online qua giỏ hàng

### ✅ Khách hàng
- Xem sách, tìm kiếm
- Thêm vào giỏ hàng
- Thanh toán, theo dõi đơn hàng

### ✅ Thống kê
- Doanh thu theo thời gian
- Sách bán chạy, nhân viên bán tốt
- Xuất báo cáo ra Excel / PDF

---

## 🗃️ Cơ sở dữ liệu (các bảng chính)

- `taikhoan` – quản lý đăng nhập, phân quyền
- `nhanvien` – thông tin nhân viên nội bộ
- `khachhang` – thông tin người mua sách
- `sach` – danh mục sách
- `giohang` – giỏ hàng của khách
- `donhang` – đơn hàng chung (online + offline)
- `chitiet_donhang` – chi tiết sách trong mỗi đơn

---

## 🚀 Hướng dẫn chạy dự án

### 1. Cài đặt yêu cầu:
- Java JDK 17 trở lên
- MySQL Server
- Scene Builder (tùy chọn)
- IDE: IntelliJ IDEA / Eclipse / NetBeans

### 2. Clone dự án:
```bash
git clone https://github.com/HoangPhucDE/Used-Bookstore-Manager.git
