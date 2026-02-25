package controller;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TablesHandler extends BaseHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }
        if (!authenticate(exchange)) return;

        String schema = extractSchemaFromUrl(DATA_DB_URL);

        try (Connection conn = DriverManager.getConnection(DATA_DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT TABLE_NAME, TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES " +
                 "WHERE TABLE_SCHEMA = ? AND TABLE_TYPE = 'BASE TABLE'")) {
            stmt.setString(1, schema);
            LogHandler.logInfo("Executing query: " + stmt.toString());
            ResultSet rs = stmt.executeQuery();
            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                String name = rs.getString("TABLE_NAME");
                String comment = rs.getString("TABLE_COMMENT");
                if (comment == null || comment.isEmpty()) comment = name;
                json.append("{\"name\":\"").append(name).append("\",\"comment\":\"").append(comment).append("\"}");
                first = false;
            }
            json.append("]");
            sendResponse(exchange, 200, json.toString());
        } catch (Exception e) {
            LogHandler.logError("Server error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "Server error: " + e.getMessage());
        }
    }

    private String extractSchemaFromUrl(String url) {
        if (url == null || !url.contains("/")) return "ql_khachsan";
        String[] parts = url.split("/");
        return parts[parts.length - 1].split("\\?")[0];
    }
}