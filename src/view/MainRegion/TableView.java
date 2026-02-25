package view.MainRegion;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.*;

import controller.UserSession;
import view.Style;

/**
 * TableView là lớp hiển thị bảng dữ liệu có hỗ trợ nút thao tác (Sửa/Xóa/Chi tiết),
 * định dạng theo kiểu dữ liệu (ví dụ: căn lề phải cho decimal, định dạng ngày giờ...).
 */
public class TableView {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JPanel view;
    private final TablePanel parent;
    private int sortedColumn = -1; // Cột đang được sắp xếp (-1 là không có cột nào)
    private int sortOrder = 0; // 0: mặc định, 1: tăng, 2: giảm
    private List<Map<String, String>> originalData; // Lưu dữ liệu gốc để quay lại trạng thái mặc định

    public TableView(TablePanel parent) {
        this.parent = parent;
        view = new JPanel(new BorderLayout());
        view.setOpaque(true);
        view.setBackground(Style.LIGHT_CL);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setFont(Style.ROB_14);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Tùy chỉnh hiển thị từng ô trong bảng (giữ nguyên như mã gốc)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JLabel label) {
                    label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Style.ACT_CL),
                        BorderFactory.createEmptyBorder(0, 20, 0, 15)
                    ));
                    label.setFont(Style.ROB_14);
                    label.setOpaque(true);
                    label.setBackground(isSelected ? Style.SEC_CL : Style.LIGHT_CL);
                    label.setForeground(isSelected ? Style.ACT_CL : Style.DARK_CL);

                    List<String> columnTypes = parent.getColumnTypes();
                    List<String> columnNames = parent.getColumnNames();
                    List<String> columnComments = parent.getColumnComments();
                    String columnName = table.getColumnName(column);

                    int columnIndex = columnComments != null ? columnComments.indexOf(columnName) : -1;
                    if (columnIndex < 0 && columnNames != null)
                        columnIndex = columnNames.indexOf(columnName);

                    if (columnIndex >= 0 && columnTypes != null && columnIndex < columnTypes.size()) {
                        String dataType = columnTypes.get(columnIndex);
                        label.setHorizontalAlignment(dataType.equalsIgnoreCase("decimal") ? SwingConstants.RIGHT : SwingConstants.LEFT);
                    } else {
                        label.setHorizontalAlignment(SwingConstants.LEFT);
                    }

                    if (value instanceof String str && columnIndex >= 0 && columnTypes != null) {
                        String dataType = columnTypes.get(columnIndex);
                        if (dataType.equalsIgnoreCase("datetime") || dataType.equalsIgnoreCase("timestamp")) {
                            try {
                                java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
                                label.setText(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date));
                            } catch (java.text.ParseException e1) {
                                try {
                                    java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str);
                                    label.setText(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date));
                                } catch (java.text.ParseException e2) {
                                    try {
                                        java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd HH").parse(str);
                                        label.setText(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date));
                                    } catch (java.text.ParseException e3) {
                                        label.setText(str);
                                    }
                                }
                            }
                        }
                    }
                }
                return c;
            }
        });

        // Trong constructor của TableView
        // Tùy chỉnh phần tiêu đề cột
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setFont(Style.ROB_16);
                label.setBackground(Style.MAIN_CL);
                label.setForeground(Style.LIGHT_CL);
                label.setPreferredSize(new Dimension(label.getWidth(), 40));
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 1, 1, 1, Style.LIGHT_CL),
                    BorderFactory.createEmptyBorder(0, 20, 0, 15)
                ));

                List<String> columnTypes = parent.getColumnTypes();
                List<String> columnComments = parent.getColumnComments();
                String columnName = table.getColumnName(column);
                int columnIndex = columnComments != null ? columnComments.indexOf(columnName) : -1;
                if (columnIndex < 0 && parent.getColumnNames() != null) {
                    columnIndex = parent.getColumnNames().indexOf(columnName);
                }

                if (columnIndex >= 0 && columnTypes != null && columnIndex < columnTypes.size()) {
                    String dataType = columnTypes.get(columnIndex);
                    label.setHorizontalAlignment(dataType.equalsIgnoreCase("decimal") ? SwingConstants.RIGHT : SwingConstants.LEFT);
                } else {
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                }

                // Kiểm tra xem cột có phải là cột nút hay không
                int totalColumns = table.getColumnCount();
                int editCol = totalColumns - (UserSession.hasPermission(parent.getTableName(), "30") ? 2 : 1); // Cột "Sửa"/"Chi tiết"
                int deleteCol = totalColumns - 1; // Cột "Xóa" (nếu có)

                if (column == editCol || (UserSession.hasPermission(parent.getTableName(), "30") && column == deleteCol)) {
                    // Không hiển thị kí tự sắp xếp cho cột nút
                    label.setText(value != null ? value.toString() : "");
                } else {
                    // Hiển thị biểu tượng sắp xếp ở đầu tên cột cho cột dữ liệu
                    if (column == sortedColumn) {
                        String sortIcon = sortOrder == 1 ? "▲ " : sortOrder == 2 ? "▼ " : "● ";
                        label.setText(sortIcon + value);
                    } else {
                        label.setText("● " + value); // Kí tự tròn cho các cột không được sắp xếp
                    }
                }

                return label;
            }
        });

        // Thêm MouseListener để xử lý sự kiện nhấn vào tiêu đề cột
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int column = header.getColumnModel().getColumnIndexAtX(e.getX());
                if (column >= 0 && column < table.getColumnCount() - (parent.getColumnNames().size() < table.getColumnCount() ? 2 : 1)) { // Bỏ qua cột nút
                    handleSort(column);
                }
            }
        });

        view.add(header, BorderLayout.NORTH);
        view.add(table, BorderLayout.CENTER);
    }

    public JPanel getView() {
        return view;
    }

    public JTable getTable() {
        return table;
    }

    /**
     * Xử lý sắp xếp khi nhấn vào tiêu đề cột.
     */
    private void handleSort(int column) {
        if (sortedColumn == column) {
            sortOrder = (sortOrder + 1) % 3; // Chuyển đổi giữa 0 (mặc định), 1 (tăng), 2 (giảm)
        } else {
            sortedColumn = column;
            sortOrder = 1; // Bắt đầu với sắp xếp tăng
        }

        updateViewWithSort(originalData, parent.getColumnNames(), parent.getColumnComments(), 
                          parent.getPrimaryKeyColumns(), null, 
                          UserSession.hasPermission(parent.getTableName(), "10"),
                          UserSession.hasPermission(parent.getTableName(), "20"),
                          UserSession.hasPermission(parent.getTableName(), "30"));
    }

    /**
     * Cập nhật dữ liệu cho bảng, áp dụng sắp xếp nếu cần.
     */
    public void updateView(List<Map<String, String>> data, List<String> columnNames, List<String> columnComments, 
                          List<String> primaryKeyColumns, FormDialogHandler formDialogHandler, boolean canAdd, 
                          boolean canEdit, boolean canDelete) {
        originalData = data != null ? new ArrayList<>(data) : null; // Lưu dữ liệu gốc
        updateViewWithSort(data, columnNames, columnComments, primaryKeyColumns, formDialogHandler, canAdd, canEdit, canDelete);
    }

    private void updateViewWithSort(List<Map<String, String>> data, List<String> columnNames, List<String> columnComments, 
                                    List<String> primaryKeyColumns, FormDialogHandler formDialogHandler, boolean canAdd, 
                                    boolean canEdit, boolean canDelete) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        if (data == null || data.isEmpty()) {
            table.revalidate();
            table.repaint();
            return;
        }

        // Hiển thị tên cột và thêm cột chức năng
        List<String> displayNames = new ArrayList<>(columnComments);
        displayNames.add(""); // Cột Sửa hoặc Chi tiết
        if (canDelete) {
            displayNames.add(""); // Cột Xóa nếu có
        }

        tableModel.setColumnIdentifiers(displayNames.toArray());

        // Sắp xếp dữ liệu nếu cần
        List<Map<String, String>> sortedData = new ArrayList<>(data);
        if (sortOrder != 0 && sortedColumn >= 0 && sortedColumn < columnNames.size()) {
            String columnName = columnNames.get(sortedColumn);
            String dataType = parent.getColumnTypes().get(sortedColumn).toLowerCase();

            Comparator<Map<String, String>> comparator = (row1, row2) -> {
                String val1 = row1.get(columnName);
                String val2 = row2.get(columnName);
                if (val1 == null) val1 = "";
                if (val2 == null) val2 = "";

                if (dataType.contains("int") || dataType.contains("decimal") || dataType.contains("float")) {
                    try {
                        double d1 = Double.parseDouble(val1.isEmpty() ? "0" : val1);
                        double d2 = Double.parseDouble(val2.isEmpty() ? "0" : val2);
                        return Double.compare(d1, d2);
                    } catch (NumberFormatException e) {
                        return val1.compareTo(val2);
                    }
                } else if (dataType.contains("date") || dataType.contains("datetime") || dataType.contains("timestamp")) {
                    try {
                        long t1 = parent.parseDate(val1);
                        long t2 = parent.parseDate(val2);
                        return Long.compare(t1, t2);
                    } catch (java.text.ParseException e) {
                        return val1.compareTo(val2);
                    }
                } else {
                    return val1.compareToIgnoreCase(val2);
                }
            };

            if (sortOrder == 2) {
                comparator = comparator.reversed();
            }

            sortedData.sort(comparator);
        }

        for (Map<String, String> row : sortedData) {
            Object[] rowData = new Object[displayNames.size()];
            for (int i = 0; i < columnNames.size(); i++) {
                rowData[i] = row.get(columnNames.get(i));
            }

            rowData[columnNames.size()] = canEdit ? "Sửa" : "Chi tiết";
            if (canDelete) {
                rowData[columnNames.size() + 1] = "Xóa";
            }

            tableModel.addRow(rowData);
        }

        // Cài đặt nút Sửa hoặc Chi tiết
        int editCol = canDelete ? table.getColumnCount() - 2 : table.getColumnCount() - 1;
        TableColumn editButton = table.getColumnModel().getColumn(editCol);
        editButton.setCellRenderer(new ButtonRenderer());
        if (canEdit) {
            editButton.setCellEditor(new ButtonEditor(new JCheckBox(), "edit", formDialogHandler));
        } else {
            editButton.setCellEditor(new ButtonEditor(new JCheckBox(), "detail", formDialogHandler));
        }
        editButton.setPreferredWidth(canEdit ? 70 : 120);
        editButton.setMaxWidth(canEdit ? 70 : 120);
        editButton.setMinWidth(canEdit ? 70 : 120);
        editButton.setResizable(false);

        // Cài đặt nút Xóa nếu có
        if (canDelete) {
            int deleteCol = table.getColumnCount() - 1;
            TableColumn deleteButton = table.getColumnModel().getColumn(deleteCol);
            deleteButton.setCellRenderer(new ButtonRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (c instanceof JButton btn) {
                        btn.setForeground(Color.RED);
                    }
                    return c;
                }
            });
            deleteButton.setCellEditor(new ButtonEditor(new JCheckBox(), "delete", formDialogHandler));
            deleteButton.setPreferredWidth(70);
            deleteButton.setMaxWidth(70);
            deleteButton.setMinWidth(70);
            deleteButton.setResizable(false);
        }

        table.revalidate();
        table.repaint();
    }
}