package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtil {
    private static final String SCHEMA = BaseHandler.DATA_DB_URL.substring(BaseHandler.DATA_DB_URL.lastIndexOf("/") + 1);

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(BaseHandler.DATA_DB_URL, BaseHandler.DB_USERNAME, BaseHandler.DB_PASSWORD);
    }

    public static Connection getAccountsConnection() throws Exception {
        return DriverManager.getConnection(BaseHandler.ACCOUNTS_DB_URL, BaseHandler.DB_USERNAME, BaseHandler.DB_PASSWORD);
    }

    public static boolean isTableExists(Connection conn, String tableName) throws Exception {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?"
        );
        stmt.setString(1, SCHEMA);
        stmt.setString(2, tableName);
        ResultSet rs = stmt.executeQuery();
        boolean exists = rs.next();
        LogHandler.logInfo("Kiểm tra bảng " + tableName + ": " + (exists ? "tồn tại" : "không tồn tại"));
        return exists;
    }

    public static String getKeyColumn(Connection conn, String tableName) throws Exception {
        PreparedStatement pkStmt = conn.prepareStatement(
                "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? AND CONSTRAINT_NAME = 'PRIMARY'"
        );
        pkStmt.setString(1, SCHEMA);
        pkStmt.setString(2, tableName);
        ResultSet pkRs = pkStmt.executeQuery();
        String keyColumn = pkRs.next() ? pkRs.getString("COLUMN_NAME") : null;
        LogHandler.logInfo("Truy vấn khóa chính cho " + tableName + ": " + (keyColumn != null ? keyColumn : "không tìm thấy"));
        return keyColumn;
    }

    public static List<String> getPrimaryKeys(Connection conn, String tableName) throws Exception {
        List<String> primaryKeys = new ArrayList<>();
        PreparedStatement pkStmt = conn.prepareStatement(
                "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? AND CONSTRAINT_NAME = 'PRIMARY' " +
                "ORDER BY ORDINAL_POSITION"
        );
        pkStmt.setString(1, SCHEMA);
        pkStmt.setString(2, tableName);
        ResultSet pkRs = pkStmt.executeQuery();
        while (pkRs.next()) {
            String columnName = pkRs.getString("COLUMN_NAME");
            primaryKeys.add(columnName);
        }
        LogHandler.logInfo("Primary keys for " + tableName + ": " + (primaryKeys.isEmpty() ? "none found" : primaryKeys));
        return primaryKeys;
    }

    public static Map<String, Map<String, String>> getColumnMetadata(Connection conn, String tableName) throws Exception {
        Map<String, Map<String, String>> columnMetadata = new HashMap<>();
        PreparedStatement metaStmt = conn.prepareStatement(
                "SELECT COLUMN_NAME, COLUMN_COMMENT, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?"
        );
        metaStmt.setString(1, SCHEMA);
        metaStmt.setString(2, tableName);
        ResultSet metaRs = metaStmt.executeQuery();
        while (metaRs.next()) {
            String columnName = metaRs.getString("COLUMN_NAME");
            String comment = metaRs.getString("COLUMN_COMMENT");
            String dataType = metaRs.getString("DATA_TYPE");
            if (comment == null || comment.isEmpty()) comment = columnName;
            Map<String, String> metadata = new HashMap<>();
            metadata.put("comment", comment);
            metadata.put("dataType", dataType);
            columnMetadata.put(columnName, metadata);
        }
        return columnMetadata;
    }

    public static Map<String, Boolean> getValidColumns(Connection conn, String tableName) throws Exception {
        Map<String, Boolean> validColumns = new HashMap<>();
        PreparedStatement metaStmt = conn.prepareStatement(
                "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?"
        );
        metaStmt.setString(1, SCHEMA);
        metaStmt.setString(2, tableName);
        ResultSet metaRs = metaStmt.executeQuery();
        while (metaRs.next()) {
            validColumns.put(metaRs.getString("COLUMN_NAME"), true);
        }
        return validColumns;
    }
}