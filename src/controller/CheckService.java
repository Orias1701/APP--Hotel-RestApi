package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.ApiClient;
import model.ApiClient.TableDataResult;
import view.MainRegion.ContentPanel;
import view.MainRegion.TablePanel;
public class CheckService {
    private static final String TABLE_NAME = "e3_chitietsddv";
    private static final String SDDV_TABLE_NAME = "e2_sddv";

    private final ContentPanel contentPanel;
    private final TablePanel tablePanel;

    public CheckService(ContentPanel contentPanel, TablePanel tablePanel) {
        this.contentPanel = contentPanel;
        this.tablePanel = tablePanel;
    }

    public void autoProcessCheckService(String keyColumn, String keyValue) {
        try {
            // Kiểm tra MaSDDV nếu cần (nếu truyền vào keyValue)
            if (keyValue != null && !keyValue.isBlank() && isMaSDDVExists(keyValue) == false) {
                showErrorDialog("❌ Mã sử dụng dịch vụ không tồn tại: " + keyValue);
                return;
            }

            updateTongTienDichVu();   // Cập nhật chi tiết từng dịch vụ
            updateTongTienSDDV();     // Tổng hợp tiền theo MaSDDV

            TableDataResult result = ApiClient.getTableData(TABLE_NAME);
            if (result == null || result.data == null) {
                showErrorDialog("Không thể lấy dữ liệu từ server.");
                return;
            }

            contentPanel.updateTableData(
                result.data,
                result.columnComments,
                result.columnTypes,
                keyColumn,
                TABLE_NAME,
                "Chi tiết dịch vụ"
            );

            tablePanel.refreshTable();
            System.out.println("✅ Đã cập nhật tổng tiền sử dụng dịch vụ.");
        } catch (Exception e) {
            showErrorDialog("Lỗi khi xử lý: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateTongTienDichVu() throws Exception {
        String query = """
            UPDATE e3_chitietsddv ct
            JOIN e1_dichvu d ON ct.MaDichVu = d.MaDichVu
            SET ct.TongTien = ct.SoLuongDichVu * d.GiaDichVu
        """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✔ Đã cập nhật tổng tiền cho " + rowsAffected + " dòng trong bảng " + TABLE_NAME + ".");
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật chi tiết dịch vụ: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi khác khi cập nhật chi tiết dịch vụ: " + e.getMessage());
        }
    }

    private void updateTongTienSDDV() throws Exception {
        String query = """
            UPDATE e2_sddv s
            SET s.TongTien = (
                SELECT COALESCE(SUM(ct.TongTien), 0)
                FROM e3_chitietsddv ct
                WHERE ct.MaSDDV = s.MaSDDV
            )
        """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✔ Đã cập nhật tổng tiền cho " + rowsAffected + " dòng trong bảng " + SDDV_TABLE_NAME + ".");
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật sử dụng dịch vụ: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi khác khi cập nhật sử dụng dịch vụ: " + e.getMessage());
        }
    }

    // ✅ Hàm kiểm tra MaSDDV đã tồn tại
    public static boolean isMaSDDVExists(String maSDDV) throws Exception {
        String query = "SELECT 1 FROM e2_sddv WHERE MaSDDV = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, maSDDV);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Trả về true nếu có ít nhất 1 dòng
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
