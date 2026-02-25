package controller;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableDataHandler extends BaseHandler implements TableDataInterface {
    private final TableDataOperations operations = new TableDataOperations();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String tableName = path.startsWith("/api/table/") ? path.substring("/api/table/".length()) : "";
        String[] pathParts = tableName.split("/");

        if (!authenticate(exchange)) {
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (!DatabaseUtil.isTableExists(conn, pathParts[0])) {
                sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Bảng không tồn tại");
                return;
            }

            switch (method) {
                case "GET":
                    handleGet(exchange, conn, pathParts[0]);
                    break;
                case "POST":
                    operations.handlePost(exchange, conn, pathParts[0]);
                    break;
                case "PUT":
                    if (pathParts.length >= 3 && "composite".equals(pathParts[1])) {
                        // Xử lý PUT với nhiều khóa chính
                        if ((pathParts.length - 2) % 2 != 0) {
                            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Số lượng tham số khóa chính không hợp lệ");
                            return;
                        }
                        List<String> primaryKeyColumns = new ArrayList<>();
                        Map<String, String> keyValues = new HashMap<>();
                        for (int i = 2; i < pathParts.length; i += 2) {
                            primaryKeyColumns.add(pathParts[i]);
                            if (i + 1 < pathParts.length) {
                                keyValues.put(pathParts[i], pathParts[i + 1]);
                            }
                        }
                        operations.handlePut(exchange, conn, pathParts[0], primaryKeyColumns, keyValues);
                    } else if (pathParts.length == 2) {
                        // Xử lý PUT với khóa chính đơn
                        operations.handlePut(exchange, conn, pathParts[0], pathParts[1]);
                    } else {
                        sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Định dạng đường dẫn không đúng");
                    }
                    break;
                case "DELETE":
                    if (pathParts.length >= 3 && "composite".equals(pathParts[1])) {
                        // Xử lý DELETE với nhiều khóa chính
                        if ((pathParts.length - 2) % 2 != 0) {
                            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Số lượng tham số khóa chính không hợp lệ");
                            return;
                        }
                        List<String> primaryKeyColumns = new ArrayList<>();
                        Map<String, String> keyValues = new HashMap<>();
                        for (int i = 2; i < pathParts.length; i += 2) {
                            primaryKeyColumns.add(pathParts[i]);
                            if (i + 1 < pathParts.length) {
                                keyValues.put(pathParts[i], pathParts[i + 1]);
                            }
                        }
                        operations.handleDelete(exchange, conn, pathParts[0], primaryKeyColumns, keyValues);
                    } else if (pathParts.length == 2) {
                        // Xử lý DELETE với khóa chính đơn
                        operations.handleDelete(exchange, conn, pathParts[0], pathParts[1]);
                    } else {
                        sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Định dạng đường dẫn không đúng");
                    }
                    break;
                default:
                    sendResponse(exchange, 405, "Phương thức không được phép");
                    break;
            }
        } catch (Exception e) {
            LogHandler.logError("Server error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "Lỗi server: " + e.getMessage());
        }
    }

    @Override
    public void handleGet(HttpExchange exchange, Connection conn, String tableName) throws Exception {
        String keyColumn = DatabaseUtil.getKeyColumn(conn, tableName);
        List<String> primaryKeyColumns = DatabaseUtil.getPrimaryKeys(conn, tableName);
        LogHandler.logInfo("Khóa chính Handler: " + keyColumn);
        LogHandler.logInfo("Primary key columns Handler: " + primaryKeyColumns);

        // Lấy metadata cột theo thứ tự ORDINAL_POSITION
        List<Map<String, String>> columns = new ArrayList<>();
        String query = "SELECT COLUMN_NAME AS name, COLUMN_COMMENT AS comment, DATA_TYPE AS dataType " +
                      "FROM INFORMATION_SCHEMA.COLUMNS " +
                      "WHERE TABLE_NAME = ? ORDER BY ORDINAL_POSITION";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tableName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> column = new LinkedHashMap<>();
                    String name = rs.getString("name");
                    String comment = rs.getString("comment") != null ? rs.getString("comment") : "";
                    String dataType = rs.getString("dataType");
                    column.put("name", name);
                    column.put("comment", comment);
                    column.put("dataType", dataType);
                    columns.add(column);
                }
            }
        }

        // Tạo JSON cho columns
        String columnsJson = JsonUtil.buildColumnsJsonFromList(columns);

        // Tạo JSON cho primaryKeyColumns
        String primaryKeysJson = JsonUtil.buildPrimaryKeysJson(primaryKeyColumns);

        // Lấy dữ liệu bảng
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tableName);
             ResultSet rs = stmt.executeQuery()) {
            String dataJson = JsonUtil.buildDataJson(rs);
            String json = "{\"keyColumn\":\"" + (keyColumn != null ? keyColumn : "") + 
                          "\",\"primaryKeyColumns\":" + primaryKeysJson + 
                          ",\"columns\":" + columnsJson + 
                          ",\"data\":" + dataJson + "}";
            sendResponse(exchange, 200, json);
        }
    }

    @Override
    public void handlePost(HttpExchange exchange, Connection conn, String tableName) throws Exception {
        operations.handlePost(exchange, conn, tableName);
    }

    @Override
    public void handlePut(HttpExchange exchange, Connection conn, String tableName, String keyValue) throws Exception {
        operations.handlePut(exchange, conn, tableName, keyValue);
    }

    @Override
    public void handlePut(HttpExchange exchange, Connection conn, String tableName, List<String> primaryKeyColumns, Map<String, String> keyValues) throws Exception {
        operations.handlePut(exchange, conn, tableName, primaryKeyColumns, keyValues);
    }

    @Override
    public void handleDelete(HttpExchange exchange, Connection conn, String tableName, String keyValue) throws Exception {
        operations.handleDelete(exchange, conn, tableName, keyValue);
    }

    @Override
    public void handleDelete(HttpExchange exchange, Connection conn, String tableName, List<String> primaryKeyColumns, Map<String, String> keyValues) throws Exception {
        operations.handleDelete(exchange, conn, tableName, primaryKeyColumns, keyValues);
    }
}