DROP DATABASE IF EXISTS authority;

CREATE DATABASE IF NOT EXISTS authority;
USE authority;

# AUTHORS CREATE
DROP TABLE IF EXISTS authors;
CREATE TABLE authors (
    Id CHAR(2) PRIMARY KEY,
    Author VARCHAR(100) NOT NULL
) COMMENT = "Quyền hạn";
INSERT INTO authors (Id, Author) VALUES
('00', 'Xem'),
('10', 'Thêm'),
('11', 'Chỉ Sửa'),
('20', 'Thêm sửa'),
('30', 'Thêm Sửa Xóa');



# MANAGEMENT CREATE
DROP TABLE IF EXISTS manage;
CREATE TABLE manage (
    TableName VARCHAR(100) PRIMARY KEY,
    TableCmt TEXT,
    Beginer CHAR(2),
    TableGroup CHAR(20) 
) COMMENT = "Quản lý";
INSERT INTO manage (TableName, TableCmt, Beginer, TableGroup)
SELECT 
    table_name AS TableName,
    table_comment AS TableCmt,
    LEFT(table_name, 2) AS Beginer,
    LEFT(table_name, 1) AS TableGroup
FROM information_schema.tables
WHERE table_schema = 'ql_khachsan';

# USER CREATE
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    UserName VARCHAR(50) PRIMARY KEY,
    Password VARCHAR(100) NOT NULL,
    MaNhanVien VARCHAR(20),
    TenNhanVien VARCHAR(100),
    `Group` VARCHAR(20) CHECK (`Group` IN ('ADMIN', 'ROOM', 'SERVICE', 'CHECK'))
) COMMENT = "Nhân viên";

INSERT INTO users (UserName, Password, MaNhanVien, TenNhanVien, `Group`) VALUES
('ADMIN', 'ADMIN', 'ADMIN', 'ADMIN', 'ADMIN'),
('RM001', 'RM001', 'RM001', 'Trần Thị Bình', 'ROOM'),
('RM002', 'RM002', 'RM002', 'Lê Quốc Dũng', 'ROOM'),
('RM003', 'RM003', 'RM003', 'Phạm Thị Hòa', 'ROOM'),
('SV001', 'SV001', 'SV001', 'Ngô Minh Nhật', 'SERVICE'),
('SV002', 'SV002', 'SV002', 'Vũ Thị Hạnh', 'SERVICE'),
('SV003', 'SV003', 'SV003', 'Đặng Thanh Tùng', 'SERVICE'),
('CK001', 'CK001', 'CK001', 'Bùi Văn Kiên', 'CHECK'),
('CK002', 'CK002', 'CK002', 'Đỗ Thị Mai', 'CHECK'),
('CK003', 'CK003', 'CK003', 'Hoàng Tuấn Anh', 'CHECK'),
('1', '1', 'Anonymous', 'Anonymous', 'ADMIN');

# GROUP CREATE
DROP TABLE IF EXISTS `group`;

CREATE TABLE `group` (
    `Group` VARCHAR(20) PRIMARY KEY,
    `a` CHAR(2) CHECK (`a` IN ('00', '10', '11', '20', '30')),
    `b` CHAR(2) CHECK (`b` IN ('00', '10', '11', '20', '30')),
    `c` CHAR(2) CHECK (`c` IN ('00', '10', '11', '20', '30')),
    `d` CHAR(2) CHECK (`d` IN ('00', '10', '11', '20', '30')),
    `e` CHAR(2) CHECK (`e` IN ('00', '10', '11', '20', '30')),
    `f` CHAR(2) CHECK (`f` IN ('00', '10', '11', '20', '30'))
) COMMENT = "Phân quyền";

INSERT INTO `group` 
(`Group`  , `a`,  `b`,  `c`,  `d`,  `e`,  `f`) VALUES
('ADMIN'  , '30', '30', '30', '30', '30', '30'),
('ROOM'   , '10', '10', '30', '00', '00', '00'),
('CHECK'  , '10', '10', '00', '30', '00', '00'),
('SERVICE', '10', '10', '00', '00', '30', '00');

-- CREATE TABLE `group` (
--     `Group` VARCHAR(20) PRIMARY KEY,
--     `a1` CHAR(2) CHECK (`a1` IN ('00', '10', '11', '20', '30')),
--     `b1` CHAR(2) CHECK (`b1` IN ('00', '10', '11', '20', '30')),
--     `b2` CHAR(2) CHECK (`b2` IN ('00', '10', '11', '20', '30')),
--     `c1` CHAR(2) CHECK (`c1` IN ('00', '10', '11', '20', '30')),
--     `c2` CHAR(2) CHECK (`c2` IN ('00', '10', '11', '20', '30')),
--     `c3` CHAR(2) CHECK (`c3` IN ('00', '10', '11', '20', '30')),
--     `d1` CHAR(2) CHECK (`d1` IN ('00', '10', '11', '20', '30')),
--     `d2` CHAR(2) CHECK (`d2` IN ('00', '10', '11', '20', '30')),
--     `d3` CHAR(2) CHECK (`d3` IN ('00', '10', '11', '20', '30')),
--     `d4` CHAR(2) CHECK (`d4` IN ('00', '10', '11', '20', '30')),
--     `e1` CHAR(2) CHECK (`e1` IN ('00', '10', '11', '20', '30')),
--     `e2` CHAR(2) CHECK (`e2` IN ('00', '10', '11', '20', '30')),
--     `e3` CHAR(2) CHECK (`e3` IN ('00', '10', '11', '20', '30')),
--     `f1` CHAR(2) CHECK (`f1` IN ('00', '10', '11', '20', '30'))
-- ) COMMENT = "Phân quyền";

-- INSERT INTO `group` 
-- (`Group`, `a1`, `b1`, `b2`, `c1`, `c2`, `c3`, `d1`, `d2`, `d3`, `d4`, `e1`, `e2`, `e3`, `f1`) VALUES
-- ('ADMIN'  , '30', '30', '30', '30', '30', '30', '30', '30', '30', '30', '30', '30', '30', '30'),
-- ('ROOM'   , '10', '10', '10', '30', '30', '30', '00', '00', '00', '00', '00', '00', '00', '00'),
-- ('CHECK'  , '10', '10', '10', '00', '00', '00', '30', '30', '30', '30', '00', '00', '00', '00'),
-- ('SERVICE', '10', '10', '10', '00', '00', '00', '00', '00', '00', '00', '30', '30', '30', '00');





