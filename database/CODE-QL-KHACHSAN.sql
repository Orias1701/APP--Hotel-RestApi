USE ql_khachsan;

-- CREATE TABLE A1_khachhang (
--     MaKhachHang VARCHAR(50) PRIMARY KEY COMMENT 'Mã khách hàng',
--     TenKhachHang VARCHAR(100) COMMENT 'Tên khách hàng',
--     GioiTinh ENUM('Nam','Nữ') COMMENT 'Giới tính',
--     Sdt VARCHAR(15) COMMENT 'SĐT',
--     TinhTrangKhach ENUM('Đang ở', 'Đã rời') COMMENT 'Tình trạng khách'
-- ) COMMENT 'Khách hàng';

-- CREATE TABLE A14_nhanvien (
--     MaNhanVien VARCHAR(50) PRIMARY KEY COMMENT 'Mã nhân viên',
--     TenNhanVien VARCHAR(100) COMMENT 'Tên nhân viên',
--     Sdt VARCHAR(15) COMMENT 'Số điện thoại',
--     GioiTinh ENUM('Nam', 'Nữ') COMMENT 'Giới tính',
--     ChucVu VARCHAR(50) COMMENT 'Chức vụ'
-- ) COMMENT 'Nhân viên';

-- CREATE TABLE A2_hoadon (
--     MaHoaDon VARCHAR(50) PRIMARY KEY COMMENT 'Mã hóa đơn',
--     MaNhanVien VARCHAR(50) COMMENT 'Mã nhân viên',
--     MaKhachHang VARCHAR(50) COMMENT 'Mã khách hàng',
--     Ngay DATE COMMENT 'Ngày nhập',
--     FOREIGN KEY (MaNhanVien) REFERENCES A14_nhanvien(MaNhanVien),
--     FOREIGN KEY (MaKhachHang) REFERENCES A1_khachhang(MaKhachHang)
-- ) COMMENT 'Hóa đơn';

-- CREATE TABLE A4_loaiphong (
--     MaLoai VARCHAR(50) PRIMARY KEY COMMENT 'Mã loại phòng',
--        TenLoai VARCHAR(100) COMMENT 'Tên loại',
--        SoGiuong VARCHAR(100) COMMENT'Số giường',
--     GiaLoai DECIMAL(10,2) COMMENT 'Giá loại phòng'
-- ) COMMENT 'Loại phòng';

-- CREATE TABLE A5_phong (
--     MaPhong VARCHAR(50) PRIMARY KEY COMMENT 'Mã phòng',
--     TenPhong VARCHAR(100) COMMENT 'Tên phòng',
--     MaLoai VARCHAR(50) COMMENT'Loại phòng',
--     MoTa VARCHAR(50) COMMENT 'Mô tả',
--     TinhTrangPhong ENUM('Trống', 'Đang sử dụng') COMMENT 'Tình trạng phòng',
--     FOREIGN KEY (MaLoai) REFERENCES A4_loaiphong(MaLoai)
-- ) COMMENT 'Phòng';

-- CREATE TABLE A6_datphong (
--     MaDatPhong VARCHAR(50) PRIMARY KEY COMMENT 'Mã đặt phòng',
--     MaPhong VARCHAR(50) COMMENT 'Mã phòng',
--     MaNhanVien VARCHAR(50) COMMENT 'Mã nhân viên', 
--     MaKhachHang VARCHAR(50) COMMENT 'Mã khách hàng',
--     NgayNhanPhong DATE COMMENT 'Ngày nhận phòng',
--     NgayTraPhong DATE COMMENT 'Ngày trả phòng',
--     FOREIGN KEY (MaPhong) REFERENCES A5_phong(MaPhong),
--     FOREIGN KEY (MaNhanVien) REFERENCES A14_nhanvien(MaNhanVien),
--     FOREIGN KEY (MaKhachHang) REFERENCES A1_khachhang(MaKhachHang)
-- ) COMMENT 'Đặt phòng';

-- CREATE TABLE A7_thietbi (
--     MaThietBi VARCHAR(50) PRIMARY KEY COMMENT 'Mã thiết bị',
--     TenThietBi VARCHAR(100) COMMENT 'Tên thiết bị',
--     DonGia DECIMAL(10,2) COMMENT 'Đơn giá thiết bị'
-- 	   DenBu DECIMAL(10,2) COMMENT 'Đền bù',
-- ) COMMENT 'Thiết bị';

-- CREATE TABLE A8_thietbiphong (
--     MaPhong VARCHAR(50) COMMENT 'Mã phòng',
--     MaThietBi VARCHAR(50) COMMENT 'Mã thiết bị',
--     SoLuong INT COMMENT 'Số lượng thiết bị',
--     TrangThai ENUM('Tốt', 'Hư hỏng') COMMENT 'Tình trạng thiết bị',
--     PRIMARY KEY (MaPhong, MaThietBi),
--     FOREIGN KEY (MaPhong) REFERENCES A5_phong(MaPhong),
--     FOREIGN KEY (MaThietBi) REFERENCES A7_thietbi(MaThietBi)
-- ) COMMENT 'Thiết bị phòng';

-- CREATE TABLE A9_kiemtraphong (
--     MaKiemTra VARCHAR(50) PRIMARY KEY COMMENT 'Mã kiểm tra phòng',
--     MaPhong VARCHAR(50) COMMENT 'Mã phòng',
--     MaNhanVien VARCHAR(50) COMMENT 'Mã nhân viên',
--     NgayKiemTra DATE COMMENT 'Ngày kiểm tra phòng',
--     GhiChu VARCHAR(255) COMMENT 'Ghi chú',
--     TrangThai ENUM('Tốt', 'Cần dọn', 'Hỏng') COMMENT 'Tình trạng phòng',
--     FOREIGN KEY (MaPhong) REFERENCES A5_phong(MaPhong),
--     FOREIGN KEY (MaNhanVien) REFERENCES A14_nhanvien(MaNhanVien)
-- ) COMMENT 'Kiểm tra phòng';

-- CREATE TABLE A10_kiemtrachitiet (
--     MaKiemTra VARCHAR(50) COMMENT 'Mã kiểm tra',
--     MaPhong VARCHAR(50) COMMENT 'Mã phòng',
--     MaThietBi VARCHAR(50) COMMENT 'Mã thiết bị',
--     TinhTrang ENUM('Tốt', 'Hỏng') COMMENT 'Tình trạng thiết bị',
--     GhiChu VARCHAR(255) COMMENT 'Ghi chú',
-- 	PRIMARY KEY (MaKiemTra, MaThietBi),
--     FOREIGN KEY (MaKiemTra) REFERENCES A9_kiemtraphong(MaKiemTra),
--     FOREIGN KEY (MaThietBi) REFERENCES A7_thietbi(MaThietBi)
-- ) COMMENT 'Kiểm tra chi tiết';

-- CREATE TABLE A11_dichvu (
--     MaDichVu VARCHAR(50) PRIMARY KEY COMMENT 'Mã dịch vụ',
--     TenDichVu VARCHAR(255) COMMENT 'Tên dịch vụ',
--     LoaiDichVu VARCHAR(100) COMMENT 'Loại dịch vụ',
--     GiaDichVu DECIMAL(10,2) COMMENT 'Giá dịch vụ'
-- ) COMMENT 'Dịch vụ';

-- CREATE TABLE A12_sddv (
--     MaSDDV VARCHAR(50) PRIMARY KEY COMMENT 'Mã sử dụng dịch vụ',
--     MaKhachHang VARCHAR(50) COMMENT 'Mã khách hàng',
--     MaNhanVien VARCHAR(50) COMMENT 'Mã nhân viên',
--     NgaySDDV DATE COMMENT 'Ngày sử dụng dịch vụ',
--     FOREIGN KEY (MaKhachHang) REFERENCES A1_khachhang(MaKhachHang),
--     FOREIGN KEY (MaNhanVien) REFERENCES A14_nhanvien(MaNhanVien)
-- ) COMMENT 'Sử dụng dịch vụ';

-- CREATE TABLE A13_chitietsddv (
--     MaSDDV VARCHAR(50) COMMENT 'Mã sử dụng dịch vụ',
--     MaDichVu VARCHAR(50) COMMENT 'Mã dịch vụ',
--     SoLuongDichVu INT COMMENT 'Số lượng dịch vụ sử dụng',
--     PRIMARY KEY (MaSDDV, MaDichVu),
--     FOREIGN KEY (MaSDDV) REFERENCES A12_sddv(MaSDDV),
--     FOREIGN KEY (MaDichVu) REFERENCES A11_dichvu(MaDichVu)
-- ) COMMENT 'Sử dụng dịch vụ chi tiết';

-- CREATE TABLE A3_hoadonchitiet (
--     MaHoaDon VARCHAR(50) COMMENT 'Mã hóa đơn',
--     MaPhong VARCHAR(50) COMMENT 'Mã phòng',
--     MaDichVu VARCHAR(50) COMMENT 'Mã dịch vụ',
--     PRIMARY KEY (MaHoaDon, MaPhong, MaDichVu),
--     FOREIGN KEY (MaHoaDon) REFERENCES A2_hoadon(MaHoaDon),
--     FOREIGN KEY (MaPhong) REFERENCES A5_phong(MaPhong),
--     FOREIGN KEY (MaDichVu) REFERENCES A11_dichvu(MaDichVu)
-- ) COMMENT 'Hóa đơn chi tiết';

-- ALTER TABLE a5_phong MODIFY TinhTrangPhong ENUM('Trống', 'Đang sử dụng', 'Đã đặt');
-- ALTER TABLE a5_phong 
-- MODIFY TinhTrangPhong ENUM('Trống', 'Đang sử dụng', 'Đã đặt') COMMENT 'Tình trạng phòng';


-- ALTER TABLE a6_datphong
-- MODIFY COLUMN NgayNhanPhong DATETIME COMMENT'Ngày nhận phòng';
-- ALTER TABLE a6_datphong
-- MODIFY COLUMN NgayTraPhong DATETIME COMMENT'Ngày trả phòng';

-- ALTER TABLE a9_kiemtraphong
-- MODIFY COLUMN NgayKiemTra DATETIME COMMENT 'Ngày kiểm tra';
-- ALTER TABLE a6_datphong
-- ADD COLUMN CachDat ENUM ('Đặt online','Đặt trực tiếp');
-- ALTER TABLE a6_datphong
-- MODIFY COLUMN CachDat ENUM ('Đặt online','Đặt trực tiếp') COMMENT 'Cách đặt';
-- ALTER TABLE a6_datphong
--  MODIFY TinhTrang ENUM ('Đang sử dụng','Quá hạn','Đang đợi','Đã trả');
-- ALTER TABLE a6_datphong
-- MODIFY COLUMN TinhTrang ENUM ('Đang sử dụng','Quá hạn','Đang đợi') COMMENT 'Tình trạng';
-- ALTER TABLE a2_hoadon
-- ADD COLUMN TongTien Decimal(10,2);
-- ALTER TABLE a2_hoadon
-- MODIFY COLUMN TongTien Decimal(10,2) COMMENT'Tổng tiền';
-- ALTER TABLE a6_datphong
-- ADD COLUMN TienPhat DECIMAL(10,2) AFTER TinhTrang;
ALTER TABLE a6_datphong
MODIFY COLUMN TienPhat DECIMAL(10,2) DEFAULT 0.00 COMMENT 'Phạt';







