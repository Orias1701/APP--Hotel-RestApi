package view.MainRegion;

import controller.BillHandler;
import controller.LogHandler;
import controller.MainCtrl;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import model.ApiClient.ApiResponse;
import view.Style;

/**
 * FormDialogPanel là lớp xử lý hiển thị form thêm/sửa/xóa dữ liệu
 * dựa trên bảng hiện tại được hiển thị trên TablePanel.
 */
public class FormDialogPanel implements FormDialogHandler {
    private final TablePanel tablePanel;

    public FormDialogPanel(TablePanel tablePanel) {
        this.tablePanel = tablePanel;
    }

    @Override
    public void showFormDialog(String actionType, int rowIndex) {
        // Kiểm tra điều kiện đầu vào để tránh lỗi logic
        if (tablePanel.getPrimaryKeyColumns() == null || tablePanel.getPrimaryKeyColumns().isEmpty()) {
            JOptionPane.showMessageDialog(tablePanel, "Không tìm thấy khóa chính của bảng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (tablePanel.getTableName() == null || tablePanel.getTableName().isEmpty()) {
            JOptionPane.showMessageDialog(tablePanel, "Không xác định được tên bảng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (String keyCol : tablePanel.getPrimaryKeyColumns()) {
            if (!tablePanel.getColumnNames().contains(keyCol)) {
                JOptionPane.showMessageDialog(tablePanel, "Khóa chính '" + keyCol + "' không khớp với các cột của bảng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (!actionType.equals("add") && !actionType.equals("detail") && (rowIndex < 0 || rowIndex >= tablePanel.getTable().getRowCount())) {
            JOptionPane.showMessageDialog(tablePanel, "Hàng được chọn không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Xử lý đặc biệt cho actionType="detail" và bảng b1
        if (actionType.equals("detail") && "b1_hoadon".equals(tablePanel.getTableName())) {
            try {
                if (rowIndex < 0 || rowIndex >= tablePanel.getTable().getRowCount()) {
                    JOptionPane.showMessageDialog(tablePanel, "Hàng được chọn không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Lấy maHoaDon từ cột đầu tiên của hàng được chọn
                String maHoaDon = tablePanel.getTable().getValueAt(rowIndex, 0).toString();
                LogHandler.logInfo("Showing invoice detail for table: b1_hoadon, maHoaDon: " + maHoaDon);
                
                BillHandler billHandler = new BillHandler(tablePanel.getContentPanel(), tablePanel);
                Window window = SwingUtilities.getWindowAncestor(tablePanel);
                if (window instanceof Frame frame) {
                    billHandler.showInvoiceDetail(frame, maHoaDon);
                } else {
                    JOptionPane.showMessageDialog(tablePanel, "Parent window is not a Frame!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                LogHandler.logError("Lỗi khi hiển thị chi tiết hóa đơn: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(tablePanel, "Lỗi khi hiển thị chi tiết hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return; // Thoát ngay, không hiển thị dialog
        }

        // Tạo dialog chứa form nhập liệu cho các trường hợp khác
        JDialog dialog = new JDialog((Frame) null, true);
        dialog.setTitle(actionType.equals("add") ? "Thêm dữ liệu" :
                actionType.equals("edit") ? "Sửa dữ liệu" :
                actionType.equals("delete") ? "Xóa dữ liệu" :
                actionType.equals("all") ? "Quản lý dữ liệu" : "Chi tiết dữ liệu");
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(Style.LIGHT_CL);

        // Tạo panel chứa các trường nhập liệu theo dạng lưới 2 cột
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(Style.LIGHT_CL);
        Map<String, JTextField> inputFields = new HashMap<>();

        // Formatter cho datetime
        SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat[] inputFormatters = {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm"),
            new SimpleDateFormat("yyyy-MM-dd HH")
        };

        // Duyệt qua các cột của bảng để tạo label + textfield tương ứng
        for (int i = 0; i < tablePanel.getColumnNames().size(); i++) {
            String col = tablePanel.getColumnNames().get(i);
            String comment = tablePanel.getColumnComments().get(i);
            String type = tablePanel.getColumnTypes().get(i);

            JLabel label = new JLabel(comment + ":");
            label.setFont(Style.MONS_14);
            label.setForeground(Style.DARK_CL);

            JTextField field = new JTextField(20);
            field.setFont(Style.MONS_14);
            field.setBorder(BorderFactory.createCompoundBorder(
                new Style.RoundBorder(Style.MAIN_CL, 10),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));

            // Xử lý giá trị và định dạng
            String value = "";
            if (!actionType.equals("add") && rowIndex >= 0) {
                Object cellValue = tablePanel.getTable().getValueAt(rowIndex, i);
                value = cellValue != null ? cellValue.toString() : "";
            }

            if (type.equalsIgnoreCase("datetime") && value != null && !value.isEmpty()) {
                boolean parsed = false;
                for (SimpleDateFormat formatter : inputFormatters) {
                    try {
                        java.util.Date date = formatter.parse(value);
                        value = outputFormatter.format(date);
                        parsed = true;
                        break;
                    } catch (java.text.ParseException ignored) {
                    }
                }
                if (!parsed) {
                    LogHandler.logWarn("Parse error for datetime value in FormDialogPanel: " + value);
                }
            } else if (type.equalsIgnoreCase("decimal") && value != null && !value.isEmpty()) {
                // Không định dạng chuỗi, giữ nguyên giá trị số
                try {
                    Double.parseDouble(value); // Kiểm tra giá trị hợp lệ
                } catch (NumberFormatException e) {
                    LogHandler.logWarn("Invalid decimal value in FormDialogPanel: " + value);
                }
            }

            field.setText(value);

            // Chỉ cho phép chỉnh sửa khi là add, edit hoặc all
            if (actionType.equals("delete") || actionType.equals("detail") || (tablePanel.getPrimaryKeyColumns().contains(col) && !actionType.equals("add"))) {
                field.setEditable(false);
                field.setForeground(Style.ACT_CL);
                field.setBackground(Style.LIGHT_CL);
            }

            formPanel.add(label);
            formPanel.add(field);
            inputFields.put(col, field);
        }

        // Đặt formPanel vào JScrollPane (phòng khi nhiều dòng)
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Style.LIGHT_CL);

        // Tạo panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Style.LIGHT_CL);

        // Nút xác nhận (Thêm/Cập nhật/Xóa)
        Style.RoundedButton confirmButton = null;
        if (actionType.equals("add") || actionType.equals("edit") || actionType.equals("all")) {
            confirmButton = new Style.RoundedButton(
                    actionType.equals("add") ? "Thêm" : "Cập nhật"
            );
            confirmButton.setFont(Style.MONS_14);
            confirmButton.setBackground(Style.BLUE);
            confirmButton.setForeground(Color.WHITE);
            confirmButton.setPreferredSize(new Dimension(100, 40));

            confirmButton.addActionListener(e -> {
                // Kiểm tra dữ liệu đầu vào
                Map<String, Object> rowData = new HashMap<>();
                java.util.List<String> primaryKeyColumns = tablePanel.getPrimaryKeyColumns();
                java.util.List<String> emptyNonKeyColumns = new ArrayList<>();

                for (String col : tablePanel.getColumnNames()) {
                    String value = inputFields.get(col).getText().trim();
                    int colIndex = tablePanel.getColumnNames().indexOf(col);
                    String type = tablePanel.getColumnTypes().get(colIndex);
                    String comment = tablePanel.getColumnComments().get(colIndex);

                    // Kiểm tra cột khóa chính
                    if (primaryKeyColumns.contains(col) && value.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Cột khóa chính '" + comment + "' không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Kiểm tra các cột không phải khóa chính
                    if (!primaryKeyColumns.contains(col) && value.isEmpty()) {
                        emptyNonKeyColumns.add(comment);
                    } else {
                        // Kiểm tra email hợp lệ
                        if (col.toLowerCase().contains("email") && !value.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                            JOptionPane.showMessageDialog(dialog, "Email không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        // Kiểm tra decimal hợp lệ
                        if (type.equalsIgnoreCase("decimal") && !value.isEmpty() && !value.matches("-?\\d+(\\.\\d+)?")) {
                            JOptionPane.showMessageDialog(dialog, "Giá trị '" + comment + "' không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    rowData.put(col, value);
                }

                // Nếu có cột không phải khóa chính trống, hỏi xác nhận
                if (!emptyNonKeyColumns.isEmpty()) {
                    String message = "Các cột sau đang trống: " + String.join(", ", emptyNonKeyColumns) + "\nBạn có muốn tiếp tục lưu dữ liệu?";
                    int choice = JOptionPane.showConfirmDialog(dialog, message, "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (choice != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                try {
                    ApiResponse response;
                    if (actionType.equals("add")) {
                        response = MainCtrl.addRow(tablePanel.getTableName(), rowData);
                    } else {
                        Map<String, String> keyValues = new HashMap<>();
                        for (String keyCol : primaryKeyColumns) {
                            keyValues.put(keyCol, inputFields.get(keyCol).getText().trim());
                        }
                        response = MainCtrl.updateRow(tablePanel.getTableName(), primaryKeyColumns, keyValues, rowData);
                    }
                    if (response.isSuccess()) {
                        JOptionPane.showMessageDialog(dialog, actionType.equals("add") ? "Thêm dữ liệu thành công" : "Cập nhật dữ liệu thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        tablePanel.refreshTable();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, response.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    LogHandler.logError("Lỗi kết nối: " + ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(dialog, "Lỗi kết nối: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else if (actionType.equals("delete")) {
            confirmButton = new Style.RoundedButton("Xóa");
            confirmButton.setFont(Style.MONS_14);
            confirmButton.setBackground(Style.RED);
            confirmButton.setForeground(Color.WHITE);
            confirmButton.setPreferredSize(new Dimension(100, 40));

            confirmButton.addActionListener(e -> {
                java.util.List<String> primaryKeyColumns = tablePanel.getPrimaryKeyColumns();
                Map<String, String> keyValues = new HashMap<>();
                StringBuilder confirmMessage = new StringBuilder("Bạn có chắc chắn muốn xóa hàng với: ");
                for (String keyCol : primaryKeyColumns) {
                    String keyValue = inputFields.get(keyCol).getText().trim();
                    keyValues.put(keyCol, keyValue);
                    confirmMessage.append(keyCol).append(": ").append(keyValue).append("; ");
                }
                confirmMessage.append("?");
                int confirm = JOptionPane.showConfirmDialog(dialog, confirmMessage.toString(), "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        ApiResponse response = MainCtrl.deleteRow(tablePanel.getTableName(), primaryKeyColumns, keyValues);
                        if (response.isSuccess()) {
                            JOptionPane.showMessageDialog(dialog, "Xóa dữ liệu thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                            tablePanel.refreshTable();
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, response.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        LogHandler.logError("Lỗi kết nối: " + ex.getMessage(), ex);
                        JOptionPane.showMessageDialog(dialog, "Lỗi kết nối: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        // Nút xóa cho trường hợp all
        Style.RoundedButton deleteButton = null;
        if (actionType.equals("all")) {
            deleteButton = new Style.RoundedButton("Xóa");
            deleteButton.setFont(Style.MONS_14);
            deleteButton.setBackground(Style.RED);
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setPreferredSize(new Dimension(100, 40));
            deleteButton.addActionListener(e -> {
                java.util.List<String> primaryKeyColumns = tablePanel.getPrimaryKeyColumns();
                Map<String, String> keyValues = new HashMap<>();
                StringBuilder confirmMessage = new StringBuilder("Bạn có chắc chắn muốn xóa hàng với: ");
                for (String keyCol : primaryKeyColumns) {
                    String keyValue = inputFields.get(keyCol).getText().trim();
                    keyValues.put(keyCol, keyValue);
                    confirmMessage.append(keyCol).append(": ").append(keyValue).append("; ");
                }
                confirmMessage.append("?");
                int confirm = JOptionPane.showConfirmDialog(dialog, confirmMessage.toString(), "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        ApiResponse response = MainCtrl.deleteRow(tablePanel.getTableName(), primaryKeyColumns, keyValues);
                        if (response.isSuccess()) {
                            JOptionPane.showMessageDialog(dialog, "Xóa dữ liệu thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                            tablePanel.refreshTable();
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, response.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        LogHandler.logError("Lỗi kết nối: " + ex.getMessage(), ex);
                        JOptionPane.showMessageDialog(dialog, "Lỗi kết nối: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        // Nút hủy hoặc thoát
        Style.RoundedButton cancelButton = new Style.RoundedButton(actionType.equals("detail") ? "Thoát" : "Hủy");
        cancelButton.setFont(Style.MONS_14);
        cancelButton.setBackground(Style.GRAY_CL);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(e -> dialog.dispose());

        // Thêm nút vào panel theo loại hành động
        buttonPanel.add(cancelButton);
        if (deleteButton != null) {
            buttonPanel.add(deleteButton);
        }
        if (confirmButton != null) {
            buttonPanel.add(confirmButton);
        }

        // Thêm các phần tử vào dialog
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Cấu hình kích thước và hiển thị dialog
        dialog.setMinimumSize(new Dimension(400, 300));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    @Override
    public void handleAction(String action, JTable table, int selectedRow) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}