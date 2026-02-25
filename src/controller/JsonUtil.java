package controller;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    public static Map<String, String> parseJson(String json) {
        try {
            if (json == null || json.trim().isEmpty()) {
                LogHandler.logError("JSON input is null or empty");
                throw new IllegalArgumentException("JSON input is null or empty");
            }
            json = json.trim();
            if (!json.startsWith("{") || !json.endsWith("}")) {
                LogHandler.logError("Invalid JSON: Not an object, Input: " + json);
                throw new IllegalArgumentException("Invalid JSON: Must be an object");
            }

            Map<String, String> result = new LinkedHashMap<>();
            String content = json.substring(1, json.length() - 1).trim();
            if (content.isEmpty()) {
                LogHandler.logError("Parsed JSON is empty, Input: " + json);
                return result;
            }

            String[] pairs = splitJsonPairs(content);
            for (String pair : pairs) {
                String[] keyValue = splitKeyValue(pair);
                if (keyValue.length != 2) {
                    LogHandler.logError("Invalid key-value pair: " + pair + ", Input: " + json);
                    throw new IllegalArgumentException("Invalid JSON: Malformed key-value pair");
                }
                String key = unescapeJsonString(keyValue[0].trim());
                String value = unescapeJsonString(keyValue[1].trim());
                if (!key.startsWith("\"") || !key.endsWith("\"")) {
                    LogHandler.logError("Invalid JSON key: " + key + ", Input: " + json);
                    throw new IllegalArgumentException("Invalid JSON: Keys must be strings");
                }
                if (!value.startsWith("\"") || !value.endsWith("\"")) {
                    LogHandler.logError("Invalid JSON value: " + value + ", Input: " + json);
                    throw new IllegalArgumentException("Invalid JSON: Values must be strings");
                }
                result.put(key.substring(1, key.length() - 1), value.substring(1, value.length() - 1));
            }

            if (result.isEmpty()) {
                LogHandler.logError("Parsed JSON is empty, Input: " + json);
            } else {
                LogHandler.logInfo("Successfully parsed JSON: " + json);
            }
            return result;
        } catch (Exception e) {
            LogHandler.logError("Failed to parse JSON: " + e.getMessage() + ", Input: " + json);
            throw new RuntimeException("Invalid JSON: " + e.getMessage(), e);
        }
    }

    private static String[] splitJsonPairs(String content) {
        // Tách các cặp key-value, xử lý dấu phẩy ngoài chuỗi
        StringBuilder pair = new StringBuilder();
        boolean inQuotes = false;
        int braceCount = 0;
        java.util.List<String> pairs = new java.util.ArrayList<>();
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == '{' && !inQuotes) {
                braceCount++;
            } else if (c == '}' && !inQuotes) {
                braceCount--;
            } else if (c == ',' && !inQuotes && braceCount == 0) {
                pairs.add(pair.toString().trim());
                pair = new StringBuilder();
                continue;
            }
            pair.append(c);
        }
        if (pair.length() > 0) {
            pairs.add(pair.toString().trim());
        }
        return pairs.toArray(new String[0]);
    }

    private static String[] splitKeyValue(String pair) {
        // Tách key và value, xử lý dấu hai chấm ngoài chuỗi
        StringBuilder key = new StringBuilder();
        boolean inQuotes = false;
        int i = 0;
        while (i < pair.length()) {
            char c = pair.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ':' && !inQuotes) {
                return new String[]{key.toString().trim(), pair.substring(i + 1).trim()};
            }
            key.append(c);
            i++;
        }
        return new String[]{};
    }

    private static String unescapeJsonString(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean escaped = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (escaped) {
                switch (c) {
                    case '"': result.append('"'); break;
                    case '\\': result.append('\\'); break;
                    case 'n': result.append('\n'); break;
                    case 'r': result.append('\r'); break;
                    case 't': result.append('\t'); break;
                    default: result.append(c);
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String buildColumnsJsonFromList(List<Map<String, String>> columns) {
        try {
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < columns.size(); i++) {
                Map<String, String> column = columns.get(i);
                json.append("{");
                json.append("\"name\":\"").append(escapeJsonString(column.get("name"))).append("\",");
                json.append("\"comment\":\"").append(escapeJsonString(column.get("comment"))).append("\",");
                json.append("\"dataType\":\"").append(escapeJsonString(column.get("dataType"))).append("\"");
                json.append("}");
                if (i < columns.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");
            String result = json.toString();
            LogHandler.logInfo("Built columns JSON: " + result);
            return result;
        } catch (Exception e) {
            LogHandler.logError("Failed to build columns JSON: " + e.getMessage());
            return "[]";
        }
    }

    public static String buildDataJson(ResultSet rs) throws Exception {
        try {
            StringBuilder json = new StringBuilder("[");
            boolean firstRow = true;
            while (rs.next()) {
                if (!firstRow) {
                    json.append(",");
                }
                json.append("{");
                boolean firstColumn = true;
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (!firstColumn) {
                        json.append(",");
                    }
                    String columnName = rs.getMetaData().getColumnName(i);
                    String value = rs.getString(i) != null ? rs.getString(i) : "";
                    json.append("\"").append(escapeJsonString(columnName)).append("\":\"").append(escapeJsonString(value)).append("\"");
                    firstColumn = false;
                }
                json.append("}");
                firstRow = false;
            }
            json.append("]");
            String result = json.toString();
            LogHandler.logInfo("Built data JSON: " + result);
            return result;
        } catch (Exception e) {
            LogHandler.logError("Failed to build data JSON: " + e.getMessage());
            throw e;
        }
    }

    public static String buildPrimaryKeysJson(List<String> primaryKeys) {
        try {
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < primaryKeys.size(); i++) {
                json.append("\"").append(escapeJsonString(primaryKeys.get(i))).append("\"");
                if (i < primaryKeys.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");
            String result = json.toString();
            LogHandler.logInfo("Built primary keys JSON: " + result);
            return result;
        } catch (Exception e) {
            LogHandler.logError("Failed to build primary keys JSON: " + e.getMessage());
            return "[]";
        }
    }

    private static String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}