package controller;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

public class TableDataOperations {
    public void handlePost(HttpExchange exchange, Connection conn, String tableName) throws Exception {
        // Đọc body yêu cầu
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "UTF-8"));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        reader.close();

        // Log nội dung body
        LogHandler.logInfo("POST request body for table " + tableName + ": " + requestBody.toString());

        // Kiểm tra body rỗng
        if (requestBody.length() == 0) {
            LogHandler.logError("POST request body is empty for table: " + tableName);
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Body rỗng");
            return;
        }

        // Kiểm tra Content-Type
        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        if (!"application/json".equalsIgnoreCase(contentType)) {
            LogHandler.logError("Invalid Content-Type: " + contentType + " for table: " + tableName);
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Content-Type phải là application/json");
            return;
        }

        // Parse JSON
        Map<String, String> data;
        try {
            data = JsonUtil.parseJson(requestBody.toString());
        } catch (Exception e) {
            LogHandler.logError("JSON parse failed for table " + tableName + ": " + e.getMessage() + ", Input: " + requestBody.toString());
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Dữ liệu JSON không hợp lệ");
            return;
        }

        if (data.isEmpty()) {
            LogHandler.logError("Parsed JSON is empty for table " + tableName + ", Input: " + requestBody.toString());
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Dữ liệu JSON không hợp lệ");
            return;
        }

        Map<String, Boolean> validColumns = DatabaseUtil.getValidColumns(conn, tableName);
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        boolean first = true;
        for (String column : data.keySet()) {
            if (!validColumns.containsKey(column)) {
                LogHandler.logError("Invalid column " + column + " for table: " + tableName);
                sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Cột không hợp lệ " + column);
                return;
            }
            if (!first) {
                columns.append(",");
                placeholders.append(",");
            }
            columns.append(column);
            placeholders.append("?");
            first = false;
        }

        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
        PreparedStatement stmt = conn.prepareStatement(sql);
        int index = 1;
        for (String column : data.keySet()) {
            stmt.setString(index++, data.get(column));
        }

        try {
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                sendResponse(exchange, 201, "{\"message\":\"Thêm hàng thành công\"}");
            } else {
                sendResponse(exchange, 500, "Lỗi server: Không thể thêm hàng");
            }
        } catch (java.sql.SQLException e) {
            LogHandler.logError("SQL error during POST for table " + tableName + ": " + e.getMessage());
            if (e.getErrorCode() == 1062) {
                sendResponse(exchange, 409, "Lỗi: Giá trị trùng lặp");
            } else {
                throw e;
            }
        }
    }

    public void handlePut(HttpExchange exchange, Connection conn, String tableName, String keyValue) throws Exception {
        LogHandler.logInfo("Xử lý PUT cho bảng: " + tableName + ", Giá trị khóa chính: " + keyValue);

        String keyColumn = DatabaseUtil.getKeyColumn(conn, tableName);
        if (keyColumn == null) {
            LogHandler.logError("Không tìm thấy khóa chính cho bảng: " + tableName);
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Bảng không có khóa chính");
            return;
        }
        LogHandler.logInfo("Khóa chính: " + keyColumn);

        // Đọc body yêu cầu
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "UTF-8"));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        reader.close();
        LogHandler.logInfo("PUT request body for table " + tableName + ": " + requestBody.toString());

        // Kiểm tra body rỗng
        if (requestBody.length() == 0) {
            LogHandler.logError("PUT request body is empty for table: " + tableName);
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Body rỗng");
            return;
        }

        // Kiểm tra Content-Type
        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        if (!"application/json".equalsIgnoreCase(contentType)) {
            LogHandler.logError("Invalid Content-Type: " + contentType + " for table: " + tableName);
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Content-Type phải là application/json");
            return;
        }

        // Parse JSON
        Map<String, String> data;
        try {
            data = JsonUtil.parseJson(requestBody.toString());
        } catch (Exception e) {
            LogHandler.logError("JSON parse failed for table " + tableName + ": " + e.getMessage() + ", Input: " + requestBody.toString());
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Dữ liệu JSON không hợp lệ");
            return;
        }

        if (data.isEmpty()) {
            LogHandler.logError("Parsed JSON is empty for table " + tableName + ", Input: " + requestBody.toString());
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Dữ liệu JSON không hợp lệ");
            return;
        }

        Map<String, Boolean> validColumns = DatabaseUtil.getValidColumns(conn, tableName);
        StringBuilder setClause = new StringBuilder();
        boolean first = true;
        for (String column : data.keySet()) {
            if (!validColumns.containsKey(column)) {
                LogHandler.logError("Invalid column " + column + " for table: " + tableName);
                sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Cột không hợp lệ " + column);
                return;
            }
            if (!first) {
                setClause.append(",");
            }
            setClause.append(column).append("=?");
            first = false;
        }

        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + keyColumn + "=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        int index = 1;
        for (String column : data.keySet()) {
            stmt.setString(index++, data.get(column));
        }
        stmt.setString(index, keyValue);

        try {
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                sendResponse(exchange, 200, "{\"message\":\"Cập nhật hàng thành công\"}");
            } else {
                sendResponse(exchange, 404, "Không tìm thấy: Hàng với giá trị khóa chính " + keyValue + " không tồn tại");
            }
        } catch (java.sql.SQLException e) {
            LogHandler.logError("SQL error during PUT for table " + tableName + ": " + e.getMessage());
            if (e.getErrorCode() == 1062) {
                sendResponse(exchange, 409, "Lỗi: Giá trị trùng lặp");
            } else {
                throw e;
            }
        }
    }

    // Phương thức mới: handlePut với nhiều khóa chính
    public void handlePut(HttpExchange exchange, Connection conn, String tableName, List<String> primaryKeyColumns, Map<String, String> keyValues) throws Exception {
        LogHandler.logInfo("Xử lý PUT cho bảng: " + tableName + ", Các khóa chính: " + primaryKeyColumns + ", Giá trị khóa: " + keyValues);

        // Kiểm tra primaryKeyColumns
        if (primaryKeyColumns == null || primaryKeyColumns.isEmpty()) {
            LogHandler.logError("Danh sách khóa chính rỗng hoặc null cho bảng: " + tableName);
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Danh sách khóa chính rỗng");
            return;
        }

        // Kiểm tra keyValues
        if (keyValues == null || keyValues.isEmpty()) {
            LogHandler.logError("Giá trị khóa chính rỗng hoặc null cho bảng: " + tableName);
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Giá trị khóa chính rỗng");
            return;
        }

        // Kiểm tra tính hợp lệ của primaryKeyColumns
        Map<String, Boolean> validColumns = DatabaseUtil.getValidColumns(conn, tableName);
        for (String keyCol : primaryKeyColumns) {
            if (!validColumns.containsKey(keyCol)) {
                LogHandler.logError("Khóa chính không hợp lệ " + keyCol + " cho bảng: " + tableName);
                sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Khóa chính không hợp lệ " + keyCol);
                return;
            }
            if (!keyValues.containsKey(keyCol)) {
                LogHandler.logError("Thiếu giá trị cho khóa chính " + keyCol + " cho bảng: " + tableName);
                sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Thiếu giá trị cho khóa chính " + keyCol);
                return;
            }
        }

        // Đọc body yêu cầu
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "UTF-8"));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        reader.close();
        LogHandler.logInfo("PUT request body for table " + tableName + ": " + requestBody.toString());

        // Kiểm tra body rỗng
        if (requestBody.length() == 0) {
            LogHandler.logError("PUT request body is empty for table: " + tableName);
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Body rỗng");
            return;
        }

        // Kiểm tra Content-Type
        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        if (!"application/json".equalsIgnoreCase(contentType)) {
            LogHandler.logError("Invalid Content-Type: " + contentType + " for table: " + tableName);
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Content-Type phải là application/json");
            return;
        }

        // Parse JSON
        Map<String, String> data;
        try {
            data = JsonUtil.parseJson(requestBody.toString());
        } catch (Exception e) {
            LogHandler.logError("JSON parse failed for table " + tableName + ": " + e.getMessage() + ", Input: " + requestBody.toString());
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Dữ liệu JSON không hợp lệ");
            return;
        }

        if (data.isEmpty()) {
            LogHandler.logError("Parsed JSON is empty for table " + tableName + ", Input: " + requestBody.toString());
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Dữ liệu JSON không hợp lệ");
            return;
        }

        // Kiểm tra cột hợp lệ
        for (String column : data.keySet()) {
            if (!validColumns.containsKey(column)) {
                LogHandler.logError("Invalid column " + column + " for table: " + tableName);
                sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Cột không hợp lệ " + column);
                return;
            }
        }

        // Xây dựng setClause cho UPDATE
        StringBuilder setClause = new StringBuilder();
        boolean first = true;
        for (String column : data.keySet()) {
            if (!first) {
                setClause.append(",");
            }
            setClause.append(column).append("=?");
            first = false;
        }

        // Xây dựng whereClause cho nhiều khóa chính
        StringBuilder whereClause = new StringBuilder();
        first = true;
        for (String keyCol : primaryKeyColumns) {
            if (!first) {
                whereClause.append(" AND ");
            }
            whereClause.append(keyCol).append("=?");
            first = false;
        }

        // Tạo câu lệnh SQL
        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereClause;
        PreparedStatement stmt = conn.prepareStatement(sql);

        // Gán giá trị cho các cột trong SET
        int index = 1;
        for (String column : data.keySet()) {
            stmt.setString(index++, data.get(column));
        }

        // Gán giá trị cho các cột trong WHERE
        for (String keyCol : primaryKeyColumns) {
            stmt.setString(index++, keyValues.get(keyCol));
        }

        try {
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                sendResponse(exchange, 200, "{\"message\":\"Cập nhật hàng thành công\"}");
            } else {
                sendResponse(exchange, 404, "Không tìm thấy: Hàng với các giá trị khóa chính " + keyValues + " không tồn tại");
            }
        } catch (java.sql.SQLException e) {
            LogHandler.logError("SQL error during PUT for table " + tableName + ": " + e.getMessage());
            if (e.getErrorCode() == 1062) {
                sendResponse(exchange, 409, "Lỗi: Giá trị trùng lặp");
            } else {
                throw e;
            }
        }
    }

    public void handleDelete(HttpExchange exchange, Connection conn, String tableName, String keyValue) throws Exception {
        LogHandler.logInfo("Xử lý DELETE cho bảng: " + tableName + ", Giá trị khóa chính: " + keyValue);

        String keyColumn = DatabaseUtil.getKeyColumn(conn, tableName);
        if (keyColumn == null) {
            LogHandler.logError("Không tìm thấy khóa chính cho bảng: " + tableName);
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Bảng không có khóa chính");
            return;
        }

        String sql = "DELETE FROM " + tableName + " WHERE " + keyColumn + "=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, keyValue);

        try {
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                sendResponse(exchange, 200, "{\"message\":\"Xóa hàng thành công\"}");
            } else {
                sendResponse(exchange, 404, "Không tìm thấy: Hàng với giá trị khóa chính " + keyValue + " không tồn tại");
            }
        } catch (java.sql.SQLException e) {
            LogHandler.logError("SQL error during DELETE for table " + tableName + ": " + e.getMessage());
            throw e;
        }
    }

    // Phương thức mới: handleDelete với nhiều khóa chính
    public void handleDelete(HttpExchange exchange, Connection conn, String tableName, List<String> primaryKeyColumns, Map<String, String> keyValues) throws Exception {
        LogHandler.logInfo("Xử lý DELETE cho bảng: " + tableName + ", Các khóa chính: " + primaryKeyColumns + ", Giá trị khóa: " + keyValues);

        // Kiểm tra primaryKeyColumns
        if (primaryKeyColumns == null || primaryKeyColumns.isEmpty()) {
            LogHandler.logError("Danh sách khóa chính rỗng hoặc null cho bảng: " + tableName);
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Danh sách khóa chính rỗng");
            return;
        }

        // Kiểm tra keyValues
        if (keyValues == null || keyValues.isEmpty()) {
            LogHandler.logError("Giá trị khóa chính rỗng hoặc null cho bảng: " + tableName);
            sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Giá trị khóa chính rỗng");
            return;
        }

        // Kiểm tra tính hợp lệ của primaryKeyColumns
        Map<String, Boolean> validColumns = DatabaseUtil.getValidColumns(conn, tableName);
        for (String keyCol : primaryKeyColumns) {
            if (!validColumns.containsKey(keyCol)) {
                LogHandler.logError("Khóa chính không hợp lệ " + keyCol + " cho bảng: " + tableName);
                sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Khóa chính không hợp lệ " + keyCol);
                return;
            }
            if (!keyValues.containsKey(keyCol)) {
                LogHandler.logError("Thiếu giá trị cho khóa chính " + keyCol + " cho bảng: " + tableName);
                sendResponse(exchange, 400, "Yêu cầu không hợp lệ: Thiếu giá trị cho khóa chính " + keyCol);
                return;
            }
        }

        // Xây dựng whereClause cho nhiều khóa chính
        StringBuilder whereClause = new StringBuilder();
        boolean first = true;
        for (String keyCol : primaryKeyColumns) {
            if (!first) {
                whereClause.append(" AND ");
            }
            whereClause.append(keyCol).append("=?");
            first = false;
        }

        // Tạo câu lệnh SQL
        String sql = "DELETE FROM " + tableName + " WHERE " + whereClause;
        PreparedStatement stmt = conn.prepareStatement(sql);

        // Gán giá trị cho các cột trong WHERE
        int index = 1;
        for (String keyCol : primaryKeyColumns) {
            stmt.setString(index++, keyValues.get(keyCol));
        }

        try {
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                sendResponse(exchange, 200, "{\"message\":\"Xóa hàng thành công\"}");
            } else {
                sendResponse(exchange, 404, "Không tìm thấy: Hàng với các giá trị khóa chính " + keyValues + " không tồn tại");
            }
        } catch (java.sql.SQLException e) {
            LogHandler.logError("SQL error during DELETE for table " + tableName + ": " + e.getMessage());
            throw e;
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (var os = exchange.getResponseBody()) {
            os.write(responseBytes);
            os.flush();
        }
    }
}