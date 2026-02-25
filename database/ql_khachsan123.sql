DROP DATABASE IF EXISTS ql_khachsan;
CREATE DATABASE IF NOT EXISTS ql_khachsan;
USE ql_khachsan;

-- Table f1_nhanvien
DROP TABLE IF EXISTS `f1_nhanvien`;
CREATE TABLE `f1_nhanvien` (
  `MaNhanVien` varchar(50) NOT NULL COMMENT 'Mã nhân viên',
  `TenNhanVien` varchar(100) DEFAULT NULL COMMENT 'Tên nhân viên',
  `Sdt` varchar(15) DEFAULT NULL COMMENT 'Số điện thoại',
  `GioiTinh` enum('Nam','Nữ') DEFAULT NULL COMMENT 'Giới tính',
  `ChucVu` varchar(50) DEFAULT NULL COMMENT 'Chức vụ',
  PRIMARY KEY (`MaNhanVien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Nhân viên';

LOCK TABLES `f1_nhanvien` WRITE;
INSERT INTO `f1_nhanvien` VALUES 
('RM001', 'Trần Thị Bình', '0988123001', 'Nữ', 'Quản lý phòng'),
('RM002', 'Lê Quốc Dũng', '0988123002', 'Nam', 'Quản lý phòng'),
('RM003', 'Phạm Thị Hòa', '0988123003', 'Nữ', 'Quản lý phòng'),
('SV001', 'Ngô Minh Nhật', '0988123004', 'Nam', 'Dịch vụ'),
('SV002', 'Vũ Thị Hạnh', '0988123005', 'Nữ', 'Dịch vụ'),
('SV003', 'Đặng Thanh Tùng', '0988123006', 'Nam', 'Dịch vụ'),
('CK001', 'Bùi Văn Kiên', '0988123007', 'Nam', 'Kiểm tra'),
('CK002', 'Đỗ Thị Mai', '0988123008', 'Nữ', 'Kiểm tra'),
('CK003', 'Hoàng Tuấn Anh', '0988123009', 'Nam', 'Kiểm tra');
UNLOCK TABLES;

-- Table a1_khachhang
DROP TABLE IF EXISTS `a1_khachhang`;
CREATE TABLE `a1_khachhang` (
  `MaKhachHang` varchar(50) NOT NULL COMMENT 'Mã khách hàng',
  `TenKhachHang` varchar(100) DEFAULT NULL COMMENT 'Tên khách hàng',
  `GioiTinh` enum('Nam','Nữ') DEFAULT NULL COMMENT 'Giới tính',
  `Sdt` varchar(15) DEFAULT NULL COMMENT 'SĐT',
  `TinhTrangKhach` enum('Đang ở','Đã rời','Đang đặt') DEFAULT NULL COMMENT 'Tình trạng',
  PRIMARY KEY (`MaKhachHang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Khách hàng';

LOCK TABLES `a1_khachhang` WRITE;
INSERT INTO `a1_khachhang` VALUES 
('KH001','Nguyễn Văn An','Nam','0987654321','Đã rời'),
('KH002','Lê Thị Bích','Nữ','0912345678','Đã rời'),
('KH003','Trần Minh Châu','Nam','0923456789','Đã rời'),
('KH004','Phạm Thị Duyên','Nữ','0934567890','Đã rời'),
('KH005','Hoàng Quang Liêm','Nam','0945678901','Đã rời'),
('KH006','Vũ Thị Mai','Nữ','0956789012','Đã rời'),
('KH007','Bùi Minh Giang','Nam','0967890123','Đã rời'),
('KH008','Ngô Thị Hà','Nữ','0978901234','Đã rời'),
('KH009','Đỗ Quang Hùng','Nam','0989012345','Đã rời'),
('KH010','Nguyễn Thị Lan','Nữ','0990123456','Đã rời'),
('KH011','Trần Quang Kiên','Nam','0901234567','Đã rời'),
('KH012','Lê Minh Linh','Nữ','0912345670','Đã rời'),
('KH013','Vũ Quang Minh','Nam','0923456780','Đã rời'),
('KH014','Hoàng Thị Ngọc','Nữ','0934567890','Đã rời'),
('KH015','Bùi Quang Hạo','Nam','0945678900','Đã rời'),
('KH016','Phan Minh Phú','Nam','0956789011','Đã rời'),
('KH017','Trương Quang Quyền','Nữ','0967890122','Đã rời'),
('KH018','Đào Thị Rạng','Nam','0978901235','Đã rời'),
('KH019','Dương Quang Sơn','Nữ','0989012346','Đã rời'),
('KH020','Nguyễn Quang Tình','Nam','0990123457','Đã rời');
UNLOCK TABLES;

-- Table c1_loaiphong
DROP TABLE IF EXISTS `c1_loaiphong`;
CREATE TABLE `c1_loaiphong` (
  `MaLoai` varchar(50) NOT NULL COMMENT 'Mã loại phòng',
  `TenLoai` varchar(100) DEFAULT NULL COMMENT 'Tên loại',
  `SoGiuong` int DEFAULT NULL COMMENT 'Số giường',
  `GiaLoai` decimal(10,2) DEFAULT NULL COMMENT 'Giá loại phòng',
  PRIMARY KEY (`MaLoai`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Loại phòng';

LOCK TABLES `c1_loaiphong` WRITE;
INSERT INTO `c1_loaiphong` VALUES 
('LP001','Standard','1',3000000.00),
('LP002','Standard','2',3500000.00),
('LP003','VIP','1',5000000.00),
('LP004','VIP','2',6500000.00),
('LP005','Presidential','1',10000000.00),
('LP006','Presidential','2',12000000.00);
UNLOCK TABLES;

-- Table c2_phong
DROP TABLE IF EXISTS `c2_phong`;
CREATE TABLE `c2_phong` (
  `MaPhong` varchar(50) NOT NULL COMMENT 'Mã phòng',
  `TenPhong` varchar(100) DEFAULT NULL COMMENT 'Tên phòng',
  `MaLoai` varchar(50) DEFAULT NULL COMMENT 'Loại phòng',
  `MoTa` varchar(50) DEFAULT NULL COMMENT 'Mô tả',
  `TinhTrangPhong` enum('Trống','Đang sử dụng','Đã đặt') DEFAULT NULL COMMENT 'Tình trạng phòng',
  PRIMARY KEY (`MaPhong`),
  KEY `MaLoai` (`MaLoai`),
  CONSTRAINT `c2_phong_ibfk_1` FOREIGN KEY (`MaLoai`) REFERENCES `c1_loaiphong` (`MaLoai`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Phòng';
LOCK TABLES `c2_phong` WRITE;
INSERT INTO `c2_phong` VALUES 
('P101','Standard','LP001','Phòng tiêu chuẩn 1 giường','Trống'),
('P102','Standard','LP002','Phòng tiêu chuẩn 2 giường','Trống'),
('P103','Standard','LP001','Phòng tiêu chuẩn 1 giường','Trống'),
('P104','Standard','LP002','Phòng tiêu chuẩn 2 giường','Trống'),
('P105','Standard','LP001','Phòng tiêu chuẩn 1 giường','Trống'),
('P106','Standard','LP002','Phòng tiêu chuẩn 2 giường','Trống'),
('P107','Standard','LP001','Phòng tiêu chuẩn 1 giường','Trống'),
('P108','Standard','LP002','Phòng tiêu chuẩn 2 giường','Trống'),
('P109','Standard','LP001','Phòng tiêu chuẩn 1 giường','Trống'),
('P110','Standard','LP002','Phòng tiêu chuẩn 2 giường','Trống'),
('P111','Standard','LP001','Phòng tiêu chuẩn 1 giường','Trống'),
('P112','Standard','LP002','Phòng tiêu chuẩn 2 giường','Trống'),
('P201','VIP','LP003','Phòng VIP 1 giường','Trống'),
('P202','VIP','LP004','Phòng VIP 2 giường','Trống'),
('P203','VIP','LP003','Phòng VIP 1 giường','Trống'),
('P204','VIP','LP004','Phòng VIP 2 giường','Trống'),
('P205','VIP','LP003','Phòng VIP 1 giường','Trống'),
('P206','VIP','LP004','Phòng VIP 2 giường','Trống'),
('P207','VIP','LP003','Phòng VIP 1 giường','Trống'),
('P208','VIP','LP004','Phòng VIP 2 giường','Trống'),
('P209','VIP','LP003','Phòng VIP 1 giường','Trống'),
('P210','VIP','LP004','Phòng VIP 2 giường','Trống'),
('P211','VIP','LP003','Phòng VIP 1 giường','Trống'),
('P212','VIP','LP004','Phòng VIP 2 giường','Trống'),
('P213','VIP','LP003','Phòng VIP 1 giường','Trống'),
('P214','VIP','LP004','Phòng VIP 2 giường','Trống'),
('P215','VIP','LP003','Phòng VIP 1 giường','Trống'),
('P301','Presidential','LP005','Phòng Tổng thống 1 giường','Trống'),
('P302','Presidential','LP006','Phòng Tổng thống 2 giường','Trống'),
('P303','Presidential','LP005','Phòng Tổng thống 1 giường','Trống'),
('P304','Presidential','LP006','Phòng Tổng thống 2 giường','Trống'),
('P305','Presidential','LP005','Phòng Tổng thống 1 giường','Trống'),
('P306','Presidential','LP006','Phòng Tổng thống 2 giường','Trống'),
('P307','Presidential','LP005','Phòng Tổng thống 1 giường','Trống'),
('P308','Presidential','LP005','Phòng Tổng thống 1 giường','Trống'),
('P309','Presidential','LP006','Phòng Tổng thống 2 giường','Trống'),
('P310','Presidential','LP006','Phòng Tổng thống 2 giường','Trống');
UNLOCK TABLES;

-- Table c3_datphong
DROP TABLE IF EXISTS `c3_datphong`;
CREATE TABLE `c3_datphong` (
  `MaDatPhong` varchar(50) NOT NULL COMMENT 'Mã đặt phòng',
  `MaPhong` varchar(50) DEFAULT NULL COMMENT 'Mã phòng',
  `MaNhanVien` varchar(50) DEFAULT NULL COMMENT 'Mã nhân viên',
  `MaKhachHang` varchar(50) DEFAULT NULL COMMENT 'Mã khách hàng',
  `NgayNhanPhong` datetime DEFAULT NULL COMMENT 'Ngày nhận phòng',
  `NgayHen` datetime DEFAULT NULL COMMENT 'Ngày hẹn trả',
  `NgayTraPhong` datetime DEFAULT NULL COMMENT 'Ngày trả phòng',
  `CachDat` enum('Đặt online','Đặt trực tiếp') DEFAULT NULL COMMENT 'Cách đặt',
  `TinhTrang` enum('Đang sử dụng','Quá hạn','Đang đợi','Đã trả') DEFAULT NULL COMMENT 'Tình trạng',
  `TienPhat` decimal(10,2) DEFAULT '0.00' COMMENT 'Phạt',
  PRIMARY KEY (`MaDatPhong`),
  KEY `MaPhong` (`MaPhong`),
  KEY `MaNhanVien` (`MaNhanVien`),
  KEY `MaKhachHang` (`MaKhachHang`),
  CONSTRAINT `c3_datphong_ibfk_1` FOREIGN KEY (`MaPhong`) REFERENCES `c2_phong` (`MaPhong`),
  CONSTRAINT `c3_datphong_ibfk_2` FOREIGN KEY (`MaNhanVien`) REFERENCES `f1_nhanvien` (`MaNhanVien`),
  CONSTRAINT `c3_datphong_ibfk_3` FOREIGN KEY (`MaKhachHang`) REFERENCES `a1_khachhang` (`MaKhachHang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Đặt phòng';

LOCK TABLES `c3_datphong` WRITE;
INSERT INTO `c3_datphong` VALUES 
('DP001','P101','RM001','KH001','2025-04-18 17:00:00','2025-04-19 17:00:00','2025-04-19 18:00:00','Đặt trực tiếp','Đã trả',900000.00),
('DP002','P102','RM002','KH002','2025-04-19 00:00:00','2025-04-21 00:00:00','2025-04-22 01:00:00','Đặt trực tiếp','Đã trả',1050000.00);
UNLOCK TABLES;

-- Table d1_thietbi
DROP TABLE IF EXISTS `d1_thietbi`;
CREATE TABLE `d1_thietbi` (
  `MaThietBi` varchar(50) NOT NULL COMMENT 'Mã thiết bị',
  `TenThietBi` varchar(100) DEFAULT NULL COMMENT 'Tên thiết bị',
  `DonGia` decimal(10,2) DEFAULT NULL COMMENT 'Đơn giá thiết bị',
  `DenBu` decimal(10,2) DEFAULT NULL COMMENT 'Đền bù',
  PRIMARY KEY (`MaThietBi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Thiết bị';

LOCK TABLES `d1_thietbi` WRITE;
INSERT INTO `d1_thietbi` VALUES 
('TB01','Giường ngủ đôi 1m6x2m',4500000.00,4950000.00),
('TB02','Nệm cao su Kim Cương 1m6x2m',3500000.00,3850000.00),
('TB03','Chăn ga gối nệm đầy đủ bộ',1200000.00,1380000.00),
('TB04','Tủ quần áo gỗ công nghiệp',3000000.00,3300000.00),
('TB05','Tivi Samsung 40 inch',8000000.00,8800000.00),
('TB06','Máy lạnh Daikin 1.5HP Inverter',9500000.00,10500000.00),
('TB07','Bàn làm việc gỗ ép',1000000.00,1150000.00),
('TB08','Ghế làm việc có đệm',600000.00,660000.00),
('TB09','Đèn ngủ để bàn',250000.00,300000.00),
('TB10','Rèm cửa cách nhiệt',1000000.00,1150000.00),
('TB11','Tủ lạnh mini Aqua 90L',2500000.00,2850000.00),
('TB12','Ấm đun nước siêu tốc',350000.00,400000.00),
('TB13','Ly tách thủy tinh 2 cái',100000.00,120000.00),
('TB14','Máy sấy tóc Panasonic mini',450000.00,500000.00),
('TB15','Điện thoại bàn nội bộ',850000.00,950000.00),
('TB16','Gương soi toàn thân',700000.00,800000.00),
('TB17','Kệ để vali',400000.00,450000.00),
('TB18','Bình chữa cháy mini',750000.00,850000.00),
('TB19','Hộp khăn giấy gỗ',150000.00,180000.00),
('TB20','Két sắt mini điện tử',1800000.00,2000000.00);
UNLOCK TABLES;

-- Table d2_thietbiphong
DROP TABLE IF EXISTS `d2_thietbiphong`;
CREATE TABLE `d2_thietbiphong` (
  `MaPhong` varchar(50) NOT NULL COMMENT 'Mã phòng',
  `MaThietBi` varchar(50) NOT NULL COMMENT 'Mã thiết bị',
  `SoLuong` int DEFAULT NULL COMMENT 'Số lượng thiết bị',
  `TrangThai` enum('Tốt','Hư hỏng') DEFAULT NULL COMMENT 'Tình trạng thiết bị',
  PRIMARY KEY (`MaPhong`,`MaThietBi`),
  KEY `MaThietBi` (`MaThietBi`),
  CONSTRAINT `d2_thietbiphong_ibfk_1` FOREIGN KEY (`MaPhong`) REFERENCES `c2_phong` (`MaPhong`),
  CONSTRAINT `d2_thietbiphong_ibfk_2` FOREIGN KEY (`MaThietBi`) REFERENCES `d1_thietbi` (`MaThietBi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Thiết bị phòng';

LOCK TABLES `d2_thietbiphong` WRITE;
INSERT INTO `d2_thietbiphong` VALUES 
('P101','TB01',1,'Tốt'),
('P101','TB05',1,'Tốt'),
('P101','TB06',1,'Tốt'),
('P101','TB11',1,'Tốt'),
('P101','TB12',1,'Tốt'),
('P101','TB14',1,'Tốt'),
('P102','TB01',1,'Tốt'),
('P102','TB05',1,'Hư hỏng'),
('P102','TB06',1,'Tốt'),
('P102','TB12',1,'Tốt'),
('P102','TB20',1,'Tốt'),
('P103','TB01',1,'Tốt'),
('P103','TB05',1,'Tốt'),
('P103','TB06',1,'Tốt'),
('P103','TB13',2,'Tốt'),
('P104','TB01',1,'Hư hỏng'),
('P104','TB06',1,'Tốt'),
('P104','TB09',2,'Tốt'),
('P104','TB18',1,'Tốt');
UNLOCK TABLES;

-- Table d3_kiemtraphong
DROP TABLE IF EXISTS `d3_kiemtraphong`;
CREATE TABLE `d3_kiemtraphong` (
  `MaKiemTra` varchar(50) NOT NULL COMMENT 'Mã kiểm tra phòng',
  `MaPhong` varchar(50) DEFAULT NULL COMMENT 'Mã phòng',
  `MaNhanVien` varchar(50) DEFAULT NULL COMMENT 'Mã nhân viên',
  `NgayKiemTra` datetime DEFAULT NULL COMMENT 'Ngày kiểm tra',
  `GhiChu` varchar(255) DEFAULT NULL COMMENT 'Ghi chú',
  `TrangThai` enum('Tốt','Cần dọn','Hỏng') DEFAULT NULL COMMENT 'Tình trạng phòng',
  `TongTien` decimal(10,2) DEFAULT NULL COMMENT 'Tiền đền',
  PRIMARY KEY (`MaKiemTra`),
  KEY `MaPhong` (`MaPhong`),
  KEY `MaNhanVien` (`MaNhanVien`),
  CONSTRAINT `d3_kiemtraphong_ibfk_1` FOREIGN KEY (`MaPhong`) REFERENCES `c2_phong` (`MaPhong`),
  CONSTRAINT `d3_kiemtraphong_ibfk_2` FOREIGN KEY (`MaNhanVien`) REFERENCES `f1_nhanvien` (`MaNhanVien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Kiểm tra phòng';

LOCK TABLES `d3_kiemtraphong` WRITE;
INSERT INTO `d3_kiemtraphong` VALUES 
('KT001','P101','CK001','2025-04-14 11:00:00','Kiểm tra phòng định kỳ','Tốt',13750000.00),
('KT002','P102','CK002','2025-04-22 00:00:00','kiểm tra pkt002','Tốt',4950000.00),
('KT003','P102','CK001','2025-04-22 00:00:00','KT P102','Tốt',0.00);
UNLOCK TABLES;

-- Table d4_kiemtrachitiet
DROP TABLE IF EXISTS `d4_kiemtrachitiet`;
CREATE TABLE `d4_kiemtrachitiet` (
  `MaKiemTra` varchar(50) NOT NULL COMMENT 'Mã kiểm tra',
  `MaPhong` varchar(50) DEFAULT NULL COMMENT 'Mã phòng',
  `MaThietBi` varchar(50) NOT NULL COMMENT 'Mã thiết bị',
  `TinhTrang` enum('Tốt','Hỏng') DEFAULT NULL COMMENT 'Tình trạng thiết bị',
  `SoLuong` int DEFAULT NULL COMMENT 'Số lượng',
  `DenBu` decimal(10,2) DEFAULT NULL COMMENT 'Đền bù',
  `GhiChu` varchar(255) DEFAULT NULL COMMENT 'Ghi chú',
  PRIMARY KEY (`MaKiemTra`,`MaThietBi`),
  KEY `MaThietBi` (`MaThietBi`),
  KEY `fk_kiemtra_thietbiphong` (`MaPhong`,`MaThietBi`),
  CONSTRAINT `d4_kiemtrachitiet_ibfk_1` FOREIGN KEY (`MaKiemTra`) REFERENCES `d3_kiemtraphong` (`MaKiemTra`),
  CONSTRAINT `fk_kiemtra_thietbiphong` FOREIGN KEY (`MaPhong`, `MaThietBi`) REFERENCES `d2_thietbiphong` (`MaPhong`, `MaThietBi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Kiểm tra chi tiết';

LOCK TABLES `d4_kiemtrachitiet` WRITE;
INSERT INTO `d4_kiemtrachitiet` VALUES 
('KT001','P101','TB01','Hỏng',1,4950000.00,NULL),
('KT001','P101','TB05','Hỏng',1,8800000.00,NULL),
('KT002','P102','TB01','Hỏng',1,4950000.00,'1'),
('KT003','P102','TB01','Tốt',1,0.00,'1');
UNLOCK TABLES;

-- Table e1_dichvu
DROP TABLE IF EXISTS `e1_dichvu`;
CREATE TABLE `e1_dichvu` (
  `MaDichVu` varchar(50) NOT NULL COMMENT 'Mã dịch vụ',
  `TenDichVu` varchar(255) DEFAULT NULL COMMENT 'Tên dịch vụ',
  `LoaiDichVu` varchar(100) DEFAULT NULL COMMENT 'Loại dịch vụ',
  `GiaDichVu` decimal(10,2) DEFAULT NULL COMMENT 'Giá dịch vụ',
  PRIMARY KEY (`MaDichVu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Dịch vụ';

LOCK TABLES `e1_dichvu` WRITE;
INSERT INTO `e1_dichvu` VALUES 
('DV001','Bữa sáng tại phòng','Ăn uống',200000.00),
('DV002','Gọi món ăn tại phòng','Ăn uống',200000.00),
('DV003','Gọi nước uống tại phòng','Ăn uống',100000.00),
('DV004','Giặt áo sơ mi','Giặt ủi - Vệ sinh',50000.00),
('DV005','Giặt quần dài','Giặt ủi - Vệ sinh',50000.00),
('DV006','Ủi quần áo','Giặt ủi - Vệ sinh',100000.00),
('DV007','Dọn phòng theo yêu cầu','Giặt ủi - Vệ sinh',0.00),
('DV008','Gọi taxi sân bay','Di chuyển',400000.00),
('DV009','Đưa đón khách hàng bằng xe riêng','Di chuyển',700000.00),
('DV010','Thuê xe máy','Di chuyển',400000.00),
('DV011','Thuê xe oto','Di chuyển',1500000.00),
('DV012','In tài liệu (1 trang)','Văn phòng',10000.00),
('DV013','Thuê laptop','Văn phòng',200000.00),
('DV014','Dịch vụ spa tại phòng','Thư giãn - Thể thao',800000.00),
('DV015','Massage toàn thân 60 phút','Thư giãn - Thể thao',1500000.00),
('DV016','Phòng tập gym miễn phí','Thư giãn - Thể thao',0.00),
('DV017','Thuê sân tennis','Thư giãn - Thể thao',500000.00),
('DV018','Phòng hội thảo 20 người','Sự kiện',2000000.00),
('DV019','Trang trí sinh nhật/honeymoon','Sự kiện',800000.00),
('DV020','Tổ chức sự kiện (theo giờ)','Sự kiện',10000000.00);
UNLOCK TABLES;

-- Table e2_sddv
DROP TABLE IF EXISTS `e2_sddv`;
CREATE TABLE `e2_sddv` (
  `MaSDDV` varchar(50) NOT NULL COMMENT 'Mã sử dụng dịch vụ',
  `MaKhachHang` varchar(50) DEFAULT NULL COMMENT 'Mã khách hàng',
  `MaNhanVien` varchar(50) DEFAULT NULL COMMENT 'Mã nhân viên',
  `NgaySDDV` date DEFAULT NULL COMMENT 'Ngày sử dụng dịch vụ',
  `TongTien` decimal(10,2) DEFAULT NULL COMMENT 'Tổng tiền',
  PRIMARY KEY (`MaSDDV`),
  KEY `MaKhachHang` (`MaKhachHang`),
  KEY `MaNhanVien` (`MaNhanVien`),
  CONSTRAINT `e2_sddv_ibfk_1` FOREIGN KEY (`MaKhachHang`) REFERENCES `a1_khachhang` (`MaKhachHang`),
  CONSTRAINT `e2_sddv_ibfk_2` FOREIGN KEY (`MaNhanVien`) REFERENCES `f1_nhanvien` (`MaNhanVien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Sử dụng dịch vụ';

LOCK TABLES `e2_sddv` WRITE;
INSERT INTO `e2_sddv` VALUES 
('SDDV001','KH001','SV001','2025-04-14',1600000.00),
('SDDV002','KH002','SV001','2025-04-22',0.00);
UNLOCK TABLES;

-- Table e3_chitietsddv
DROP TABLE IF EXISTS `e3_chitietsddv`;
CREATE TABLE `e3_chitietsddv` (
  `MaSDDV` varchar(50) NOT NULL COMMENT 'Mã sử dụng dịch vụ',
  `MaDichVu` varchar(50) NOT NULL COMMENT 'Mã dịch vụ',
  `SoLuongDichVu` int DEFAULT NULL COMMENT 'Số lượng dịch vụ sử dụng',
  `TongTien` decimal(10,2) DEFAULT NULL COMMENT 'Tổng tiền',
  PRIMARY KEY (`MaSDDV`,`MaDichVu`),
  KEY `MaDichVu` (`MaDichVu`),
  CONSTRAINT `e3_chitietsddv_ibfk_1` FOREIGN KEY (`MaSDDV`) REFERENCES `e2_sddv` (`MaSDDV`),
  CONSTRAINT `e3_chitietsddv_ibfk_2` FOREIGN KEY (`MaDichVu`) REFERENCES `e1_dichvu` (`MaDichVu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Sử dụng dịch vụ chi tiết';

LOCK TABLES `e3_chitietsddv` WRITE;
INSERT INTO `e3_chitietsddv` VALUES 
('SDDV001','DV001',2,400000.00),
('SDDV001','DV002',4,800000.00),
('SDDV001','DV003',4,400000.00);
UNLOCK TABLES;

-- Table b1_hoadon
DROP TABLE IF EXISTS `b1_hoadon`;
CREATE TABLE `b1_hoadon` (
  `MaHoaDon` varchar(50) NOT NULL COMMENT 'Mã hóa đơn',
  `MaNhanVien` varchar(50) DEFAULT NULL COMMENT 'Mã nhân viên',
  `MaKhachHang` varchar(50) DEFAULT NULL COMMENT 'Mã khách hàng',
  `Ngay` date DEFAULT NULL COMMENT 'Ngày nhập',
  `TongTien` decimal(10,2) DEFAULT NULL COMMENT 'Tổng tiền',
  PRIMARY KEY (`MaHoaDon`),
  KEY `MaNhanVien` (`MaNhanVien`),
  KEY `MaKhachHang` (`MaKhachHang`),
  CONSTRAINT `b1_hoadon_ibfk_1` FOREIGN KEY (`MaNhanVien`) REFERENCES `f1_nhanvien` (`MaNhanVien`),
  CONSTRAINT `b1_hoadon_ibfk_2` FOREIGN KEY (`MaKhachHang`) REFERENCES `a1_khachhang` (`MaKhachHang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Hóa đơn';

LOCK TABLES `b1_hoadon` WRITE;
INSERT INTO `b1_hoadon` VALUES 
('HD001','RM001','KH001','2025-04-15',35750000.00);
UNLOCK TABLES;

-- Table b2_hoadonchitiet
DROP TABLE IF EXISTS `b2_hoadonchitiet`;
CREATE TABLE `b2_hoadonchitiet` (
  `MaHoaDon` varchar(50) NOT NULL COMMENT 'Mã hóa đơn',
  `MaPhong` varchar(50) NOT NULL COMMENT 'Mã phòng',
  `MaSDDV` varchar(50) NOT NULL COMMENT 'Mã dịch vụ sử dụng',
  `MaKiemTra` varchar(50) NOT NULL COMMENT 'Mã kiểm tra phòng',
  `MaDatPhong` varchar(50) NOT NULL COMMENT 'Mã đặt phòng',
  `TongTien` decimal(10,2) DEFAULT NULL COMMENT 'Tiền hóa đơn',
  PRIMARY KEY (`MaHoaDon`,`MaPhong`,`MaSDDV`,`MaKiemTra`,`MaDatPhong`),
  KEY `fk_hoadonchitiet_sddv` (`MaSDDV`),
  KEY `fk_hoadonchitiet_kiemtra` (`MaKiemTra`),
  KEY `fk_hoadonchitiet_datphong` (`MaDatPhong`),
  CONSTRAINT `fk_hoadonchitiet_datphong` FOREIGN KEY (`MaDatPhong`) REFERENCES `c3_datphong` (`MaDatPhong`),
  CONSTRAINT `fk_hoadonchitiet_kiemtra` FOREIGN KEY (`MaKiemTra`) REFERENCES `d3_kiemtraphong` (`MaKiemTra`),
  CONSTRAINT `fk_hoadonchitiet_sddv` FOREIGN KEY (`MaSDDV`) REFERENCES `e2_sddv` (`MaSDDV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Hóa đơn chi tiết';

LOCK TABLES `b2_hoadonchitiet` WRITE;
INSERT INTO `b2_hoadonchitiet` VALUES 
('HD001','P101','SDDV001','KT001','DP001',19250000.00),
('HD001','P102','SDDV002','KT002','DP002',16500000.00);
UNLOCK TABLES;

-- Dump completed on 2025-04-23