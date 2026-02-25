package controller;

import java.util.HashMap;
import java.util.Map;

public class UserSession {
    private static String currentUsername;
    private static String currentGroup;
    private static Map<String, String> tablePermissions = new HashMap<>(); // tableName -> permission

    public static void setUser(String username, String group, Map<String, String> permissions) {
        currentUsername = username;
        currentGroup = group;
        tablePermissions = permissions;
        LogHandler.logInfo("User session initialized for: " + username + ", Group: " + group);
    }

    public static void clearSession() {
        currentUsername = null;
        currentGroup = null;
        tablePermissions.clear();
        LogHandler.logInfo("User session cleared");
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static String getCurrentGroup() {
        return currentGroup;
    }

    public static Map<String, String> getTablePermissions() {
        return tablePermissions;
    }

    public static boolean hasPermission(String tableName, String requiredPermission) {
        String permission = tablePermissions.getOrDefault(tableName, "00");
        int permLevel = Integer.parseInt(permission);
        int reqLevel = Integer.parseInt(requiredPermission);
        return permLevel >= reqLevel;
    }

    public static void logOut() {
        System.out.println("Logged out");
    }
}