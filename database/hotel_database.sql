-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ql_khachsan
-- ------------------------------------------------------
-- Server version	8.4.4

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `a1_khachhang`
--
DROP DATABASE IF EXISTS ql_khachsan;

CREATE DATABASE IF NOT EXISTS ql_khachsan;

USE ql_khachsan;

DROP TABLE IF EXISTS `a1_khachhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `a1_khachhang` (
  `MaKhachHang` varchar(50) NOT NULL COMMENT 'Mã khách hàng',
  `TenKhachHang` varchar(100) DEFAULT NULL COMMENT 'Tên khách hàng',
  `GioiTinh` enum('Nam','Nữ') DEFAULT NULL COMMENT 'Giới tính',
  `Sdt` varchar(15) DEFAULT NULL COMMENT 'SĐT',
  `TinhTrangKhach` enum('Đang ở','Đã rời','Đang đặt') COMMENT 'Tình trạng',
  PRIMARY KEY (`MaKhachHang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Khách hàng';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a1_khachhang`
--

LOCK TABLES `a1_khachhang` WRITE;
/*!40000 ALTER TABLE `a1_khachhang` DISABLE KEYS */;
INSERT INTO `a1_khachhang` VALUES ('KH001','Nguyễn Văn An','Nam','0987654321','Đã rời'),('KH002','Lê Thị Bích','Nữ','0912345678','Đã rời'),('KH003','Trần Minh Châu','Nam','0923456789','Đang ở'),('KH004','Phạm Thị Duyên','Nữ','0934567890','Đang ở'),('KH005','Hoàng Quang Liêm','Nam','0945678901',NULL),('KH006','Vũ Thị Mai','Nữ','0956789012',NULL),('KH007','Bùi Minh Giang','Nam','0967890123',NULL),('KH008','Ngô Thị Hà','Nữ','0978901234',NULL),('KH009','Đỗ Quang Hùng','Nam','0989012345',NULL),('KH010','Nguyễn Thị Lan','Nữ','0990123456',NULL),('KH011','Trần Quang Kiên','Nam','0901234567',NULL),('KH012','Lê Minh Linh','Nữ','0912345670',NULL),('KH013','Vũ Quang Minh','Nam','0923456780',NULL),('KH014','Hoàng Thị Ngọc','Nữ','0934567890',NULL),('KH015','Bùi Quang Hạo','Nam','0945678900',NULL),('KH016','Phan Minh Phú','Nam','0956789011',NULL),('KH017','Trương Quang Quyền','Nữ','0967890122',NULL),('KH018','Đào Thị Rạng','Nam','0978901235',NULL),('KH019','Dương Quang Sơn','Nữ','0989012346',NULL),('KH020','Nguyễn Quang Tình','Nam','0990123457',NULL);
/*!40000 ALTER TABLE `a1_khachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `b1_hoadon`
--

DROP TABLE IF EXISTS `b1_hoadon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `b1_hoadon`
--

LOCK TABLES `b1_hoadon` WRITE;
/*!40000 ALTER TABLE `b1_hoadon` DISABLE KEYS */;
INSERT INTO `b1_hoadon` VALUES ('HD001','NV001','KH001','2025-04-15',NULL);
/*!40000 ALTER TABLE `b1_hoadon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `b2_hoadonchitiet`
--

DROP TABLE IF EXISTS `b2_hoadonchitiet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `b2_hoadonchitiet` (
  `MaHoaDon` varchar(50) NOT NULL COMMENT 'Mã hóa đơn',
  `MaPhong` varchar(50) NOT NULL COMMENT 'Mã phòng',
  `MaDichVu` varchar(50) NOT NULL COMMENT 'Mã dịch vụ',
  PRIMARY KEY (`MaHoaDon`,`MaPhong`,`MaDichVu`),
  KEY `MaPhong` (`MaPhong`),
  KEY `MaDichVu` (`MaDichVu`),
  CONSTRAINT `b2_hoadonchitiet_ibfk_1` FOREIGN KEY (`MaHoaDon`) REFERENCES `b1_hoadon` (`MaHoaDon`),
  CONSTRAINT `b2_hoadonchitiet_ibfk_2` FOREIGN KEY (`MaPhong`) REFERENCES `c2_phong` (`MaPhong`),
  CONSTRAINT `b2_hoadonchitiet_ibfk_3` FOREIGN KEY (`MaDichVu`) REFERENCES `e1_dichvu` (`MaDichVu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Hóa đơn chi tiết';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `b2_hoadonchitiet`
--

LOCK TABLES `b2_hoadonchitiet` WRITE;
/*!40000 ALTER TABLE `b2_hoadonchitiet` DISABLE KEYS */;
INSERT INTO `b2_hoadonchitiet` VALUES ('HD001','P101','DV001');
/*!40000 ALTER TABLE `b2_hoadonchitiet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c1_loaiphong`
--

DROP TABLE IF EXISTS `c1_loaiphong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `c1_loaiphong` (
  `MaLoai` varchar(50) NOT NULL COMMENT 'Mã loại phòng',
  `TenLoai` varchar(100) DEFAULT NULL COMMENT 'Tên loại',
  `SoGiuong` varchar(100) DEFAULT NULL COMMENT 'Số giường',
  `GiaLoai` decimal(10,2) DEFAULT NULL COMMENT 'Giá loại phòng',
  PRIMARY KEY (`MaLoai`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Loại phòng';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c1_loaiphong`
--

LOCK TABLES `c1_loaiphong` WRITE;
/*!40000 ALTER TABLE `c1_loaiphong` DISABLE KEYS */;
INSERT INTO `c1_loaiphong` VALUES ('LP001','Standard','1',3000000.00),('LP002','Standard','2',3500000.00),('LP003','VIP','1',5000000.00),('LP004','VIP','2',6500000.00),('LP005','Presidential','1',10000000.00),('LP006','Presidential','2',12000000.00);
/*!40000 ALTER TABLE `c1_loaiphong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c2_phong`
--

DROP TABLE IF EXISTS `c2_phong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c2_phong`
--

LOCK TABLES `c2_phong` WRITE;
/*!40000 ALTER TABLE `c2_phong` DISABLE KEYS */;
INSERT INTO `c2_phong` VALUES ('P101','Standard','LP001','Phòng tiêu chuẩn 1 giường','Trống'),('P102','Standard','LP002','Phòng tiêu chuẩn 2 giường','Trống'),('P103','Standard','LP001','Phòng tiêu chuẩn 1 giường','Đã đặt'),('P104','Standard','LP002','Phòng tiêu chuẩn 2 giường','Đã đặt'),('P105','Standard','LP001','Phòng tiêu chuẩn 1 giường','Trống'),('P106','Standard','LP002','Phòng tiêu chuẩn 2 giường','Trống'),('P107','Standard','LP001','Phòng tiêu chuẩn 1 giường','Trống'),('P108','Standard','LP002','Phòng tiêu chuẩn 2 giường','Trống'),('P109','Standard','LP001','Phòng tiêu chuẩn 1 giường','Trống'),('P110','Standard','LP002','Phòng tiêu chuẩn 2 giường','Trống'),('P111','Standard','LP001','Phòng tiêu chuẩn 1 giường','Trống'),('P112','Standard','LP002','Phòng tiêu chuẩn 2 giường','Trống'),('P201','VIP','LP003','Phòng VIP 1 giường','Trống'),('P202','VIP','LP004','Phòng VIP 2 giường','Trống'),('P203','VIP','LP003','Phòng VIP 1 giường','Trống'),('P204','VIP','LP004','Phòng VIP 2 giường','Trống'),('P205','VIP','LP003','Phòng VIP 1 giường','Trống'),('P206','VIP','LP004','Phòng VIP 2 giường','Trống'),('P207','VIP','LP003','Phòng VIP 1 giường','Trống'),('P208','VIP','LP004','Phòng VIP 2 giường','Trống'),('P209','VIP','LP003','Phòng VIP 1 giường','Trống'),('P210','VIP','LP004','Phòng VIP 2 giường','Trống'),('P211','VIP','LP003','Phòng VIP 1 giường','Trống'),('P212','VIP','LP004','Phòng VIP 2 giường','Trống'),('P213','VIP','LP003','Phòng VIP 1 giường','Trống'),('P214','VIP','LP004','Phòng VIP 2 giường','Trống'),('P215','VIP','LP003','Phòng VIP 1 giường','Trống'),('P301','Presidential','LP005','Phòng Tổng thống 1 giường','Trống'),('P302','Presidential','LP006','Phòng Tổng thống 2 giường','Trống'),('P303','Presidential','LP005','Phòng Tổng thống 1 giường','Trống'),('P304','Presidential','LP006','Phòng Tổng thống 2 giường','Trống'),('P305','Presidential','LP005','Phòng Tổng thống 1 giường','Trống'),('P306','Presidential','LP006','Phòng Tổng thống 2 giường','Trống'),('P307','Presidential','LP005','Phòng Tổng thống 1 giường','Trống'),('P308','Presidential','LP005','Phòng Tổng thống 1 giường','Trống'),('P309','Presidential','LP006','Phòng Tổng thống 2 giường','Trống'),('P310','Presidential','LP006','Phòng Tổng thống 2 giường','Trống');
/*!40000 ALTER TABLE `c2_phong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c3_datphong`
--

DROP TABLE IF EXISTS `c3_datphong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `c3_datphong` (
  `MaDatPhong` varchar(50) NOT NULL COMMENT 'Mã đặt phòng',
  `MaPhong` varchar(50) DEFAULT NULL COMMENT 'Mã phòng',
  `MaNhanVien` varchar(50) DEFAULT NULL COMMENT 'Mã nhân viên',
  `MaKhachHang` varchar(50) DEFAULT NULL COMMENT 'Mã khách hàng',
  `NgayNhanPhong` datetime DEFAULT NULL COMMENT 'Ngày nhận phòng',
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c3_datphong`
--

LOCK TABLES `c3_datphong` WRITE;
/*!40000 ALTER TABLE `c3_datphong` DISABLE KEYS */;
INSERT INTO `c3_datphong` VALUES ('DP001','P101','NV001','KH001','2025-04-10 00:01:22','2025-04-13 06:11:23','Đặt online','Quá hạn',0.00),('DP002','P102','NV002','KH002','2025-04-11 00:00:00','2025-04-14 05:00:00','Đặt online','Đã trả',0.00),('DP003','P103','NV003','KH003','2025-04-19 00:00:00','2025-04-22 08:00:00','Đặt online','Đang đợi',0.00),('DP004','P104','NV004','KH004','2025-04-19 17:00:00','2025-04-20 00:00:00','Đặt online','Quá hạn',0.00);
/*!40000 ALTER TABLE `c3_datphong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d1_thietbi`
--

DROP TABLE IF EXISTS `d1_thietbi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d1_thietbi` (
  `MaThietBi` varchar(50) NOT NULL COMMENT 'Mã thiết bị',
  `TenThietBi` varchar(100) DEFAULT NULL COMMENT 'Tên thiết bị',
  `DonGia` decimal(10,2) DEFAULT NULL COMMENT 'Đơn giá thiết bị',
  `DenBu` decimal(10,2) DEFAULT NULL COMMENT 'Đền bù',
  PRIMARY KEY (`MaThietBi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Thiết bị';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d1_thietbi`
--

LOCK TABLES `d1_thietbi` WRITE;
/*!40000 ALTER TABLE `d1_thietbi` DISABLE KEYS */;
INSERT INTO `d1_thietbi` VALUES ('TB01','Giường ngủ đôi 1m6x2m',4500000.00,4950000.00),('TB02','Nệm cao su Kim Cương 1m6x2m',3500000.00,3850000.00),('TB03','Chăn ga gối nệm đầy đủ bộ',1200000.00,1380000.00),('TB04','Tủ quần áo gỗ công nghiệp',3000000.00,3300000.00),('TB05','Tivi Samsung 40 inch',8000000.00,8800000.00),('TB06','Máy lạnh Daikin 1.5HP Inverter',9500000.00,10500000.00),('TB07','Bàn làm việc gỗ ép',1000000.00,1150000.00),('TB08','Ghế làm việc có đệm',600000.00,660000.00),('TB09','Đèn ngủ để bàn',250000.00,300000.00),('TB10','Rèm cửa cách nhiệt',1000000.00,1150000.00),('TB11','Tủ lạnh mini Aqua 90L',2500000.00,2850000.00),('TB12','Ấm đun nước siêu tốc',350000.00,400000.00),('TB13','Ly tách thủy tinh 2 cái',100000.00,120000.00),('TB14','Máy sấy tóc Panasonic mini',450000.00,500000.00),('TB15','Điện thoại bàn nội bộ',850000.00,950000.00),('TB16','Gương soi toàn thân',700000.00,800000.00),('TB17','Kệ để vali',400000.00,450000.00),('TB18','Bình chữa cháy mini',750000.00,850000.00),('TB19','Hộp khăn giấy gỗ',150000.00,180000.00),('TB20','Két sắt mini điện tử',1800000.00,2000000.00);
/*!40000 ALTER TABLE `d1_thietbi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d2_thietbiphong`
--

DROP TABLE IF EXISTS `d2_thietbiphong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d2_thietbiphong`
--

LOCK TABLES `d2_thietbiphong` WRITE;
/*!40000 ALTER TABLE `d2_thietbiphong` DISABLE KEYS */;
INSERT INTO `d2_thietbiphong` VALUES ('P101','TB01',1,'Tốt'),('P101','TB05',1,'Tốt'),('P101','TB06',1,'Tốt'),('P101','TB11',1,'Tốt'),('P101','TB12',1,'Tốt'),('P101','TB14',1,'Tốt'),('P102','TB01',1,'Tốt'),('P102','TB05',1,'Hư hỏng'),('P102','TB06',1,'Tốt'),('P102','TB12',1,'Tốt'),('P102','TB20',1,'Tốt'),('P103','TB01',1,'Tốt'),('P103','TB05',1,'Tốt'),('P103','TB06',1,'Tốt'),('P103','TB13',2,'Tốt'),('P104','TB01',1,'Hư hỏng'),('P104','TB06',1,'Tốt'),('P104','TB09',2,'Tốt'),('P104','TB18',1,'Tốt');
/*!40000 ALTER TABLE `d2_thietbiphong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d3_kiemtraphong`
--

DROP TABLE IF EXISTS `d3_kiemtraphong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d3_kiemtraphong` (
  `MaKiemTra` varchar(50) NOT NULL COMMENT 'Mã kiểm tra phòng',
  `MaPhong` varchar(50) DEFAULT NULL COMMENT 'Mã phòng',
  `MaNhanVien` varchar(50) DEFAULT NULL COMMENT 'Mã nhân viên',
  `NgayKiemTra` datetime DEFAULT NULL COMMENT 'Ngày kiểm tra',
  `GhiChu` varchar(255) DEFAULT NULL COMMENT 'Ghi chú',
  `TrangThai` enum('Tốt','Cần dọn','Hỏng') DEFAULT NULL COMMENT 'Tình trạng phòng',
  PRIMARY KEY (`MaKiemTra`),
  KEY `MaPhong` (`MaPhong`),
  KEY `MaNhanVien` (`MaNhanVien`),
  CONSTRAINT `d3_kiemtraphong_ibfk_1` FOREIGN KEY (`MaPhong`) REFERENCES `c2_phong` (`MaPhong`),
  CONSTRAINT `d3_kiemtraphong_ibfk_2` FOREIGN KEY (`MaNhanVien`) REFERENCES `f1_nhanvien` (`MaNhanVien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Kiểm tra phòng';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d3_kiemtraphong`
--

LOCK TABLES `d3_kiemtraphong` WRITE;
/*!40000 ALTER TABLE `d3_kiemtraphong` DISABLE KEYS */;
INSERT INTO `d3_kiemtraphong` VALUES ('KT001','P101','NV001','2025-04-14 11:01:00','Kiểm tra phòng định kỳ','Tốt');
/*!40000 ALTER TABLE `d3_kiemtraphong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d4_kiemtrachitiet`
--

DROP TABLE IF EXISTS `d4_kiemtrachitiet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `d4_kiemtrachitiet` (
  `MaKiemTra` varchar(50) NOT NULL COMMENT 'Mã kiểm tra',
  `MaPhong` varchar(50) DEFAULT NULL COMMENT 'Mã phòng',
  `MaThietBi` varchar(50) NOT NULL COMMENT 'Mã thiết bị',
  `TinhTrang` enum('Tốt','Hỏng') DEFAULT NULL COMMENT 'Tình trạng thiết bị',
  `DenBu` decimal(10,2) DEFAULT NULL COMMENT 'Đền bù',
  `GhiChu` varchar(255) DEFAULT NULL COMMENT 'Ghi chú',
  PRIMARY KEY (`MaKiemTra`,`MaThietBi`),
  KEY `MaThietBi` (`MaThietBi`),
  KEY `fk_kiemtra_thietbiphong` (`MaPhong`,`MaThietBi`),
  CONSTRAINT `d4_kiemtrachitiet_ibfk_1` FOREIGN KEY (`MaKiemTra`) REFERENCES `d3_kiemtraphong` (`MaKiemTra`),
  CONSTRAINT `fk_kiemtra_thietbiphong` FOREIGN KEY (`MaPhong`, `MaThietBi`) REFERENCES `d2_thietbiphong` (`MaPhong`, `MaThietBi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Kiểm tra chi tiết';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d4_kiemtrachitiet`
--

LOCK TABLES `d4_kiemtrachitiet` WRITE;
/*!40000 ALTER TABLE `d4_kiemtrachitiet` DISABLE KEYS */;
INSERT INTO `d4_kiemtrachitiet` VALUES ('KT001','P101','TB01','Tốt',0.00,'Kiểm tra thiết bị giường đôi phòng P101');
/*!40000 ALTER TABLE `d4_kiemtrachitiet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `e1_dichvu`
--

DROP TABLE IF EXISTS `e1_dichvu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `e1_dichvu` (
  `MaDichVu` varchar(50) NOT NULL COMMENT 'Mã dịch vụ',
  `TenDichVu` varchar(255) DEFAULT NULL COMMENT 'Tên dịch vụ',
  `LoaiDichVu` varchar(100) DEFAULT NULL COMMENT 'Loại dịch vụ',
  `GiaDichVu` decimal(10,2) DEFAULT NULL COMMENT 'Giá dịch vụ',
  PRIMARY KEY (`MaDichVu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Dịch vụ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `e1_dichvu`
--

LOCK TABLES `e1_dichvu` WRITE;
/*!40000 ALTER TABLE `e1_dichvu` DISABLE KEYS */;
INSERT INTO `e1_dichvu` VALUES ('DV001','Bữa sáng tại phòng','Ăn uống',200000.00),('DV002','Gọi món ăn tại phòng','Ăn uống',200000.00),('DV003','Gọi nước uống tại phòng','Ăn uống',100000.00),('DV004','Giặt áo sơ mi','Giặt ủi - Vệ sinh',50000.00),('DV005','Giặt quần dài','Giặt ủi - Vệ sinh',50000.00),('DV006','Ủi quần áo','Giặt ủi - Vệ sinh',100000.00),('DV007','Dọn phòng theo yêu cầu','Giặt ủi - Vệ sinh',0.00),('DV008','Gọi taxi sân bay','Di chuyển',400000.00),('DV009','Đưa đón khách hàng bằng xe riêng','Di chuyển',700000.00),('DV010','Thuê xe máy','Di chuyển',400000.00),('DV011','Thuê xe oto','Di chuyển',1500000.00),('DV012','In tài liệu (1 trang)','Văn phòng',10000.00),('DV013','Thuê laptop','Văn phòng',200000.00),('DV014','Dịch vụ spa tại phòng','Thư giãn - Thể thao',800000.00),('DV015','Massage toàn thân 60 phút','Thư giãn - Thể thao',1500000.00),('DV016','Phòng tập gym miễn phí','Thư giãn - Thể thao',0.00),('DV017','Thuê sân tennis','Thư giãn - Thể thao',500000.00),('DV018','Phòng hội thảo 20 người','Sự kiện',2000000.00),('DV019','Trang trí sinh nhật/honeymoon','Sự kiện',800000.00),('DV020','Tổ chức sự kiện (theo giờ)','Sự kiện',10000000.00);
/*!40000 ALTER TABLE `e1_dichvu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `e2_sddv`
--

DROP TABLE IF EXISTS `e2_sddv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `e2_sddv` (
  `MaSDDV` varchar(50) NOT NULL COMMENT 'Mã sử dụng dịch vụ',
  `MaKhachHang` varchar(50) DEFAULT NULL COMMENT 'Mã khách hàng',
  `MaNhanVien` varchar(50) DEFAULT NULL COMMENT 'Mã nhân viên',
  `NgaySDDV` date DEFAULT NULL COMMENT 'Ngày sử dụng dịch vụ',
  PRIMARY KEY (`MaSDDV`),
  KEY `MaKhachHang` (`MaKhachHang`),
  KEY `MaNhanVien` (`MaNhanVien`),
  CONSTRAINT `e2_sddv_ibfk_1` FOREIGN KEY (`MaKhachHang`) REFERENCES `a1_khachhang` (`MaKhachHang`),
  CONSTRAINT `e2_sddv_ibfk_2` FOREIGN KEY (`MaNhanVien`) REFERENCES `f1_nhanvien` (`MaNhanVien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Sử dụng dịch vụ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `e2_sddv`
--

LOCK TABLES `e2_sddv` WRITE;
/*!40000 ALTER TABLE `e2_sddv` DISABLE KEYS */;
INSERT INTO `e2_sddv` VALUES ('SDDV001','KH001','NV001','2025-04-14');
/*!40000 ALTER TABLE `e2_sddv` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `e3_chitietsddv`
--

DROP TABLE IF EXISTS `e3_chitietsddv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `e3_chitietsddv` (
  `MaSDDV` varchar(50) NOT NULL COMMENT 'Mã sử dụng dịch vụ',
  `MaDichVu` varchar(50) NOT NULL COMMENT 'Mã dịch vụ',
  `SoLuongDichVu` int DEFAULT NULL COMMENT 'Số lượng dịch vụ sử dụng',
  PRIMARY KEY (`MaSDDV`,`MaDichVu`),
  KEY `MaDichVu` (`MaDichVu`),
  CONSTRAINT `e3_chitietsddv_ibfk_1` FOREIGN KEY (`MaSDDV`) REFERENCES `e2_sddv` (`MaSDDV`),
  CONSTRAINT `e3_chitietsddv_ibfk_2` FOREIGN KEY (`MaDichVu`) REFERENCES `e1_dichvu` (`MaDichVu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Sử dụng dịch vụ chi tiết';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `e3_chitietsddv`
--

LOCK TABLES `e3_chitietsddv` WRITE;
/*!40000 ALTER TABLE `e3_chitietsddv` DISABLE KEYS */;
INSERT INTO `e3_chitietsddv` VALUES ('SDDV001','DV001',2);
/*!40000 ALTER TABLE `e3_chitietsddv` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `f1_nhanvien`
--

DROP TABLE IF EXISTS `f1_nhanvien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `f1_nhanvien` (
  `MaNhanVien` varchar(50) NOT NULL COMMENT 'Mã nhân viên',
  `TenNhanVien` varchar(100) DEFAULT NULL COMMENT 'Tên nhân viên',
  `Sdt` varchar(15) DEFAULT NULL COMMENT 'Số điện thoại',
  `GioiTinh` enum('Nam','Nữ') DEFAULT NULL COMMENT 'Giới tính',
  `ChucVu` varchar(50) DEFAULT NULL COMMENT 'Chức vụ',
  PRIMARY KEY (`MaNhanVien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Nhân viên';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f1_nhanvien`
--

LOCK TABLES `f1_nhanvien` WRITE;
/*!40000 ALTER TABLE `f1_nhanvien` DISABLE KEYS */;
INSERT INTO `f1_nhanvien` VALUES ('NV001','Nguyen Thi Lan','0988123456','Nữ','Lễ tân'),('NV002','Tran Minh Hoa','0917654321','Nam','Quản lý'),('NV003','Pham Ngoc Tu','0922456789','Nữ','Dọn phòng'),('NV004','Le Minh Tuan','0945789012','Nam','Bảo vệ');
/*!40000 ALTER TABLE `f1_nhanvien` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-19 15:27:59
