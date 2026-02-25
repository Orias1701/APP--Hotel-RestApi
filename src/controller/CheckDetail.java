package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.ApiClient;
import model.ApiClient.ApiResponse;
import model.TableDataOperationsClient;
import view.MainRegion.ContentPanel;
import view.MainRegion.TablePanel;

public class CheckDetail {

    private final TableDataOperationsClient client;
    private ContentPanel contentPanel; // Tham chiếu đến ContentPanel để cập nhật dữ liệu
    private TablePanel tablePanel; // Tham chiếu đến TablePanel để làm mới bảng

    // Constructor để khởi tạo với authToken, contentPanel và tablePanel
    public CheckDetail(String authToken, ContentPanel contentPanel, TablePanel tablePanel) {
        this.client = new TableDataOperationsClient(authToken);
        this.contentPanel = contentPanel;
        this.tablePanel = tablePanel;
    }

    // Hàm chính xử lý logic kiểm tra và cập nhật đền bù
    public ApiResponse processCheckDetail(String tableName, String keyColumn, String keyValue) {
        ApiResponse res;

        try {
            // Kiểm tra số lượng trước khi xử lý
            if (!isSoLuongValid()) {
                showErrorDialog("❌ Số lượng trong kiểm tra chi tiết vượt quá số lượng trong thiết bị phòng.");
                return new ApiResponse(false, "Số lượng không hợp lệ");
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

                updateDenBuTheoTinhTrang(); // Cập nhật đền bù theo tình trạng thiết bị
                updateTongTienKiemTraPhong(); // Cập nhật tổng tiền trong bảng kiểm tra phòng
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

    // Hàm cập nhật cột Đền Bù theo tình trạng thiết bị
    private void updateDenBuTheoTinhTrang() throws Exception {
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Trường hợp 'Tốt' => Đền Bù = 0
            String updateTot = "UPDATE d4_kiemtrachitiet SET DenBu = 0 WHERE TinhTrang = 'Tốt'";
            try (PreparedStatement pstmt = conn.prepareStatement(updateTot)) {
                int rows = pstmt.executeUpdate();
                System.out.println("✔ Đã cập nhật " + rows + " dòng có Tình Trạng = 'Tốt' (Đền Bù = 0)");
            }

            // Trường hợp 'Hỏng' => Lấy Đền Bù từ bảng d1_thietbi
            String updateHong = """
                UPDATE d4_kiemtrachitiet kt
                JOIN d1_thietbi tb ON kt.MaThietBi = tb.MaThietBi
                SET kt.DenBu = tb.DenBu
                WHERE kt.TinhTrang = 'Hỏng'
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(updateHong)) {
                int rows = pstmt.executeUpdate();
                System.out.println("✔ Đã cập nhật " + rows + " dòng có Tình Trạng = 'Hỏng' (Lấy Đền Bù từ bảng thiết bị)");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi cập nhật Đền Bù: " + e.getMessage());
        }
    }

    // Hàm cập nhật cột TongTien trong bảng d3_kiemtraphong
    private void updateTongTienKiemTraPhong() throws Exception {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String updateTongTien = """
                UPDATE d3_kiemtraphong kp
                SET kp.TongTien = (
                    SELECT COALESCE(SUM(kt.DenBu), 0)
                    FROM d4_kiemtrachitiet kt
                    WHERE kt.MaKiemTra = kp.MaKiemTra
                )
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(updateTongTien)) {
                int rows = pstmt.executeUpdate();
                System.out.println("✔ Đã cập nhật " + rows + " dòng trong bảng d3_kiemtraphong (TongTien)");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi cập nhật TongTien: " + e.getMessage());
        }
    }

    // Hàm kiểm tra số lượng trong d4_kiemtrachitiet không vượt quá d2_thietbiphong
    private boolean isSoLuongValid() throws Exception {
        String query = """
            SELECT kt.MaThietBi, kt.MaPhong, kt.SoLuong AS SoLuongKT, tbp.SoLuong AS SoLuongTBP
            FROM d4_kiemtrachitiet kt
            JOIN d2_thietbiphong tbp ON kt.MaThietBi = tbp.MaThietBi AND kt.MaPhong = tbp.MaPhong
            WHERE kt.SoLuong > tbp.SoLuong
        """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                // Nếu có ít nhất một dòng vượt quá số lượng
                System.err.println("Số lượng kiểm tra chi tiết vượt quá số lượng thiết bị phòng: " +
                        "MaThietBi=" + rs.getString("MaThietBi") + ", MaPhong=" + rs.getString("MaPhong") +
                        ", SoLuongKT=" + rs.getInt("SoLuongKT") + ", SoLuongTBP=" + rs.getInt("SoLuongTBP"));
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi SQL khi kiểm tra số lượng: " + e.getMessage());
            return false;
        }
    }

    // Hàm xử lý tự động kiểm tra và cập nhật đền bù
    public void autoProcessCheckDetail(String tableName, String keyColumn, String keyValue) {
        try {
            // Kiểm tra số lượng trước khi xử lý
            if (!isSoLuongValid()) {
                showErrorDialog("❌ Số lượng trong kiểm tra chi tiết vượt quá số lượng trong thiết bị phòng.");
                return;
            }

            processCheckDetail(tableName, keyColumn, keyValue);
            // Lấy lại dữ liệu sau khi cập nhật
            ApiClient.TableDataResult result = ApiClient.getTableData(tableName);
            if (result == null || result.data == null) {
                showErrorDialog("Không thể lấy dữ liệu từ server");
                return;
            }
            // Cập nhật lại đền bù và tổng tiền
            updateDenBuTheoTinhTrang();
            updateTongTienKiemTraPhong();
            // Cập nhật bảng hiển thị
            contentPanel.updateTableData(result.data, result.columnComments, result.columnTypes, keyColumn, tableName, "Chi tiết kiểm tra");
            tablePanel.refreshTable();
            System.out.println("Done check detail");
        } catch (Exception e) {
            showErrorDialog("Lỗi khi xử lý: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hàm hiển thị thông báo lỗi
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            "Lỗi",
            JOptionPane.ERROR_MESSAGE
        );
    }
}