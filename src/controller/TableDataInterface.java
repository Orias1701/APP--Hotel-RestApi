package controller;

import com.sun.net.httpserver.HttpExchange;
import java.sql.Connection;
import java.util.Map;

public interface TableDataInterface {
    void handleGet(HttpExchange exchange, Connection conn, String tableName) throws Exception;
    void handlePost(HttpExchange exchange, Connection conn, String tableName) throws Exception;
    void handlePut(HttpExchange exchange, Connection conn, String tableName, String keyValue) throws Exception;
    void handleDelete(HttpExchange exchange, Connection conn, String tableName, String keyValue) throws Exception;

    // Phương thức mới cho nhiều khóa chính
    void handlePut(HttpExchange exchange, Connection conn, String tableName, java.util.List<String> primaryKeyColumns, Map<String, String> keyValues) throws Exception;
    void handleDelete(HttpExchange exchange, Connection conn, String tableName, java.util.List<String> primaryKeyColumns, Map<String, String> keyValues) throws Exception;
}