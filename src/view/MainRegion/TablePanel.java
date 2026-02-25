package view.MainRegion;

import controller.LogHandler;
import controller.UserSession;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.ApiClient;
import model.ApiClient.TableDataResult;
import view.Style;
import java.awt.event.*;

public class TablePanel extends JPanel implements TableViewDataHandler {
    private final JScrollPane scrollPane;
    private final ContentPanel parent;
    private boolean isButtonView = false;
    private String keyColumn;
    private List<String> primaryKeyColumns;
    private String tableName;
    private String tableComment;
    private List<String> columnNames;
    private List<String> columnComments;
    private List<String> columnTypes;
    private FormDialogHandler formDialogHandler;
    private TableView tableView;
    private GridView gridView;
    private JPanel currentView;
    private List<Map<String, String>> currentData;
    private JPanel filterPanel;
    private Map<String, Filter> columnFilters;
    private static final SimpleDateFormat[] DATE_PARSERS = {
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
        new SimpleDateFormat("yyyy-MM-dd HH:mm"),
        new SimpleDateFormat("yyyy-MM-dd HH"),
        new SimpleDateFormat("yyyy-MM-dd")
    };

    private static class Filter {
        String condition;
        String value;
        JTextField valueField;
        Component conditionComponent; // Thay JComboBox bằng Component để hỗ trợ JTextField

        Filter(String condition, String value, JTextField valueField, Component conditionComponent) {
            this.condition = condition;
            this.value = value;
            this.valueField = valueField;
            this.conditionComponent = conditionComponent;
        }
    }

    public TablePanel(ContentPanel parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        tableView = new TableView(this);
        gridView = new GridView(this);
        currentView = tableView.getView();
        columnFilters = new HashMap<>();

        filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        filterPanel.setBackground(Style.LIGHT_CL);

        scrollPane = new JScrollPane(currentView);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new Style.CustomScrollBarUI());
        verticalScrollBar.setPreferredSize(new Dimension(13, Integer.MAX_VALUE));
        verticalScrollBar.setUnitIncrement(20);
        verticalScrollBar.setOpaque(true);
        verticalScrollBar.setBackground(Style.LIGHT_CL);
        verticalScrollBar.setBorder(new EmptyBorder(0, 5, 0, 0));

        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        formDialogHandler = new FormDialogPanel(this);
    }

    public void setButtonView(boolean isButtonView) {
        if (this.isButtonView != isButtonView) {
            this.isButtonView = isButtonView;
            currentView = isButtonView ? gridView.getView() : tableView.getView();
            scrollPane.setViewportView(currentView);
            scrollPane.revalidate();
            scrollPane.repaint();
            applyFilters();
        }
    }

    public void showAddFormDialog() {
        if (tableName == null || tableName.isEmpty() || columnNames == null || columnNames.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Vui lòng chọn một bảng trước", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        formDialogHandler.showFormDialog("add", -1);
    }

    @Override
    public void updateTableData(List<Map<String, String>> data, Map<String, String> columnCommentsMap, 
                               Map<String, String> columnTypesMap, String keyColumn, 
                               String tableName, String tableComment) {
        updateTableData(data, columnCommentsMap, columnTypesMap, keyColumn, new ArrayList<>(), tableName, tableComment);
    }

    public void updateTableData(List<Map<String, String>> data, Map<String, String> columnCommentsMap, 
                               Map<String, String> columnTypesMap, String keyColumn, List<String> primaryKeyColumns, 
                               String tableName, String tableComment) {
        this.keyColumn = keyColumn;
        this.primaryKeyColumns = primaryKeyColumns;
        this.tableName = tableName;
        this.tableComment = tableComment;
        this.currentData = data != null ? new ArrayList<>(data) : null;
        this.columnNames = data != null && !data.isEmpty() ? new ArrayList<>(data.get(0).keySet()) : null;
        this.columnComments = new ArrayList<>();
        this.columnTypes = new ArrayList<>();

        if (columnNames != null) {
            for (String columnName : columnNames) {
                String comment = columnCommentsMap != null ? columnCommentsMap.getOrDefault(columnName, columnName) : columnName;
                String type = columnTypesMap != null ? columnTypesMap.getOrDefault(columnName, "unknown") : "unknown";
                this.columnComments.add(comment);
                this.columnTypes.add(type);
            }
        }
        
        LogHandler.logInfo("Khóa chính TablePanel: " + keyColumn);
        LogHandler.logInfo("Các cột khóa chính TablePanel: " + primaryKeyColumns);
        LogHandler.logInfo("Tên bảng TablePanel: " + tableName);
        LogHandler.logInfo("Chú thích bảng TablePanel: " + tableComment);
        LogHandler.logInfo("Kiểu dữ liệu cột: " + columnTypes);

        boolean canAdd = UserSession.hasPermission(tableName, "10");
        boolean canEdit = UserSession.hasPermission(tableName, "20");
        boolean canDelete = UserSession.hasPermission(tableName, "30");

        this.formDialogHandler = new FormDialogPanel(this);
        updateFilterPanel();
        applyFilters();
    }

    private void updateFilterPanel() {
        filterPanel.removeAll();
        columnFilters.clear();

        if (columnNames == null || columnTypes == null) {
            filterPanel.revalidate();
            filterPanel.repaint();
            return;
        }

        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            String columnComment = columnComments.get(i);
            String dataType = columnTypes.get(i).toLowerCase();

            JPanel filterColumnPanel = new JPanel(new BorderLayout(5, 5));
            filterColumnPanel.setBackground(Style.LIGHT_CL);

            JLabel label = new JLabel(columnComment + ":");
            label.setFont(Style.ROB_B14);
            filterColumnPanel.add(label, BorderLayout.NORTH);

            if (dataType.contains("varchar") || dataType.contains("text") || dataType.contains("char") || dataType.contains("enum")) {
                JTextField filterField = new JTextField(10);
                filterField.setFont(Style.ROB_B14);
                filterField.setPreferredSize(new Dimension(100, 40));
                filterField.setMaximumSize(new Dimension(100, 40));
                filterField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Style.MAIN_CL),  
                    BorderFactory.createEmptyBorder(0, 10, 0, 10)
                ));
                filterField.addActionListener(e -> {
                    columnFilters.put(columnName, new Filter(null, filterField.getText().trim(), filterField, null));
                    applyFilters();
                });
                filterColumnPanel.add(filterField, BorderLayout.CENTER);
                columnFilters.put(columnName, new Filter(null, "", filterField, null));
            } else if (dataType.contains("int") || dataType.contains("decimal") || dataType.contains("float") ||
                       dataType.contains("date") || dataType.contains("datetime") || dataType.contains("timestamp")) {
                JPanel filterItemPanel = new JPanel(new BorderLayout());
                filterItemPanel.setBackground(Color.WHITE);
                filterItemPanel.setPreferredSize(new Dimension(140, 25));
                filterItemPanel.setMaximumSize(new Dimension(140, 25));
                filterItemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Style.MAIN_CL),
                    BorderFactory.createEmptyBorder(0, 10, 0, 10)
                ));

                // Text field hiển thị dấu điều kiện (thay cho JComboBox)
                JTextField conditionField = new JTextField("=");
                conditionField.setFont(Style.ROB_B14);
                conditionField.setBorder(null);
                conditionField.setBackground(Color.WHITE);
                conditionField.setPreferredSize(new Dimension(20, 25));
                conditionField.setEditable(false);
                conditionField.setHorizontalAlignment(JTextField.CENTER);
                
                JTextField filterField = new JTextField();
                // Tạo JPopupMenu để hiển thị danh sách điều kiện
                JPopupMenu conditionPopup = new JPopupMenu();
                String[] conditions = {">", ">=", "=", "<=", "<"};
                for (String condition : conditions) {
                    JMenuItem item = new JMenuItem(condition);
                    item.addActionListener(e -> {
                        conditionField.setText(condition);
                        columnFilters.put(columnName, new Filter(
                            condition,
                            filterField.getText().trim(),
                            filterField,
                            conditionField
                        ));
                        applyFilters();
                    });
                    conditionPopup.add(item);
                }

                // Hiển thị popup khi click vào conditionField
                conditionField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        conditionPopup.show(conditionField, 0, conditionField.getHeight());
                    }
                });

                filterField.setFont(Style.ROB_B14);
                filterField.setBorder(null);
                filterField.setHorizontalAlignment(JTextField.LEFT);

                // Thêm các thành phần vào panel
                filterItemPanel.add(conditionField, BorderLayout.WEST);
                filterItemPanel.add(filterField, BorderLayout.CENTER);

                filterColumnPanel.add(filterItemPanel, BorderLayout.CENTER);

                // Gắn filter logic
                filterField.addActionListener(e -> {
                    columnFilters.put(columnName, new Filter(
                        conditionField.getText(),
                        filterField.getText().trim(),
                        filterField,
                        conditionField
                    ));
                    applyFilters();
                });

                columnFilters.put(columnName, new Filter("=", "", filterField, conditionField));
            } else {
                JTextField filterField = new JTextField(10);
                filterField.setFont(Style.ROB_B14);
                filterField.addActionListener(e -> {
                    columnFilters.put(columnName, new Filter(null, filterField.getText().trim(), filterField, null));
                    applyFilters();
                });
                filterColumnPanel.add(filterField, BorderLayout.CENTER);
                columnFilters.put(columnName, new Filter(null, "", filterField, null));
            }

            filterPanel.add(filterColumnPanel);
            filterPanel.add(Box.createHorizontalStrut(10));
        }

        filterPanel.revalidate();
        filterPanel.repaint();
    }

    private void applyFilters() {
        if (currentData == null) {
            tableView.updateView(null, columnNames, columnComments, primaryKeyColumns, formDialogHandler, 
                                 UserSession.hasPermission(tableName, "10"), 
                                 UserSession.hasPermission(tableName, "20"), 
                                 UserSession.hasPermission(tableName, "30"));
            gridView.updateView(null, columnNames, columnComments, columnTypes, primaryKeyColumns, formDialogHandler, 
                               UserSession.hasPermission(tableName, "10"), 
                               UserSession.hasPermission(tableName, "20"), 
                               UserSession.hasPermission(tableName, "30"));
            return;
        }

        List<Map<String, String>> filteredData = currentData.stream()
            .filter(row -> {
                for (String column : columnNames) {
                    Filter filter = columnFilters.get(column);
                    if (filter == null || filter.value.isEmpty()) {
                        continue;
                    }

                    String value = row.get(column);
                    if (value == null) {
                        value = "";
                    }

                    String dataType = columnTypes.get(columnNames.indexOf(column)).toLowerCase();
                    if (dataType.contains("varchar") || dataType.contains("text") || dataType.contains("char")) {
                        if (!value.toLowerCase().contains(filter.value.toLowerCase())) {
                            return false;
                        }
                    } else if (dataType.contains("int") || dataType.contains("decimal") || dataType.contains("float")) {
                        try {
                            double rowValue = Double.parseDouble(value);
                            double filterValue = Double.parseDouble(filter.value);
                            switch (filter.condition) {
                                case ">":
                                    if (!(rowValue > filterValue)) return false;
                                    break;
                                case ">=":
                                    if (!(rowValue >= filterValue)) return false;
                                    break;
                                case "=":
                                    if (!(Math.abs(rowValue - filterValue) < 0.0001)) return false;
                                    break;
                                case "<=":
                                    if (!(rowValue <= filterValue)) return false;
                                    break;
                                case "<":
                                    if (!(rowValue < filterValue)) return false;
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    } else if (dataType.contains("date") || dataType.contains("datetime") || dataType.contains("timestamp")) {
                        try {
                            long rowTime = parseDate(value);
                            long filterTime = parseDate(filter.value);
                            switch (filter.condition) {
                                case ">":
                                    if (!(rowTime > filterTime)) return false;
                                    break;
                                case ">=":
                                    if (!(rowTime >= filterTime)) return false;
                                    break;
                                case "=":
                                    if (!(rowTime == filterTime)) return false;
                                    break;
                                case "<=":
                                    if (!(rowTime <= filterTime)) return false;
                                    break;
                                case "<":
                                    if (!(rowTime < filterTime)) return false;
                                    break;
                            }
                        } catch (ParseException e) {
                            return false;
                        }
                    }
                }
                return true;
            })
            .collect(Collectors.toList());

        boolean canAdd = UserSession.hasPermission(tableName, "10");
        boolean canEdit = UserSession.hasPermission(tableName, "20");
        boolean canDelete = UserSession.hasPermission(tableName, "30");

        tableView.updateView(filteredData, columnNames, columnComments, primaryKeyColumns, formDialogHandler, 
                            canAdd, canEdit, canDelete);
        gridView.updateView(filteredData, columnNames, columnComments, columnTypes, primaryKeyColumns, formDialogHandler, 
                           canAdd, canEdit, canDelete);

        currentView.revalidate();
        currentView.repaint();
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    long parseDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new ParseException("Empty date string", 0);
        }
        for (SimpleDateFormat parser : DATE_PARSERS) {
            try {
                return parser.parse(dateStr).getTime();
            } catch (ParseException ignored) {
            }
        }
        throw new ParseException("Invalid date format: " + dateStr, 0);
    }

    @Override
    public void refreshTable() {
        if (tableName == null || tableName.isEmpty()) {
            updateTableData(null, null, null, null, new ArrayList<>(), tableName, tableComment);
            return;
        }
        try {
            TableDataResult result = ApiClient.getTableData(tableName);
            if (result.data != null && !result.data.isEmpty()) {
                updateTableData(result.data, result.columnComments, result.columnTypes, 
                               result.keyColumn, result.primaryKeyColumns, tableName, tableComment);
            } else {
                updateTableData(null, null, null, null, new ArrayList<>(), tableName, tableComment);
                JOptionPane.showMessageDialog(parent, "Không có dữ liệu để hiển thị sau khi làm mới", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            LogHandler.logError("Lỗi khi làm mới dữ liệu: " + ex.getMessage(), ex);
            JOptionPane.showMessageDialog(parent, "Lỗi khi làm mới dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getKeyColumn() {
        return keyColumn;
    }

    public List<String> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    @Override
    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<String> getColumnComments() {
        return columnComments;
    }

    public List<String> getColumnTypes() {
        return columnTypes;
    }

    public JTable getTable() {
        return tableView.getTable();
    }

    ContentPanel getContentPanel() {
        return parent;
    }
}