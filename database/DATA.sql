-- Data for khachhang_01
USE ql_khachsan;
-- INSERT INTO A1_khachhang (MaKhachHang, TenKhachHang, GioiTinh, Sdt, TinhTrangKhach)
-- VALUES
-- ('KH001', 'Nguyễn Văn An', 'Nam', '0987654321', 'Đang ở'),
-- ('KH002', 'Lê Thị Bích', 'Nữ', '0912345678', 'Đã rời'),
-- ('KH003', 'Trần Minh Châu', 'Nam', '0923456789', 'Đang ở'),
-- ('KH004', 'Phạm Thị Duyên', 'Nữ', '0934567890', 'Đang ở'),
-- ('KH005', 'Hoàng Quang Liêm', 'Nam', '0945678901', 'Đã rời'),
-- ('KH006', 'Vũ Thị Mai', 'Nữ', '0956789012', 'Đang ở'),
-- ('KH007', 'Bùi Minh Giang', 'Nam', '0967890123', 'Đang ở'),
-- ('KH008', 'Ngô Thị Hà', 'Nữ', '0978901234', 'Đã rời'),
-- ('KH009', 'Đỗ Quang Hùng', 'Nam', '0989012345', 'Đang ở'),
-- ('KH010', 'Nguyễn Thị Lan', 'Nữ', '0990123456', 'Đang ở'),
-- ('KH011', 'Trần Quang Kiên', 'Nam', '0901234567', 'Đã rời'),
-- ('KH012', 'Lê Minh Linh', 'Nữ', '0912345670', 'Đang ở'),
-- ('KH013', 'Vũ Quang Minh', 'Nam', '0923456780', 'Đang ở'),
-- ('KH014', 'Hoàng Thị Ngọc', 'Nữ', '0934567890', 'Đã rời'),
-- ('KH015', 'Bùi Quang Hạo', 'Nam', '0945678900', 'Đang ở'),
-- ('KH016', 'Phan Minh Phú', 'Nam', '0956789011', 'Đang ở'),
-- ('KH017', 'Trương Quang Quyền', 'Nữ', '0967890122', 'Đã rời'),
-- ('KH018', 'Đào Thị Rạng', 'Nam', '0978901235', 'Đang ở'),
-- ('KH019', 'Dương Quang Sơn', 'Nữ', '0989012346', 'Đang ở'),
-- ('KH020', 'Nguyễn Quang Tình', 'Nam', '0990123457', 'Đã rời');




-- INSERT INTO A14_nhanvien (MaNhanVien, TenNhanVien, Sdt, GioiTinh, ChucVu) VALUES
-- ('NV001', 'Nguyen Thi Lan', '0988123456', 'Nữ', 'Lễ tân'),
-- ('NV002', 'Tran Minh Hoa', '0917654321', 'Nam', 'Quản lý'),
-- ('NV003', 'Pham Ngoc Tu', '0922456789', 'Nữ', 'Dọn phòng'),
-- ('NV004', 'Le Minh Tuan', '0945789012', 'Nam', 'Bảo vệ');



-- INSERT INTO A4_loaiphong (MaLoai, TenLoai, SoGiuong, GiaLoai)
-- VALUES 
-- ('LP001', 'Standard', 1, 3000000),
-- ('LP002', 'Standard', 2, 3500000),
-- ('LP003', 'VIP', 1, 5000000),
-- ('LP004', 'VIP', 2, 6500000),
-- ('LP005', 'Presidential', 1, 10000000),
-- ('LP006', 'Presidential', 2, 12000000);



-- Thêm dữ liệu mới, mỗi tầng chỉ 10 phòng
-- INSERT INTO a5_phong (MaPhong, TenPhong, MaLoai, MoTa, TinhTrangPhong)
-- VALUES
-- ('P301', 'Presidential', 'LP005', 'Phòng Tổng thống 1 giường', NULL),
-- ('P302', 'Presidential', 'LP006', 'Phòng Tổng thống 2 giường', NULL),
-- ('P303', 'Presidential', 'LP005', 'Phòng Tổng thống 1 giường', NULL),
-- ('P304', 'Presidential', 'LP006', 'Phòng Tổng thống 2 giường', NULL),
-- ('P305', 'Presidential', 'LP005', 'Phòng Tổng thống 1 giường', NULL),
-- ('P306', 'Presidential', 'LP006', 'Phòng Tổng thống 2 giường', NULL),
-- ('P307', 'Presidential', 'LP005', 'Phòng Tổng thống 1 giường', NULL);
-- Standard (10 phòng)
-- ('P101', 'Standard', 'LP001', 'Phòng tiêu chuẩn 1 giường'),
-- ('P102', 'Standard', 'LP002', 'Phòng tiêu chuẩn 2 giường'),
-- ('P103', 'Standard', 'LP001', 'Phòng tiêu chuẩn 1 giường'),
-- ('P104', 'Standard', 'LP002', 'Phòng tiêu chuẩn 2 giường'),
-- ('P105', 'Standard', 'LP001', 'Phòng tiêu chuẩn 1 giường'),
-- ('P106', 'Standard', 'LP002', 'Phòng tiêu chuẩn 2 giường'),
-- ('P107', 'Standard', 'LP001', 'Phòng tiêu chuẩn 1 giường'),
-- ('P108', 'Standard', 'LP002', 'Phòng tiêu chuẩn 2 giường'),
-- ('P109', 'Standard', 'LP001', 'Phòng tiêu chuẩn 1 giường'),
-- ('P110', 'Standard', 'LP002', 'Phòng tiêu chuẩn 2 giường'),

-- VIP (10 phòng)
-- ('P201', 'VIP', 'LP003', 'Phòng VIP 1 giường'),
-- ('P202', 'VIP', 'LP004', 'Phòng VIP 2 giường'),
-- ('P203', 'VIP', 'LP003', 'Phòng VIP 1 giường'),
-- ('P204', 'VIP', 'LP004', 'Phòng VIP 2 giường'),
-- ('P205', 'VIP', 'LP003', 'Phòng VIP 1 giường'),
-- ('P206', 'VIP', 'LP004', 'Phòng VIP 2 giường'),
-- ('P207', 'VIP', 'LP003', 'Phòng VIP 1 giường'),
-- ('P208', 'VIP', 'LP004', 'Phòng VIP 2 giường'),
-- ('P209', 'VIP', 'LP003', 'Phòng VIP 1 giường'),
-- ('P210', 'VIP', 'LP004', 'Phòng VIP 2 giường'),

-- Presidential (10 phòng)

-- ('P308', 'Presidential', 'LP006', 'Phòng Tổng thống 2 giường'),
-- ('P309', 'Presidential', 'LP005', 'Phòng Tổng thống 1 giường'),
-- ('P310', 'Presidential', 'LP006', 'Phòng Tổng thống 2 giường');



-- INSERT INTO A6_datphong (MaDatPhong, MaPhong, MaNhanVien, MaKhachHang, NgayNhanPhong, NgayTraPhong)
-- VALUES
--   ('DP005', 'P101', 'NV001', 'KH001', '2025-04-10', '2025-04-13'),
--   ('DP006', 'P102', 'NV002', 'KH002', '2025-04-11', '2025-04-14'),
--   ('DP007', 'P103', 'NV003', 'KH003', '2025-04-12', '2025-04-15'),
--   ('DP008', 'P104', 'NV004', 'KH004', '2025-04-13', '2025-04-16'),
--   ('DP009', 'P201', 'NV001', 'KH002', '2025-04-14', '2025-04-18'),
--   ('DP010', 'P202', 'NV002', 'KH003', '2025-04-15', '2025-04-19'),
--   ('DP011', 'P203', 'NV003', 'KH004', '2025-04-16', '2025-04-20'),
--   ('DP012', 'P204', 'NV004', 'KH001', '2025-04-17', '2025-04-21');


-- INSERT INTO A7_thietbi (MaThietBi, TenThietBi, DonGia)
-- VALUES 
--     ('TB01', 'Giường ngủ đôi 1m6x2m', 4500000),
--     ('TB02', 'Nệm cao su Kim Cương 1m6x2m', 3500000),
--     ('TB03', 'Chăn ga gối nệm đầy đủ bộ', 1200000),
--     ('TB04', 'Tủ quần áo gỗ công nghiệp', 3000000),
--     ('TB05', 'Tivi Samsung 40 inch', 8000000),
--     ('TB06', 'Máy lạnh Daikin 1.5HP Inverter', 9500000),
--     ('TB07', 'Bàn làm việc gỗ ép', 1000000),
--     ('TB08', 'Ghế làm việc có đệm', 600000),
--     ('TB09', 'Đèn ngủ để bàn', 250000),
--     ('TB10', 'Rèm cửa cách nhiệt', 1000000),
--     ('TB11', 'Tủ lạnh mini Aqua 90L', 2500000),
--     ('TB12', 'Ấm đun nước siêu tốc', 350000),
--     ('TB13', 'Ly tách thủy tinh 2 cái', 100000),
--     ('TB14', 'Máy sấy tóc Panasonic mini', 450000),
--     ('TB15', 'Điện thoại bàn nội bộ', 850000),
--     ('TB16', 'Gương soi toàn thân', 700000),
--     ('TB17', 'Kệ để vali', 400000),
--     ('TB18', 'Bình chữa cháy mini', 750000),
--     ('TB19', 'Hộp khăn giấy gỗ', 150000),
--     ('TB20', 'Két sắt mini điện tử', 1800000);
-- ALTER TABLE a7_thietbi;
-- UPDATE A7_thietbi SET DenBu = 4950000 WHERE MaThietBi = 'TB01'; -- +10%
-- UPDATE A7_thietbi SET DenBu = 3850000 WHERE MaThietBi = 'TB02'; -- +10%
-- UPDATE A7_thietbi SET DenBu = 1380000 WHERE MaThietBi = 'TB03'; -- +15%
-- UPDATE A7_thietbi SET DenBu = 3300000 WHERE MaThietBi = 'TB04'; -- +10%
-- UPDATE A7_thietbi SET DenBu = 8800000 WHERE MaThietBi = 'TB05'; -- +10%
-- UPDATE A7_thietbi SET DenBu = 10500000 WHERE MaThietBi = 'TB06'; -- +10%
-- UPDATE A7_thietbi SET DenBu = 1150000 WHERE MaThietBi = 'TB07'; -- +15%
-- UPDATE A7_thietbi SET DenBu = 660000 WHERE MaThietBi = 'TB08'; -- +10%
-- UPDATE A7_thietbi SET DenBu = 300000 WHERE MaThietBi = 'TB09'; -- +20%
-- UPDATE A7_thietbi SET DenBu = 1150000 WHERE MaThietBi = 'TB10'; -- +15%
-- UPDATE A7_thietbi SET DenBu = 2850000 WHERE MaThietBi = 'TB11'; -- +14%
-- UPDATE A7_thietbi SET DenBu = 400000 WHERE MaThietBi = 'TB12'; -- +14%
-- UPDATE A7_thietbi SET DenBu = 120000 WHERE MaThietBi = 'TB13'; -- +20%
-- UPDATE A7_thietbi SET DenBu = 500000 WHERE MaThietBi = 'TB14'; -- +11%
-- UPDATE A7_thietbi SET DenBu = 950000 WHERE MaThietBi = 'TB15'; -- +12%
-- UPDATE A7_thietbi SET DenBu = 800000 WHERE MaThietBi = 'TB16'; -- +14%
-- UPDATE A7_thietbi SET DenBu = 450000 WHERE MaThietBi = 'TB17'; -- +12.5%
-- UPDATE A7_thietbi SET DenBu = 850000 WHERE MaThietBi = 'TB18'; -- +13%
-- UPDATE A7_thietbi SET DenBu = 180000 WHERE MaThietBi = 'TB19'; -- +20%
-- UPDATE A7_thietbi SET DenBu = 2000000 WHERE MaThietBi = 'TB20'; -- +11%



-- INSERT INTO A8_thietbiphong (MaPhong, MaThietBi, SoLuong, TrangThai)
-- VALUES
--     ('P101', 'TB01', 1, 'Tốt'),          -- Giường đôi
--     ('P101', 'TB05', 1, 'Tốt'),          -- Tivi
--     ('P101', 'TB06', 1, 'Tốt'),          -- Máy lạnh
--     ('P101', 'TB11', 1, 'Tốt'),          -- Tủ lạnh mini
--     ('P101', 'TB12', 1, 'Tốt'),          -- Ấm siêu tốca11_dichvu
--     ('P101', 'TB14', 1, 'Tốt'),          -- Máy sấy tóc

--     ('P102', 'TB01', 1, 'Tốt'),
--     ('P102', 'TB05', 1, 'Hư hỏng'),      -- Tivi hỏng
--     ('P102', 'TB06', 1, 'Tốt'),
--     ('P102', 'TB12', 1, 'Tốt'),
--     ('P102', 'TB20', 1, 'Tốt'),          -- Két sắt

--     ('P103', 'TB01', 1, 'Tốt'),
--     ('P103', 'TB05', 1, 'Tốt'),
--     ('P103', 'TB06', 1, 'Tốt'),
--     ('P103', 'TB13', 2, 'Tốt'),          -- Ly tách

--     ('P104', 'TB01', 1, 'Hư hỏng'),      -- Giường hỏng
--     ('P104', 'TB06', 1, 'Tốt'),
--     ('P104', 'TB09', 2, 'Tốt'),          -- Đèn ngủ
--     ('P104', 'TB18', 1, 'Tốt');          -- Bình chữa cháythietbiphong_08



 -- INSERT INTO A11_dichvu (MaDichVu, TenDichVu, LoaiDichVu, GiaDichVu)
-- VALUES 
-- ('DV001', 'Bữa sáng tại phòng', 'Ăn uống', 200000),
-- ('DV002', 'Gọi món ăn tại phòng', 'Ăn uống', 200000),
-- ('DV003', 'Gọi nước uống tại phòng', 'Ăn uống', 100000),
-- ('DV004', 'Giặt áo sơ mi', 'Giặt ủi - Vệ sinh', 50000),
-- ('DV005', 'Giặt quần dài', 'Giặt ủi - Vệ sinh', 50000),
-- ('DV006', 'Ủi quần áo', 'Giặt ủi - Vệ sinh', 100000),
-- ('DV007', 'Dọn phòng theo yêu cầu', 'Giặt ủi - Vệ sinh', 0),
-- ('DV008', 'Gọi taxi sân bay', 'Di chuyển', 400000),
-- ('DV009', 'Đưa đón khách hàng bằng xe riêng', 'Di chuyển', 700000),
-- ('DV010', 'Thuê xe máy', 'Di chuyển', 400000),
-- ('DV011', 'Thuê xe oto', 'Di chuyển', 1500000),
-- ('DV012', 'In tài liệu (1 trang)', 'Văn phòng', 10000),
-- ('DV013', 'Thuê laptop', 'Văn phòng', 200000),
-- ('DV014', 'Dịch vụ spa tại phòng', 'Thư giãn - Thể thao', 800000),
-- ('DV015', 'Massage toàn thân 60 phút', 'Thư giãn - Thể thao', 1500000),
-- ('DV016', 'Phòng tập gym miễn phí', 'Thư giãn - Thể thao', 0),
-- ('DV017', 'Thuê sân tennis', 'Thư giãn - Thể thao', 500000),
-- ('DV018', 'Phòng hội thảo 20 người', 'Sự kiện', 2000000),
-- ('DV019', 'Trang trí sinh nhật/honeymoon', 'Sự kiện', 800000),
-- ('DV020', 'Tổ chức sự kiện (theo giờ)', 'Sự kiện', 10000000); 
-- INSERT INTO A6_datphong (MaDatPhong, MaPhong, MaNhanVien, MaKhachHang, NgayNhanPhong, NgayTraPhong) VALUES
-- ('DP001', 'P101', 'NV001', 'KH001', '2025-04-10', '2025-04-12'),
-- ('DP002', 'P102', 'NV002', 'KH002', '2025-04-09', '2025-04-11'),
-- ('DP003', 'P103', 'NV003', 'KH003', '2025-04-08', '2025-04-10'),
-- ('DP004', 'P104', 'NV004', 'KH004', '2025-04-07', '2025-04-09'),
-- ('DP011', 'P105', 'NV001', 'KH005', '2025-04-10', '2025-04-14'),  
-- ('DP012', 'P106', 'NV002', 'KH006', '2025-04-09', '2025-04-13'),  
-- ('DP013', 'P107', 'NV003', 'KH007', '2025-04-08', '2025-04-12'), 
-- ('DP014', 'P108', 'NV004', 'KH008', '2025-04-07', '2025-04-09'),  
-- ('DP015', 'P109', 'NV001', 'KH009', '2025-04-10', '2025-04-12'),  
-- ('DP016', 'P110', 'NV002', 'KH010', '2025-04-09', '2025-04-11');

-- INSERT INTO A12_sddv(MaSDDV, MaKhachHang, MaNhanVien, NgaySDDV) VALUES
-- ('SDDV001', 'KH001', 'NV001', '2025-04-14'),
-- ('SDDV002', 'KH002', 'NV002', '2025-04-13'),
-- ('SDDV003', 'KH003', 'NV003', '2025-04-12'),
-- ('SDDV004', 'KH004', 'NV004', '2025-04-11'),
-- ('SDDV005', 'KH005', 'NV001', '2025-04-10'),
-- ('SDDV006', 'KH006', 'NV002', '2025-04-09'),
-- ('SDDV007', 'KH007', 'NV003', '2025-04-08'),
-- ('SDDV008', 'KH008', 'NV004', '2025-04-07'),
-- ('SDDV009', 'KH009', 'NV001', '2025-04-06'),
-- ('SDDV010', 'KH010', 'NV002', '2025-04-05');
-- INSERT INTO A13_chitietsddv (MaSDDV, MaDichVu, SoLuongDichVu) VALUES
-- ('SDDV001', 'DV001', 2),
-- ('SDDV002', 'DV002', 1),
-- ('SDDV003', 'DV003', 3),
-- ('SDDV004', 'DV004', 5),
-- ('SDDV005', 'DV005', 1),
-- ('SDDV006', 'DV006', 2),
-- ('SDDV007', 'DV007', 4),
-- ('SDDV008', 'DV008', 1),
-- ('SDDV009', 'DV009', 3),
-- ('SDDV010', 'DV010', 2);

-- INSERT INTO A2_hoadon (MaHoaDon, MaNhanVien, MaKhachHang, Ngay) VALUES
-- ('HD001', 'NV001', 'KH001', '2025-04-15'),
-- ('HD002', 'NV002', 'KH002', '2025-04-14'),
-- ('HD003', 'NV003', 'KH003', '2025-04-13'),
-- ('HD004', 'NV004', 'KH004', '2025-04-12'),
-- ('HD005', 'NV001', 'KH005', '2025-04-15'),
-- ('HD006', 'NV002', 'KH006', '2025-04-14'),
-- ('HD007', 'NV003', 'KH007', '2025-04-13'),
-- ('HD008', 'NV004', 'KH008', '2025-04-12'),
-- ('HD009', 'NV001', 'KH009', '2025-04-15'),
-- ('HD010', 'NV002', 'KH010', '2025-04-14');

-- INSERT INTO A3_hoadonchitiet (MaHoaDon, MaPhong, MaDichVu) VALUES
-- ('HD001', 'P101', 'DV001'),
-- ('HD002', 'P102', 'DV002'),
-- ('HD003', 'P103', 'DV003'),
-- ('HD004', 'P104', 'DV004'),
-- ('HD005', 'P105', 'DV005'),
-- ('HD006', 'P106', 'DV006'),
-- ('HD007', 'P107', 'DV007'),
-- ('HD008', 'P108', 'DV008'),
-- ('HD009', 'P109', 'DV009'),
-- ('HD010', 'P110', 'DV010');
--  
 -- INSERT INTO A9_kiemtraphong(MaKiemTra, MaPhong, MaNhanVien, NgayKiemTra, GhiChu, TrangThai) VALUES
-- ('KT001', 'P101', 'NV001', '2025-04-14', 'Kiểm tra phòng định kỳ', 'Tốt'),
-- ('KT002', 'P102', 'NV002', '2025-04-13', 'Kiểm tra sau khi khách trả phòng', 'Cần dọn'),
-- ('KT003', 'P103', 'NV003', '2025-04-12', 'Phòng kiểm tra trước khi khách nhận phòng', 'Tốt'),
-- ('KT004', 'P104', 'NV004', '2025-04-11', 'Kiểm tra phòng sau khi sửa chữa', 'Hỏng'),
-- ('KT005', 'P105', 'NV001', '2025-04-10', 'Kiểm tra phòng định kỳ', 'Tốt'),
-- ('KT006', 'P106', 'NV002', '2025-04-09', 'Kiểm tra sau khi khách trả phòng', 'Cần dọn'),
-- ('KT007', 'P107', 'NV003', '2025-04-08', 'Phòng kiểm tra trước khi khách nhận phòng', 'Tốt'),
-- ('KT008', 'P108', 'NV004', '2025-04-07', 'Kiểm tra phòng sau khi sửa chữa', 'Hỏng'),
-- ('KT009', 'P109', 'NV001', '2025-04-06', 'Kiểm tra phòng định kỳ', 'Tốt'),
-- ('KT010', 'P110', 'NV002', '2025-04-05', 'Kiểm tra phòng sau khi khách trả phòng', 'Cần dọn');

-- INSERT INTO A10_kiemtrachitiet (MaKiemTra, MaThietBi, TinhTrang, GhiChu) VALUES
-- ('KT001', 'TB01', 'Tốt', 'Kiểm tra thiết bị giường đôi phòng P101'),
-- ('KT002', 'TB05', 'Tốt', 'Kiểm tra thiết bị tivi phòng P101'),
-- ('KT003', 'TB06', 'Tốt', 'Kiểm tra thiết bị máy lạnh phòng P101'),
-- ('KT004', 'TB11', 'Tốt', 'Kiểm tra thiết bị tủ lạnh mini phòng P101'),
-- ('KT005', 'TB12', 'Tốt', 'Kiểm tra thiết bị ấm siêu tốc phòng P101'),
-- ('KT006', 'TB14', 'Tốt', 'Kiểm tra thiết bị máy sấy tóc phòng P101'),

-- ('KT007', 'TB01', 'Tốt', 'Kiểm tra thiết bị giường đôi phòng P102'),
-- ('KT008', 'TB05', 'Hỏng', 'Kiểm tra thiết bị tivi phòng P102'),
-- ('KT009', 'TB06', 'Tốt', 'Kiểm tra thiết bị máy lạnh phòng P102'),
-- ('KT010', 'TB12', 'Tốt', 'Kiểm tra thiết bị ấm siêu tốc phòng P102'),
-- ('KT010', 'TB20', 'Tốt', 'Kiểm tra thiết bị két sắt phòng P102');
-- ALTER TABLE b0_kiemtrachitiet;
-- -- UPDATE b0_kiemtrachitiet SET MaPhong = 'P101' WHERE MaKiemTra = 'KT001';
-- -- UPDATE b0_kiemtrachitiet SET MaPhong = 'P102' WHERE MaKiemTra = 'KT002';
-- -- UPDATE b0_kiemtrachitiet SET MaPhong = 'P103' WHERE MaKiemTra = 'KT003';
-- -- UPDATE b0_kiemtrachitiet SET MaPhong = 'P104' WHERE MaKiemTra = 'KT004';
-- -- UPDATE b0_kiemtrachitiet SET MaPhong = 'P105' WHERE MaKiemTra = 'KT005';
-- -- UPDATE b0_kiemtrachitiet SET MaPhong = 'P106' WHERE MaKiemTra = 'KT006';
-- -- UPDATE b0_kiemtrachitiet SET MaPhong = 'P107' WHERE MaKiemTra = 'KT007';
-- -- UPDATE b0_kiemtrachitiet SET MaPhong = 'P108' WHERE MaKiemTra = 'KT008';
-- -- UPDATE b0_kiemtrachitiet SET MaPhong = 'P109' WHERE MaKiemTra = 'KT009';
-- -- UPDATE b0_kiemtrachitiet SET MaPhong = 'P110' WHERE MaKiemTra = 'KT010';








 






