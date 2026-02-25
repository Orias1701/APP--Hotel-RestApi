-- Active: 1743462407381@@127.0.0.1@3306@accounts
-- ALTER TABLE `chitietphieusddv` COMMENT = 'CHI TIẾT';
-- ALTER TABLE chitietphieusddv MODIFY MaPhieuSDDV varchar(50) COMMENT 'Mã phiếu';
-- ALTER TABLE chitietphieusddv MODIFY MaDichVu varchar(50) COMMENT 'Mã dịch vụ';
-- ALTER TABLE chitietphieusddv MODIFY SoLuongDichVu int NOT NULL DEFAULT 1 COMMENT 'Số lượng';


-- ALTER TABLE `dichvu` COMMENT = 'DỊCH VỤ';
-- ALTER TABLE dichvu MODIFY MaDichVu varchar(50) COMMENT 'Mã dịch vụ';
-- ALTER TABLE dichvu MODIFY LoaiDichVu varchar(100) COMMENT 'Loại dịch vụ';
-- ALTER TABLE dichvu MODIFY GiaDichVu decimal(10,2) COMMENT 'Giá dịch vụ';


-- ALTER TABLE `hoadon` COMMENT = 'HÓA ĐƠN';
-- ALTER TABLE hoadon MODIFY MaHoaDon varchar(50) COMMENT 'Mã hóa đơn';
-- ALTER TABLE hoadon MODIFY MaNhanVien varchar(50) COMMENT 'Mã nhân viên';
-- ALTER TABLE hoadon MODIFY MaKhachHang varchar(50) COMMENT 'Mã khách hàng';
-- ALTER TABLE hoadon MODIFY Ngay date COMMENT 'Ngày tạo';


-- ALTER TABLE `khachhang` COMMENT = 'KHÁCH HÀNG';
-- ALTER TABLE khachhang MODIFY MaKhachHang varchar(50) COMMENT 'Mã khách hàng';
-- ALTER TABLE khachhang MODIFY TenKhachHang varchar(100) COMMENT 'Tên kkhách hàng';
-- ALTER TABLE khachhang MODIFY Sdt varchar(15) COMMENT 'Số điện thoại';
-- ALTER TABLE khachhang MODIFY TinhTrangKhach enum('Đang ở','Đã rời đi','Hủy đặt phòng') COMMENT 'Tình trạng';


-- ALTER TABLE `loaiphong` COMMENT = 'LOẠI PHÒNG';
-- ALTER TABLE loaiphong MODIFY Maloai varchar(50) COMMENT 'Mã loại phòng';
-- ALTER TABLE loaiphong MODIFY TenLoaiPhong varchar(100) COMMENT 'Tên loại phòng';
-- ALTER TABLE loaiphong MODIFY GiaLoai decimal(10,2) COMMENT 'Giá loại phòng';


-- ALTER TABLE `nhanvien` COMMENT = 'NHÂN VIÊN';
-- ALTER TABLE nhanvien MODIFY MaNhanVien varchar(50) COMMENT 'Mã nhân viên';
-- ALTER TABLE nhanvien MODIFY TenNhanVien varchar(100) COMMENT 'Tên nhân viên';
-- ALTER TABLE nhanvien MODIFY Sdt varchar(15) COMMENT 'Số điện thoại';
-- ALTER TABLE nhanvien MODIFY GioiTinh enum('Nam','Nữ') COMMENT 'Giới tính';

-- ALTER TABLE `phieudatphong` COMMENT = 'PHIẾU ĐẶT PHÒNG';
-- ALTER TABLE phieudatphong MODIFY MaPhieuDatPhong varchar(50) COMMENT 'Mã phiếu';
-- ALTER TABLE phieudatphong MODIFY MaPhong varchar(50) COMMENT 'Mã phòng';
-- ALTER TABLE phieudatphong MODIFY MaNhanVien varchar(50) COMMENT 'Mã nhân viên';
-- ALTER TABLE phieudatphong MODIFY MaKhachHang varchar(50) COMMENT 'Mã khách hàng';
-- ALTER TABLE phieudatphong MODIFY MaHoaDon varchar(50) COMMENT 'Mã hóa đơn';
-- ALTER TABLE phieudatphong MODIFY NgayNhanPhong date COMMENT 'Ngày nhận';
-- ALTER TABLE phieudatphong MODIFY NgayTraPhong date COMMENT 'Ngày trả';


-- ALTER TABLE `phieusddv` COMMENT = 'PHIẾU DỊCH VỤ';
-- ALTER TABLE phieusddv MODIFY MaPhieuSDDV varchar(50) COMMENT 'Mã phiếu';
-- ALTER TABLE phieusddv MODIFY MaKhachHang varchar(50) COMMENT 'Mã khách hàng';
-- ALTER TABLE phieusddv MODIFY MaNhanVien varchar(50) COMMENT 'Mã nhân viên';
-- ALTER TABLE phieusddv MODIFY NgaySDDV date COMMENT 'Ngày sử dụng';

-- ALTER TABLE `phong` COMMENT = 'QUẢN LÝ PHÒNG';
-- ALTER TABLE phong MODIFY MaPhong varchar(50) COMMENT 'Mã phòng';
-- ALTER TABLE phong MODIFY TenPhong varchar(100) COMMENT 'Tên phòng';
-- ALTER TABLE phong MODIFY MaLoai varchar(50) COMMENT 'Mã loại phòng';
-- ALTER TABLE phong MODIFY MoTa varchar(50) COMMENT 'Mô tả phòng';
-- ALTER TABLE phong MODIFY TinhTrangPhong enum('Trống','Đang sử dụng','Đã đặt trước','Bảo trì') COMMENT 'Tình trạng';
