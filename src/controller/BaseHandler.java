package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class BaseHandler implements HttpHandler, HttpHandlerLogic {
    protected static final String ACCOUNTS_DB_URL;
    protected static final String DATA_DB_URL;
    protected static final String DB_USERNAME;
    protected static final String DB_PASSWORD;
    protected static final String USER_PASS;
    protected static final String USER_NAME;
    static {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
            ACCOUNTS_DB_URL = props.getProperty("accounts.db.url");
            DATA_DB_URL = props.getProperty("data.db.url");
            DB_USERNAME = props.getProperty("db.username");
            DB_PASSWORD = props.getProperty("db.password");
            USER_PASS = props.getProperty("user.password");
            USER_NAME = props.getProperty("user.username");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage());
        }
    }

    @Override
    public boolean authenticate(HttpExchange exchange) throws IOException {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            sendResponse(exchange, 401, "{\"error\":\"Unauthorized\"}");
            return false;
        }

        String base64Credentials = authHeader.substring("Basic ".length());
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] parts = credentials.split(":");
        String username = parts[0];
        String password = parts.length > 1 ? parts[1] : "";

        try (Connection conn = DriverManager.getConnection(ACCOUNTS_DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Kiểm tra tài khoản
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT username, password, `Group` FROM users WHERE username = ?"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getString("password").equals(password)) {
                String group = rs.getString("Group");
                // Lấy quyền truy cập từ bảng group và manage
                Map<String, String> permissions = getPermissions(conn, group);
                UserSession.setUser(username, group, permissions);
                return true;
            } else {
                sendResponse(exchange, 401, "{\"error\":\"Unauthorized\"}");
                return false;
            }
        } catch (Exception e) {
            LogHandler.logError("Authentication error: " + e.getMessage(), e);
            sendResponse(exchange, 500, "{\"error\":\"Server error: " + e.getMessage() + "\"}");
            return false;
        }
    }

    private Map<String, String> getPermissions(Connection conn, String group) throws Exception {
        Map<String, String> permissions = new HashMap<>();
        // Lấy quyền từ bảng group
        PreparedStatement groupStmt = conn.prepareStatement(
            "SELECT a, b, c, d, e, f FROM `group` WHERE `Group` = ?"
        );
        groupStmt.setString(1, group);
        ResultSet groupRs = groupStmt.executeQuery();
        if (!groupRs.next()) {
            return permissions;
        }

        Map<String, String> groupPermissions = new HashMap<>();
        groupPermissions.put("a", groupRs.getString("a"));
        groupPermissions.put("b", groupRs.getString("b"));
        groupPermissions.put("c", groupRs.getString("c"));
        groupPermissions.put("d", groupRs.getString("d"));
        groupPermissions.put("e", groupRs.getString("e"));
        groupPermissions.put("f", groupRs.getString("f"));

        // Lấy danh sách bảng từ bảng manage
        PreparedStatement manageStmt = conn.prepareStatement(
            "SELECT TableName, TableGroup FROM manage WHERE TableGroup IN ('a', 'b', 'c', 'd', 'e', 'f')"
        );
        ResultSet manageRs = manageStmt.executeQuery();
        while (manageRs.next()) {
            String tableName = manageRs.getString("TableName");
            String tableGroup = manageRs.getString("TableGroup");
            permissions.put(tableName, groupPermissions.getOrDefault(tableGroup, "00"));
        }

        return permissions;
    }

    @Override
    public void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
            os.flush();
        }
    }
}