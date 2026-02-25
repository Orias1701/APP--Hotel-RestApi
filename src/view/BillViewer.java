package view;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class BillViewer extends JFrame {
    private final JPanel mainPanel;

    public BillViewer(Frame parent, Map<String, String> hoaDonInfo, List<Map<String, String>> phongInfo, int soLuongPhong,
                     Map<String, Object> kiemTraPhongInfo, List<Map<String, String>> dichVuInfo, int soLuongDichVuDaDat) {
        super("Chi tiết hóa đơn");
        setSize(1000, 800);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Main panel with vertical BoxLayout
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Style.LIGHT_CL);

        // Bill Information Section
        JPanel hoaDonPanel = new JPanel(new GridLayout(4, 2, 15, 10));
        hoaDonPanel.setBackground(Style.LIGHT_CL);
        hoaDonPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        JLabel[] labels = {
            new JLabel("Mã hóa đơn:"), new JLabel(hoaDonInfo.getOrDefault("mahoadon", "")),
            new JLabel("Ngày lập:"), new JLabel(hoaDonInfo.getOrDefault("ngaylap", "")),
            new JLabel("Khách hàng:"), new JLabel(hoaDonInfo.getOrDefault("tenkhachhang", "")),
            new JLabel("Tổng tiền:"), new JLabel(hoaDonInfo.getOrDefault("tongtien", ""))
        };
        for (JLabel label : labels) {
            label.setFont(Style.MONS_14);
            hoaDonPanel.add(label);
        }
        mainPanel.add(hoaDonPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Room Information Section
        JPanel phongPanel = new JPanel();
        phongPanel.setLayout(new BoxLayout(phongPanel, BoxLayout.Y_AXIS));
        phongPanel.setBackground(Style.LIGHT_CL);
        phongPanel.setBorder(BorderFactory.createTitledBorder("Phòng đã đặt"));
        JLabel phongLabel = new JLabel("Số lượng phòng đã đặt: " + soLuongPhong);
        phongLabel.setFont(Style.MONS_14);
        phongLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        phongPanel.add(phongLabel);
        phongPanel.add(Box.createVerticalStrut(10));

        DefaultTableModel phongModel = new DefaultTableModel(
            new String[]{"Mã phòng", "Loại phòng", "Giá phòng", "Ngày nhận", "Ngày trả", "Ngày hẹn", "Tiền phòng", "Tiền phạt", "Tổng tiền"},
            0
        );
        JTable phongTable = new JTable(phongModel);
        phongTable.setFont(Style.MONS_14);
        phongTable.setRowHeight(30);
        phongTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel phongColumnModel = phongTable.getColumnModel();
        int[] phongColumnWidths = {90, 120, 90, 110, 110, 110, 90, 90, 110};
        for (int i = 0; i < phongColumnWidths.length; i++) {
            phongColumnModel.getColumn(i).setPreferredWidth(phongColumnWidths[i]);
        }
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < phongColumnModel.getColumnCount(); i++) {
            phongColumnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        for (Map<String, String> row : phongInfo) {
            phongModel.addRow(new Object[]{
                row.get("maphong"),
                row.get("tenloaiphong"),
                row.get("giaphong"),
                row.get("ngaynhan"),
                row.get("ngaytra"),
                row.get("ngayhen"),
                row.get("tienphong"),
                row.get("tienphat"),
                row.get("tongtienphong")
            });
        }
        phongTable.setPreferredSize(new Dimension(920, phongTable.getRowHeight() * (phongModel.getRowCount() + 1)));
        phongPanel.add(phongTable);
        mainPanel.add(phongPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Equipment Check Section
        JPanel kiemTraPanel = new JPanel();
        kiemTraPanel.setLayout(new BoxLayout(kiemTraPanel, BoxLayout.Y_AXIS));
        kiemTraPanel.setBackground(Style.LIGHT_CL);
        kiemTraPanel.setBorder(BorderFactory.createTitledBorder("Kiểm tra phòng"));
        String tienDenBu = (String) kiemTraPhongInfo.getOrDefault("tiendenbu", "0");
        JLabel kiemTraLabel = new JLabel("Tiền đền bù: " + tienDenBu);
        kiemTraLabel.setFont(Style.MONS_14);
        kiemTraLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        kiemTraPanel.add(kiemTraLabel);
        kiemTraPanel.add(Box.createVerticalStrut(10));

        if (!tienDenBu.equals("0")) {
            DefaultTableModel thietBiModel = new DefaultTableModel(
                new String[]{"Tên thiết bị", "Tiền đền", "Số lượng hỏng", "Tổng tiền"},
                0
            );
            JTable thietBiTable = new JTable(thietBiModel);
            thietBiTable.setFont(Style.MONS_14);
            thietBiTable.setRowHeight(30);
            thietBiTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumnModel thietBiColumnModel = thietBiTable.getColumnModel();
            int[] thietBiColumnWidths = {200, 200, 150, 200};
            for (int i = 0; i < thietBiColumnWidths.length; i++) {
                thietBiColumnModel.getColumn(i).setPreferredWidth(thietBiColumnWidths[i]);
            }
            for (int i = 0; i < thietBiColumnModel.getColumnCount(); i++) {
                thietBiColumnModel.getColumn(i).setCellRenderer(centerRenderer);
            }
            @SuppressWarnings("unchecked")
            List<Map<String, String>> thietBiHong = (List<Map<String, String>>) kiemTraPhongInfo.get("thietbihong");
            for (Map<String, String> row : thietBiHong) {
                thietBiModel.addRow(new Object[]{
                    row.get("tenthietbi"),
                    row.get("tienden"),
                    row.get("soluonghong"),
                    row.get("tongtienden")
                });
            }
            thietBiTable.setPreferredSize(new Dimension(750, thietBiTable.getRowHeight() * (thietBiModel.getRowCount() + 1)));
            kiemTraPanel.add(thietBiTable);
        }
        mainPanel.add(kiemTraPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Services Section
        JPanel dichVuPanel = new JPanel();
        dichVuPanel.setLayout(new BoxLayout(dichVuPanel, BoxLayout.Y_AXIS));
        dichVuPanel.setBackground(Style.LIGHT_CL);
        dichVuPanel.setBorder(BorderFactory.createTitledBorder("Dịch vụ sử dụng"));
        JLabel dichVuLabel = new JLabel("Số lượng dịch vụ đã đặt: " + soLuongDichVuDaDat);
        dichVuLabel.setFont(Style.MONS_14);
        dichVuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dichVuPanel.add(dichVuLabel);
        dichVuPanel.add(Box.createVerticalStrut(10));

        DefaultTableModel dichVuModel = new DefaultTableModel(
            new String[]{"Tên dịch vụ", "Tiền dịch vụ", "Số lượng", "Tổng tiền"},
            0
        );
        JTable dichVuTable = new JTable(dichVuModel);
        dichVuTable.setFont(Style.MONS_14);
        dichVuTable.setRowHeight(30);
        dichVuTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel dichVuColumnModel = dichVuTable.getColumnModel();
        int[] dichVuColumnWidths = {200, 200, 150, 200};
        for (int i = 0; i < dichVuColumnWidths.length; i++) {
            dichVuColumnModel.getColumn(i).setPreferredWidth(dichVuColumnWidths[i]);
        }
        for (int i = 0; i < dichVuColumnModel.getColumnCount(); i++) {
            dichVuColumnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        for (Map<String, String> row : dichVuInfo) {
            dichVuModel.addRow(new Object[]{
                row.get("tendichvu"),
                row.get("tiendichvu"),
                row.get("soluong"),
                row.get("tongtiendichvu")
            });
        }
        dichVuTable.setPreferredSize(new Dimension(750, dichVuTable.getRowHeight() * (dichVuModel.getRowCount() + 1)));
        dichVuPanel.add(dichVuTable);
        mainPanel.add(dichVuPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Export to HTML Button
        JButton exportButton = new JButton("Xuất File hóa đơn chi tiết");
        exportButton.setFont(Style.MONS_14);
        exportButton.setBackground(Style.MAIN_CL);
        exportButton.setForeground(Style.LIGHT_CL);
        exportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(exportButton);
        exportButton.addActionListener(e -> exportToHTML(hoaDonInfo, phongInfo, soLuongPhong, kiemTraPhongInfo, dichVuInfo, soLuongDichVuDaDat));

        // Add main panel to scroll pane for the window
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
    }

    private void exportToHTML(Map<String, String> hoaDonInfo, List<Map<String, String>> phongInfo, int soLuongPhong,
                             Map<String, Object> kiemTraPhongInfo, List<Map<String, String>> dichVuInfo, int soLuongDichVuDaDat) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file HTML");
        fileChooser.setSelectedFile(new File("ChiTietHoaDon_" + hoaDonInfo.getOrDefault("mahoadon", "bill") + ".html"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                StringBuilder html = new StringBuilder();
                html.append("<!DOCTYPE html><html><head><title>Chi tiết hóa đơn</title>");
                html.append("<meta charset='UTF-8'>");
                html.append("<style>");
                html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
                html.append("h2 { color: #333; }");
                html.append("table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }");
                html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }");
                html.append("th { background-color: #f2f2f2; }");
                html.append("label { display: block; margin-bottom: 10px; }");
                html.append("</style></head><body>");

                // Bill Information
                html.append("<h2>Thông tin hóa đơn</h2>");
                html.append("<label>Mã hóa đơn: ").append(hoaDonInfo.getOrDefault("mahoadon", "")).append("</label>");
                html.append("<label>Ngày lập: ").append(hoaDonInfo.getOrDefault("ngaylap", "")).append("</label>");
                html.append("<label>Khách hàng: ").append(hoaDonInfo.getOrDefault("tenkhachhang", "")).append("</label>");
                html.append("<label>Tổng tiền: ").append(hoaDonInfo.getOrDefault("tongtien", "")).append("</label>");

                // Room Information
                html.append("<h2>Phòng đã đặt</h2>");
                html.append("<label>Số lượng phòng đã đặt: ").append(soLuongPhong).append("</label>");
                html.append("<table>");
                html.append("<tr><th>Mã phòng</th><th>Loại phòng</th><th>Giá phòng</th><th>Ngày nhận</th><th>Ngày trả</th><th>Ngày hẹn</th><th>Tiền phòng</th><th>Tiền phạt</th><th>Tổng tiền</th></tr>");
                for (Map<String, String> row : phongInfo) {
                    html.append("<tr>");
                    html.append("<td>").append(row.get("maphong")).append("</td>");
                    html.append("<td>").append(row.get("tenloaiphong")).append("</td>");
                    html.append("<td>").append(row.get("giaphong")).append("</td>");
                    html.append("<td>").append(row.get("ngaynhan")).append("</td>");
                    html.append("<td>").append(row.get("ngaytra")).append("</td>");
                    html.append("<td>").append(row.get("ngayhen")).append("</td>");
                    html.append("<td>").append(row.get("tienphong")).append("</td>");
                    html.append("<td>").append(row.get("tienphat")).append("</td>");
                    html.append("<td>").append(row.get("tongtienphong")).append("</td>");
                    html.append("</tr>");
                }
                html.append("</table>");

                // Equipment Check
                html.append("<h2>Kiểm tra phòng</h2>");
                String tienDenBu = (String) kiemTraPhongInfo.getOrDefault("tiendenbu", "0");
                html.append("<label>Tiền đền bù: ").append(tienDenBu).append("</label>");
                if (!tienDenBu.equals("0")) {
                    html.append("<table>");
                    html.append("<tr><th>Tên thiết bị</th><th>Tiền đền</th><th>Số lượng hỏng</th><th>Tổng tiền</th></tr>");
                    @SuppressWarnings("unchecked")
                    List<Map<String, String>> thietBiHong = (List<Map<String, String>>) kiemTraPhongInfo.get("thietbihong");
                    for (Map<String, String> row : thietBiHong) {
                        html.append("<tr>");
                        html.append("<td>").append(row.get("tenthietbi")).append("</td>");
                        html.append("<td>").append(row.get("tienden")).append("</td>");
                        html.append("<td>").append(row.get("soluonghong")).append("</td>");
                        html.append("<td>").append(row.get("tongtienden")).append("</td>");
                        html.append("</tr>");
                    }
                    html.append("</table>");
                }            

                // Services
                html.append("<h2>Dịch vụ sử dụng</h2>");
                html.append("<label>Số lượng dịch vụ đã đặt: ").append(soLuongDichVuDaDat).append("</label>");
                html.append("<table>");
                html.append("<tr><th>Tên dịch vụ</th><th>Tiền dịch vụ</th><th>Số lượng</th><th>Tổng tiền</th></tr>");
                for (Map<String, String> row : dichVuInfo) {
                    html.append("<tr>");
                    html.append("<td>").append(row.get("tendichvu")).append("</td>");
                    html.append("<td>").append(row.get("tiendichvu")).append("</td>");
                    html.append("<td>").append(row.get("soluong")).append("</td>");
                    html.append("<td>").append(row.get("tongtiendichvu")).append("</td>");
                    html.append("</tr>");
                }
                html.append("</table>");

                html.append("</body></html>");

                // Write to file
                try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                    writer.write(html.toString());
                }
                JOptionPane.showMessageDialog(this, "Xuất thành công! Mở file và in thành PDF nếu cần.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất HTML: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}