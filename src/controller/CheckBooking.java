package controller;

import java.sql.*;
import java.time.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import model.ApiClient;
import model.ApiClient.TableDataResult;
import model.TableDataOperationsClient;
import view.MainRegion.ContentPanel;
import view.MainRegion.TablePanel;

public class CheckBooking {
    private final TableDataOperationsClient client;
    private final ContentPanel contentPanel;
    private final TablePanel tablePanel;

    public CheckBooking(String authToken, ContentPanel contentPanel, TablePanel tablePanel) {
        this.client = new TableDataOperationsClient(authToken);
        this.contentPanel = contentPanel;
        this.tablePanel = tablePanel;
    }

    // Hàm chính: Tự động xử lý và cập nhật trạng thái đặt phòng
    public void autoProcessBooking(String tableName, String keyColumn, String keyValue) {
        try {
            // Cập nhật trạng thái trước
            updatePhongTheoTinhTrang();
            
            // Lấy dữ liệu mới từ server để cập nhật giao diện
            TableDataResult result = ApiClient.getTableData(tableName);
            if (result == null || result.data == null) {
                JOptionPane.showMessageDialog(null, "Không thể lấy dữ liệu từ server", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Cập nhật giao diện
            contentPanel.updateTableData(result.data, result.columnComments, result.columnTypes, keyColumn, tableName, "Thông tin đặt phòng");
            tablePanel.updateTableData(result.data, result.columnComments, result.columnTypes, keyColumn, tableName, "Thông tin đặt phòng");
            tablePanel.refreshTable();
            System.out.println("✔ Đã cập nhật trạng thái đặt phòng");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi xử lý đặt phòng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Hàm cốt lõi: Xử lý logic cập nhật trạng thái phòng, khách, và đặt phòng
    private void updatePhongTheoTinhTrang() throws Exception {
        try (Connection conn = DatabaseUtil.getConnection()) {
            Set<String> phongDaXuLy = new HashSet<>();
            Set<String> khachDat = new HashSet<>();

            String sql = """
                SELECT dp.MaDatPhong, dp.MaPhong, dp.NgayNhanPhong, dp.NgayTraPhong, dp.NgayHen,
                       dp.CachDat, dp.TinhTrang, p.TinhTrangPhong, 
                       kh.MaKhachHang, kh.TinhTrangKhach
                FROM c3_datphong dp
                JOIN c2_phong p ON dp.MaPhong = p.MaPhong
                JOIN a1_khachhang kh ON dp.MaKhachHang = kh.MaKhachHang
                WHERE dp.TinhTrang IN ('Đang sử dụng', 'Quá hạn', 'Đang đợi', 'Đã trả');
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Đang xử lý đặt phòng: " + rs.getString("MaDatPhong"));
                    String maDatPhong = rs.getString("MaDatPhong");
                    String maPhong = rs.getString("MaPhong");
                    String maKhachHang = rs.getString("MaKhachHang");
                    String tinhTrangDat = rs.getString("TinhTrang");
                    String cachDat = rs.getString("CachDat");

                    // Kiểm tra giá trị null cho các cột thời gian
                    Timestamp nhanPhongTimestamp = rs.getTimestamp("NgayNhanPhong");
                    Timestamp traPhongTimestamp = rs.getTimestamp("NgayTraPhong");
                    Timestamp henTraTimestamp = rs.getTimestamp("NgayHen");

                    if (nhanPhongTimestamp == null || traPhongTimestamp == null || henTraTimestamp == null) {
                        JOptionPane.showMessageDialog(null, 
                            "Đặt phòng không hợp lệ (phòng " + maPhong + "): Một hoặc nhiều cột thời gian (NgayNhanPhong, NgayTraPhong, NgayHen) là NULL.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    LocalDateTime nhanPhong = nhanPhongTimestamp.toLocalDateTime();
                    LocalDateTime traPhong = traPhongTimestamp.toLocalDateTime();
                    LocalDateTime henTra = henTraTimestamp.toLocalDateTime();

                    // Kiểm tra tính hợp lệ thời gian
                    if (!isValidBooking(nhanPhong, traPhong, henTra)) {
                        JOptionPane.showMessageDialog(null, 
                            "Đặt phòng không hợp lệ (phòng " + maPhong + "): Ngày trả phòng phải sau ngày nhận phòng và trước hạn trả.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    // Kiểm tra xung đột đặt phòng
                    if (hasBookingConflict(conn, maPhong, nhanPhong, traPhong, maDatPhong)) {
                        JOptionPane.showMessageDialog(null, "Xung đột đặt phòng cho phòng " + maPhong, "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    // Tính tiền phòng (dùng để tính tiền phạt)
                    double tienPhong = calculateRoomPrice(conn, maPhong, nhanPhong, traPhong);
                    double tienPhat = calculateLateFee(tienPhong, traPhong, henTra);

                    String newTinhTrangPhong = "Trống";
                    String newTinhTrangKhach = "Đã rời";
                    String newTinhTrangDat = tinhTrangDat;

                    // Logic đặt trực tiếp
                    if ("Đặt trực tiếp".equalsIgnoreCase(cachDat)) {
                        System.out.println("Đặt trực tiếp");
                        switch (tinhTrangDat) {
                            case "Đang sử dụng":
                                newTinhTrangPhong = "Đang sử dụng";
                                newTinhTrangKhach = "Đang ở";
                                break;
                            case "Quá hạn":
                                if (traPhong.isAfter(henTra.plusMinutes(30))) {
                                    newTinhTrangPhong = "Đang sử dụng";
                                    newTinhTrangKhach = "Đang ở";
                                    updateTienPhat(conn, maDatPhong, tienPhat);
                                    System.out.println("Đã tính tiền phạt cho đặt phòng " + maDatPhong + " với số tiền: " + tienPhat);
                                } else {
                                    newTinhTrangPhong = "Đang sử dụng";
                                    newTinhTrangKhach = "Đang ở";
                                }
                                break;
                            case "Đang đợi":
                                JOptionPane.showMessageDialog(null, 
                                    "⚠ Đặt trực tiếp không thể có trạng thái 'Đang đợi' (phòng " + maPhong + ")", 
                                    "Lỗi", JOptionPane.WARNING_MESSAGE);
                                continue;
                            case "Đã trả":
                                newTinhTrangPhong = "Trống";
                                newTinhTrangKhach = "Đã rời";
                                break;
                            default:
                                System.out.println("Trạng thái không xác định: " + tinhTrangDat);
                        }
                    }

                    // Logic đặt online
                    if ("Đặt online".equalsIgnoreCase(cachDat)) {
                        switch (tinhTrangDat) {
                            case "Đang đợi":
                                LocalDateTime thoiGianHuy = nhanPhong.minusHours(5);
                                if (traPhong.isBefore(thoiGianHuy)) {
                                    newTinhTrangPhong = "Trống";
                                    newTinhTrangKhach = "Đã rời";
                                } else if (traPhong.isBefore(nhanPhong)) {
                                    newTinhTrangPhong = "Đã đặt";
                                    newTinhTrangKhach = "Đã đặt";
                                } else if (traPhong.isAfter(nhanPhong) && traPhong.isBefore(henTra)) {
                                    newTinhTrangDat = "Đang sử dụng";
                                    newTinhTrangPhong = "Đang sử dụng";
                                    newTinhTrangKhach = "Đang ở";
                                } else if (traPhong.isAfter(henTra)) {
                                    newTinhTrangDat = "Quá hạn";
                                    newTinhTrangPhong = "Trống";
                                    newTinhTrangKhach = "Đã rời";
                                    updateTienPhat(conn, maDatPhong, tienPhat);
                                }
                                break;
                            case "Đang sử dụng":
                                if (traPhong.isAfter(henTra)) {
                                    newTinhTrangDat = "Quá hạn";
                                    newTinhTrangPhong = "Trống";
                                    newTinhTrangKhach = "Đã rời";
                                    updateTienPhat(conn, maDatPhong, tienPhat);
                                } else {
                                    newTinhTrangPhong = "Đang sử dụng";
                                    newTinhTrangKhach = "Đang ở";
                                }
                                break;
                            case "Quá hạn":
                                newTinhTrangPhong = "Trống";
                                newTinhTrangKhach = "Đã rời";
                                updateTienPhat(conn, maDatPhong, tienPhat);
                                break;
                            case "Đã trả":
                                newTinhTrangPhong = "Trống";
                                newTinhTrangKhach = "Đã rời";
                                break;
                        }
                    }

                    // Cập nhật trạng thái
                    updatePhongStatus(conn, maPhong, newTinhTrangPhong);
                    updateKhachStatus(conn, maKhachHang, newTinhTrangKhach);
                    updateDatPhongStatus(conn, maDatPhong, newTinhTrangDat);

                    phongDaXuLy.add(maPhong);
                    khachDat.add(maKhachHang);
                }
            }

            // Cập nhật trạng thái mặc định cho phòng không được xử lý
            try (PreparedStatement ps = conn.prepareStatement("SELECT MaPhong FROM c2_phong");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String maPhong = rs.getString("MaPhong");
                    if (!phongDaXuLy.contains(maPhong)) {
                        updatePhongStatus(conn, maPhong, "Trống");
                    }
                }
            }

            // Cập nhật trạng thái mặc định cho khách không có đặt phòng
            try (PreparedStatement ps = conn.prepareStatement("SELECT MaKhachHang FROM a1_khachhang");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String maKhach = rs.getString("MaKhachHang");
                    if (!khachDat.contains(maKhach)) {
                        updateKhachStatus(conn, maKhach, "Đã rời");
                    }
                }
            }
            //Cập nhật lại data datphong 
            TableDataResult result = ApiClient.getTableData("c3_datphong");
            if (result == null || result.data == null) {
                JOptionPane.showMessageDialog(null, "Không thể lấy dữ liệu từ server", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    // Kiểm tra tính hợp lệ của thời gian đặt phòng
    private boolean isValidBooking(LocalDateTime nhanPhong, LocalDateTime traPhong, LocalDateTime henTra) {
        return nhanPhong.isBefore(henTra) && traPhong.isAfter(nhanPhong);
    }

    // Kiểm tra xung đột đặt phòng
    private boolean hasBookingConflict(Connection conn, String maPhong, LocalDateTime nhanPhong, LocalDateTime traPhong, String maDatPhong) throws SQLException {
        String sql = """
            SELECT MaDatPhong
            FROM c3_datphong
            WHERE MaPhong = ? AND TinhTrang NOT IN ('Đã trả')
            AND (NgayNhanPhong <= ? AND NgayHen >= ?)
            AND MaDatPhong != ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            ps.setTimestamp(2, Timestamp.valueOf(traPhong));
            ps.setTimestamp(3, Timestamp.valueOf(nhanPhong));
            ps.setString(4, maDatPhong);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Tính tiền phòng (dùng để tính tiền phạt)
    private double calculateRoomPrice(Connection conn, String maPhong, LocalDateTime nhanPhong, LocalDateTime traPhong) throws SQLException {
        String sql = """
            SELECT lp.GiaLoai
            FROM c2_phong p
            JOIN c1_loaiphong lp ON p.MaLoai = lp.MaLoai
            WHERE p.MaPhong = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double giaLoai = rs.getDouble("GiaLoai");
                    // long hoursUsed = Duration.between(nhanPhong, traPhong).toHours();
                    return giaLoai;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "⚠ Không tìm thấy giá loại phòng cho phòng " + maPhong, "Lỗi", JOptionPane.WARNING_MESSAGE);
        return 0;
    }

    // Tính tiền phạt
    private double calculateLateFee(double tienPhong, LocalDateTime traPhong, LocalDateTime henTra) {
        LocalDateTime chophep = henTra.plusMinutes(30);
        if (traPhong.isAfter(chophep)) {
            long minutesLate = Duration.between(chophep, traPhong).toMinutes();
            long hoursLate = (minutesLate + 59) / 60; // Làm tròn lên
            return tienPhong * 0.3 * hoursLate; // Tiền phạt = 30% tiền phòng * số giờ trễ
        }
        return 0; // Trả đúng giờ hoặc sớm: Tiền phạt = 0
    }

    // Cập nhật tiền phạt
    private void updateTienPhat(Connection conn, String maDatPhong, double tienPhat) throws SQLException {
        String sql = "UPDATE c3_datphong SET TienPhat = ? WHERE MaDatPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, tienPhat);
            ps.setString(2, maDatPhong);
            ps.executeUpdate();
        }
    }

    // Cập nhật trạng thái phòng
    private void updatePhongStatus(Connection conn, String maPhong, String tinhTrangPhong) throws SQLException {
        String sql = "UPDATE c2_phong SET TinhTrangPhong = ? WHERE MaPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tinhTrangPhong);
            ps.setString(2, maPhong);
            ps.executeUpdate();
        }
    }

    // Cập nhật trạng thái khách
    private void updateKhachStatus(Connection conn, String maKhachHang, String tinhTrangKhach) throws SQLException {
        String sql = "UPDATE a1_khachhang SET TinhTrangKhach = ? WHERE MaKhachHang = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tinhTrangKhach);
            ps.setString(2, maKhachHang);
            ps.executeUpdate();
        }
    }

    // Cập nhật trạng thái đặt phòng
    private void updateDatPhongStatus(Connection conn, String maDatPhong, String tinhTrangDat) throws SQLException {
        String sql = "UPDATE c3_datphong SET TinhTrang = ? WHERE MaDatPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tinhTrangDat);
            ps.setString(2, maDatPhong);
            ps.executeUpdate();
        }
    }
}