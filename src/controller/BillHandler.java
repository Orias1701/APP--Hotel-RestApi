package controller;

import java.awt.Frame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import javax.swing.JOptionPane;
import view.BillViewer;
import view.MainRegion.ContentPanel;
import view.MainRegion.TablePanel;

public class BillHandler {
    private final CheckBill checkBill;

    // Hàm khởi tạo nhận vào contentPanel và tablePanel để xử lý
    public BillHandler(ContentPanel contentPanel, TablePanel tablePanel) {
        this.checkBill = new CheckBill("defaultString", contentPanel, tablePanel);
    }

    // Hiển thị chi tiết hóa đơn cho người dùng
    public void showInvoiceDetail(Frame parent, String maHoaDon) {
        try {
            // Kiểm tra hóa đơn có tồn tại không
            if (!checkBill.isDataValid("b1_hoadon", "MaHoaDon", maHoaDon)) {
                JOptionPane.showMessageDialog(parent, "Hóa đơn không tồn tại hoặc không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy thông tin tổng quát hóa đơn
            Map<String, String> hoaDonInfo = getThongTinHoaDon(maHoaDon);

            // Lấy thông tin các phòng có trong hóa đơn
            List<Map<String, String>> phongInfo = getPhongInfo(maHoaDon);
            int soLuongPhong = phongInfo.size();

            // Lấy thông tin kiểm tra phòng, tiền đền bù
            Map<String, Object> kiemTraPhongInfo = getKiemTraPhongInfo(maHoaDon);

            // Lấy thông tin các dịch vụ đã sử dụng
            List<Map<String, String>> dichVuInfo = getDichVuInfo(maHoaDon);
            int soLuongDichVuDaDat = dichVuInfo.stream()
                .mapToInt(row -> Integer.parseInt(row.getOrDefault("soluong", "0")))
                .sum();

            // Tự động xử lý cập nhật trạng thái hóa đơn chi tiết
            checkBill.autoProcessCheckBill("b2_hoadonchitiet", "MaHoaDon", maHoaDon);

            // Hiển thị giao diện chi tiết hóa đơn
            BillViewer billViewer = new BillViewer(parent, hoaDonInfo, phongInfo, soLuongPhong, kiemTraPhongInfo, dichVuInfo, soLuongDichVuDaDat);
            billViewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Lỗi khi xử lý hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Truy vấn thông tin tổng quan hóa đơn
    private Map<String, String> getThongTinHoaDon(String maHoaDon) throws Exception {
        Map<String, String> data = new HashMap<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = """
                SELECT hd.MaHoaDon, kh.TenKhachHang AS TenKhachHang, hd.Ngay, hd.TongTien
                FROM b1_hoadon hd
                JOIN a1_khachhang kh ON hd.MaKhachHang = kh.MaKhachHang
                WHERE hd.MaHoaDon = ?
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, maHoaDon);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        data.put("mahoadon", rs.getString("MaHoaDon"));
                        data.put("tenkhachhang", rs.getString("TenKhachHang"));
                        data.put("ngaylap", rs.getString("Ngay"));
                        data.put("tongtien", rs.getString("TongTien"));
                    }
                }
            }
        }
        return data;
    }

    // Lấy danh sách phòng đã đặt trong hóa đơn
    private List<Map<String, String>> getPhongInfo(String maHoaDon) throws Exception {
        List<Map<String, String>> phongInfo = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = """
                SELECT 
                    hdc.MaPhong, 
                    lp.TenLoai, 
                    lp.GiaLoai, 
                    dp.NgayNhanPhong, 
                    dp.NgayTraPhong, 
                    dp.NgayHen, 
                    dp.TienPhat
                FROM b2_hoadonchitiet hdc
                JOIN c3_datphong dp ON hdc.MaDatPhong = dp.MaDatPhong
                JOIN c2_phong p ON hdc.MaPhong = p.MaPhong
                JOIN c1_loaiphong lp ON p.MaLoai = lp.MaLoai
                WHERE hdc.MaHoaDon = ?
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, maHoaDon);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> row = new HashMap<>();
                        row.put("maphong", rs.getString("MaPhong"));
                        row.put("tenloaiphong", rs.getString("TenLoai"));
                        String giaPhong = rs.getString("GiaLoai");
                        row.put("giaphong", giaPhong);

                        // Tính số ngày thuê từ ngày nhận đến ngày trả
                        String ngayNhan = rs.getString("NgayNhanPhong");
                        String ngayTra = rs.getString("NgayTraPhong");
                        row.put("ngaynhan", ngayNhan);
                        row.put("ngaytra", ngayTra);
                        row.put("ngayhen", rs.getString("NgayHen"));

                        long days = java.time.temporal.ChronoUnit.DAYS.between(
                            java.time.LocalDate.parse(ngayNhan.split(" ")[0]),
                            java.time.LocalDate.parse(ngayTra.split(" ")[0])
                        );

                        // Tính tiền phòng = giá phòng x số ngày
                        double tienPhong = Double.parseDouble(giaPhong) * days;
                        row.put("tienphong", String.valueOf(tienPhong));

                        // Tính tiền phạt nếu có
                        String tienPhat = rs.getString("TienPhat");
                        double phat = tienPhat != null ? Double.parseDouble(tienPhat) : 0.0;
                        row.put("tienphat", String.valueOf(phat));

                        // Tổng tiền phòng = tiền phòng + tiền phạt
                        double tongTienPhong = tienPhong + phat;
                        row.put("tongtienphong", String.valueOf(tongTienPhong));

                        phongInfo.add(row);
                    }
                }
            }
        }
        return phongInfo;
    }

    // Lấy thông tin kiểm tra phòng, thiết bị hỏng, tiền đền bù
    private Map<String, Object> getKiemTraPhongInfo(String maHoaDon) throws Exception {
        Map<String, Object> kiemTraInfo = new HashMap<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Lấy tổng tiền đền bù
            String queryDenBu = """
                SELECT kt.TongTien
                FROM b2_hoadonchitiet hdc
                JOIN d3_kiemtraphong kt ON hdc.MaKiemTra = kt.MaKiemTra
                WHERE hdc.MaHoaDon = ?
            """;
            double tienDenBu = 0.0;
            try (PreparedStatement pstmt = conn.prepareStatement(queryDenBu)) {
                pstmt.setString(1, maHoaDon);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        tienDenBu = rs.getDouble("TongTien");
                    }
                }
            }
            kiemTraInfo.put("tiendenbu", String.valueOf(tienDenBu));

            // Nếu có tiền đền thì lấy chi tiết thiết bị hỏng
            if (tienDenBu != 0) {
                List<Map<String, String>> thietBiHong = new ArrayList<>();
                String queryChiTiet = """
                    SELECT 
                        tb.TenThietBi, 
                        tb.DenBu, 
                        ktct.SoLuong
                    FROM b2_hoadonchitiet hdc
                    JOIN d3_kiemtraphong kt ON hdc.MaKiemTra = kt.MaKiemTra
                    JOIN d4_kiemtrachitiet ktct ON kt.MaKiemTra = ktct.MaKiemTra
                    JOIN d1_thietbi tb ON ktct.MaThietBi = tb.MaThietBi
                    WHERE hdc.MaHoaDon = ?
                """;
                try (PreparedStatement pstmt = conn.prepareStatement(queryChiTiet)) {
                    pstmt.setString(1, maHoaDon);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            Map<String, String> row = new HashMap<>();
                            row.put("tenthietbi", rs.getString("TenThietBi"));
                            String tienDen = rs.getString("DenBu");
                            String soLuongHong = rs.getString("SoLuong");

                            // Tính tổng tiền đền cho từng loại thiết bị
                            double tongTienDen = Double.parseDouble(tienDen) * Double.parseDouble(soLuongHong);
                            row.put("tienden", tienDen);
                            row.put("soluonghong", soLuongHong);
                            row.put("tongtienden", String.valueOf(tongTienDen));
                            thietBiHong.add(row);
                        }
                    }
                }
                kiemTraInfo.put("thietbihong", thietBiHong);
            } else {
                kiemTraInfo.put("thietbihong", new ArrayList<Map<String, String>>());
            }
        }
        return kiemTraInfo;
    }

    // Lấy thông tin dịch vụ đã dùng
    private List<Map<String, String>> getDichVuInfo(String maHoaDon) throws Exception {
        List<Map<String, String>> dichVuInfo = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = """
                SELECT 
                    dv.TenDichVu, 
                    dv.GiaDichVu, 
                    sddvct.SoLuongDichVu
                FROM b2_hoadonchitiet hdc
                JOIN e2_sddv sddv ON hdc.MaSDDV = sddv.MaSDDV
                JOIN e3_chitietsddv sddvct ON sddv.MaSDDV = sddvct.MaSDDV
                JOIN e1_dichvu dv ON sddvct.MaDichVu = dv.MaDichVu
                WHERE hdc.MaHoaDon = ?
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, maHoaDon);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> row = new HashMap<>();
                        row.put("tendichvu", rs.getString("TenDichVu"));
                        String tienDichVu = rs.getString("GiaDichVu");
                        String soLuong = rs.getString("SoLuongDichVu");

                        // Tính tổng tiền dịch vụ
                        double tongTienDichVu = Double.parseDouble(tienDichVu) * Double.parseDouble(soLuong);
                        row.put("tiendichvu", tienDichVu);
                        row.put("soluong", soLuong);
                        row.put("tongtiendichvu", String.valueOf(tongTienDichVu));

                        dichVuInfo.add(row);
                    }
                }
            }
        }
        return dichVuInfo;
    }
}