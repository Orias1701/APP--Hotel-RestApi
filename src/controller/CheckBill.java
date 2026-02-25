package controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import model.ApiClient;
import model.ApiClient.ApiResponse;
import model.TableDataOperationsClient;
import view.MainRegion.ContentPanel;
import view.MainRegion.TablePanel;

public class CheckBill {

    private final TableDataOperationsClient client;
    private ContentPanel contentPanel; // Tham chiếu đến ContentPanel để cập nhật dữ liệu
    private TablePanel tablePanel; // Tham chiếu đến TablePanel để làm mới bảng

    // Constructor để khởi tạo với authToken, contentPanel và tablePanel
    public CheckBill(String authToken, ContentPanel contentPanel, TablePanel tablePanel) {
        this.client = new TableDataOperationsClient(authToken);
        this.contentPanel = contentPanel;
        this.tablePanel = tablePanel;
    }

    /**
     * Xử lý chi tiết hóa đơn và tính lại TongTien dựa trên loại chi phí.
     *
     * @param tableName Tên bảng (b2_hoadonchitiet)
     * @param keyColumn Tên cột khóa (MaHoaDon)
     * @param keyValue Giá trị khóa (MaHoaDon)
     * @return ApiResponse Kết quả xử lý
     */
    public ApiResponse processCheckBill(String tableName, String keyColumn, String keyValue) {
        ApiResponse res;

        try {
            // Kiểm tra dữ liệu trước khi xử lý
            if (!isDataValid(tableName, keyColumn, keyValue)) {
                showErrorDialog("❌ Dữ liệu không hợp lệ hoặc không tồn tại.");
                return new ApiResponse(false, "Dữ liệu không hợp lệ");
            }

            if (keyValue == null || keyValue.trim().isEmpty()) {
                // Nếu không có keyValue, lấy tất cả dữ liệu
                res = client.getRow(tableName, keyColumn, keyValue);
            } else {
                // Nếu có keyValue, truy vấn theo key
                res = client.getRow(tableName, keyColumn, keyValue);
            }

            if (res.isSuccess()) {
                System.out.println("✅ Dữ liệu dòng:");
                System.out.println(res.getMessage());

                updateTongTienHoaDonChiTiet(); // Cập nhật TongTien trong b2_hoadonchitiet và b1_hoadon
                tablePanel.refreshTable(); // Làm mới bảng hiển thị
            } else {
                System.err.println("Lỗi: " + res.getMessage());
            }

            return res;
        } catch (Exception e) {
            showErrorDialog("Lỗi khi xử lý: " + e.getMessage());
            e.printStackTrace();
            return new ApiResponse(false, e.getMessage());
        }
    }

    /**
     * Hàm tự động xử lý và cập nhật TongTien cho hóa đơn chi tiết.
     *
     * @param tableName Tên bảng (b2_hoadonchitiet)
     * @param keyColumn Tên cột khóa (MaHoaDon)
     * @param keyValue Giá trị khóa (MaHoaDon)
     */
    public void autoProcessCheckBill(String tableName, String keyColumn, String keyValue) {
        try {
            // Kiểm tra dữ liệu trước khi xử lý
            if (!isDataValid(tableName, keyColumn, keyValue)) {
                showErrorDialog("❌ Dữ liệu không hợp lệ hoặc không tồn tại.");
                return;
            }

            processCheckBill(tableName, keyColumn, keyValue);
            // Lấy lại dữ liệu sau khi cập nhật
            ApiClient.TableDataResult result = ApiClient.getTableData(tableName);
            if (result == null || result.data == null) {
                showErrorDialog("Không thể lấy dữ liệu từ server");
                return;
            }

            // Cập nhật TongTien
            updateTongTienHoaDonChiTiet();
            // Cập nhật bảng hiển thị
            contentPanel.updateTableData(result.data, result.columnComments, result.columnTypes, 
                keyColumn, tableName, "Chi tiết hóa đơn");

            System.out.println("Done check bill");
        } catch (Exception e) {
            showErrorDialog("Lỗi khi xử lý: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật cột TongTien trong bảng b2_hoadonchitiet và b1_hoadon.
     * - b2_hoadonchitiet.TongTien = tienphong + d3_kiemtraphong.TongTien + e2_sddv.TongTien
     * - tienphong = c1_loaiphong.GiaLoai * (c3_datphong.NgayHen - c3_datphong.NgayNhanPhong) + c3_datphong.TienPhat
     * - b1_hoadon.TongTien = SUM(b2_hoadonchitiet.TongTien) WHERE b2_hoadonchitiet.MaHoaDon = b1_hoadon.MaHoaDon
     */
    private void updateTongTienHoaDonChiTiet() throws Exception {
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Bước 1: Tính TongTien cho b2_hoadonchitiet
            String selectQuery = """
                SELECT 
                    hdc.MaHoaDon, hdc.MaPhong, hdc.MaSDDV, hdc.MaKiemTra, hdc.MaDatPhong,
                    COALESCE(sddv.TongTien, 0) AS TongTienSDDV,
                    DATEDIFF(dp.NgayTraPhong, dp.NgayNhanPhong) AS SoNgayThue,
                    COALESCE(lp.GiaLoai, 0) AS GiaLoai,
                    COALESCE(dp.TienPhat, 0) AS TienPhat,
                    COALESCE(kt.TongTien, 0) AS TongTienDenBu
                FROM b2_hoadonchitiet hdc
                LEFT JOIN e2_sddv sddv ON hdc.MaSDDV = sddv.MaSDDV
                LEFT JOIN c3_datphong dp ON hdc.MaDatPhong = dp.MaDatPhong
                LEFT JOIN c2_phong p ON dp.MaPhong = p.MaPhong
                LEFT JOIN c1_loaiphong lp ON p.MaLoai = lp.MaLoai
                LEFT JOIN d3_kiemtraphong kt ON hdc.MaKiemTra = kt.MaKiemTra
            """;

            Map<String, BigDecimal> tongTienMap = new HashMap<>();

            try (PreparedStatement pstmt = conn.prepareStatement(selectQuery);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String maHoaDon = rs.getString("MaHoaDon");
                    String maPhong = rs.getString("MaPhong");

                    // Lấy các giá trị cần thiết
                    BigDecimal tongTienSDDV = rs.getBigDecimal("TongTienSDDV") != null ? rs.getBigDecimal("TongTienSDDV") : BigDecimal.ZERO;
                    BigDecimal tongTienDenBu = rs.getBigDecimal("TongTienDenBu") != null ? rs.getBigDecimal("TongTienDenBu") : BigDecimal.ZERO;
                    int soNgayThue = rs.getInt("SoNgayThue");
                    BigDecimal giaLoai = rs.getBigDecimal("GiaLoai") != null ? rs.getBigDecimal("GiaLoai") : BigDecimal.ZERO;
                    BigDecimal tienPhat = rs.getBigDecimal("TienPhat") != null ? rs.getBigDecimal("TienPhat") : BigDecimal.ZERO;

                    // Tính tiền phòng (tienphong)
                    BigDecimal soNgayThueBD = BigDecimal.valueOf(soNgayThue);
                    BigDecimal tienPhong = giaLoai.multiply(soNgayThueBD).add(tienPhat);

                    // Tính tổng tiền chi tiết (hoadonct.tongtien)
                    BigDecimal tongTien = tienPhong.add(tongTienDenBu).add(tongTienSDDV);

                    tongTienMap.put(maHoaDon + "|" + maPhong, tongTien);
                }
            }

            // Bước 2: Cập nhật TongTien vào b2_hoadonchitiet
            String updateHoadonChiTietQuery = """
                UPDATE b2_hoadonchitiet
                SET TongTien = ?
                WHERE MaHoaDon = ? AND MaPhong = ?
            """;

            try (PreparedStatement pstmt = conn.prepareStatement(updateHoadonChiTietQuery)) {
                for (Map.Entry<String, BigDecimal> entry : tongTienMap.entrySet()) {
                    String[] keys = entry.getKey().split("\\|");
                    String maHoaDon = keys[0];
                    String maPhong = keys[1];
                    BigDecimal tongTien = entry.getValue();

                    pstmt.setBigDecimal(1, tongTien);
                    pstmt.setString(2, maHoaDon);
                    pstmt.setString(3, maPhong);
                    pstmt.addBatch();
                }

                int[] rows = pstmt.executeBatch();
                System.out.println("✔ Đã cập nhật " + rows.length + " dòng trong bảng b2_hoadonchitiet (TongTien)");
            }

            // Bước 3: Cập nhật TongTien vào b1_hoadon (hoadon.tongtien = SUM(hoadonct.tongtien))
            String updateHoadonQuery = """
                UPDATE b1_hoadon hd
                SET hd.TongTien = (
                    SELECT COALESCE(SUM(hdc.TongTien), 0)
                    FROM b2_hoadonchitiet hdc
                    WHERE hdc.MaHoaDon = hd.MaHoaDon
                )
            """;

            try (PreparedStatement pstmt = conn.prepareStatement(updateHoadonQuery)) {
                int rows = pstmt.executeUpdate();
                System.out.println("✔ Đã cập nhật " + rows + " dòng trong bảng b1_hoadon (TongTien)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi cập nhật TongTien: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Kiểm tra dữ liệu trong b2_hoadonchitiet có hợp lệ không.
     *
     * @param tableName Tên bảng
     * @param keyColumn Tên cột khóa
     * @param keyValue Giá trị khóa
     * @return true nếu dữ liệu hợp lệ, false nếu không
     */
    boolean isDataValid(String tableName, String keyColumn, String keyValue) throws SQLException, Exception {
        // Kiểm tra bảng hợp lệ
        if (!tableName.equals("b1_hoadon") && !tableName.equals("b2_hoadonchitiet")) {
            System.err.println("Bảng không hợp lệ: " + tableName);
            return false;
        }
        System.out.println("Bảng hợp lệ: " + tableName);
    
        String query;
        if (keyValue == null || keyValue.trim().isEmpty()) {
            query = "SELECT COUNT(*) FROM " + tableName;
        } else {
            query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + keyColumn + " = ?";
        }
    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (keyValue != null && !keyValue.trim().isEmpty()) {
                pstmt.setString(1, keyValue);
            }
    
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count == 0) {
                        System.err.println("Không tìm thấy dữ liệu với " + keyColumn + " = " + keyValue);
                        return false;
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi SQL khi kiểm tra dữ liệu: " + e.getMessage());
            throw e;
        }
        return false;
    }
    
    /**
     * Hiển thị thông báo lỗi.
     *
     * @param message Thông điệp lỗi
     */
    public Map<String, Object> getInvoiceDetail(String maChiTiet) {
        // Mô phỏng truy vấn CSDL
        Map<String, Object> data = new HashMap<>();
        try {
            var conn = DatabaseUtil.getConnection();
            var query = """
                SELECT TenDichVu, TenThietBi, MaPhong, SoLuong, DonGia, ThanhTien
                FROM b2_hoadonchitiet
                WHERE MaChiTiet = ?
            """;
            try (var pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, maChiTiet);
                var rs = pstmt.executeQuery();
                if (rs.next()) {
                    data.put("success", true);
                    data.put("TenDichVu", rs.getString("TenDichVu"));
                    data.put("TenThietBi", rs.getString("TenThietBi"));
                    data.put("MaPhong", rs.getString("MaPhong"));
                    data.put("SoLuong", rs.getInt("SoLuong"));
                    data.put("DonGia", rs.getInt("DonGia"));
                    data.put("ThanhTien", rs.getInt("ThanhTien"));
                } else {
                    data.put("success", false);
                }
            }
        } catch (Exception e) {
            data.put("success", false);
            e.printStackTrace();
        }
        return data;
    }
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            "Lỗi",
            JOptionPane.ERROR_MESSAGE
        );
    }
}