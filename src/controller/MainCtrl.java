package controller;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import model.ApiClient;
import model.TableDataOperationsClient;
import view.MainRegion.ContentPanel;
import view.MainRegion.TablePanel;
import view.MainUI;
import view.MenuRegion.MenuPanel;

public class MainCtrl {
    private final ContentPanel contentPanel;
    private final MenuPanel menuPanel;
    private static TableDataOperationsClient operationsClient;
    private CheckDetail checkDetail;
    private CheckBooking checkBooking;

    public MainCtrl(ContentPanel contentPanel, MenuPanel menuPanel, MainUI mainUI) {
        if (contentPanel == null || menuPanel == null) {
            throw new IllegalArgumentException("ContentPanel and MenuPanel must not be null");
        }
        this.contentPanel = contentPanel;
        this.menuPanel = menuPanel;

        menuPanel.setTableSelectionListener((tableName, tableComment) -> {
            if (tableName != null && !tableName.isEmpty()) {
                ApiClient.TableDataResult result = ApiClient.getTableData(tableName);
                contentPanel.updateTableData(result.data, result.columnComments, result.columnTypes, 
                                            result.keyColumn, result.primaryKeyColumns, tableName, tableComment);
            } else {
                LogHandler.logError("Invalid table name received");
            }
        });
        //Xử lý kiểm tra chi tiết phòng
        TablePanel tablePanel = new TablePanel(contentPanel);
        CheckDetail checker = new CheckDetail("Bearer your-auth-token", this.contentPanel, tablePanel);
        checker.autoProcessCheckDetail("d4_kiemtrachitiet", "MaThietBi", "");
        //Xử lý kiểm tra đặt phòng
        CheckBooking checkBooking = new CheckBooking("Bearer your-auth-token", this.contentPanel, tablePanel);
        checkBooking.autoProcessBooking("", "", "");
        CheckService checkService = new CheckService(this.contentPanel, tablePanel);
        checkService.autoProcessCheckService("", "");
        CheckBill checkBill = new CheckBill("Bearer your-auth-token", this.contentPanel, tablePanel);
        checkBill.autoProcessCheckBill("b2_hoadonchitiet", "","");
        // Khởi động server
        startServer(mainUI);
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public static void setAuthHeader(String authHeader) {
        operationsClient = new TableDataOperationsClient(authHeader);
        LogHandler.logInfo("TableDataOperationsClient được khởi tạo với authHeader");
    }

    public static ApiClient.ApiResponse addRow(String tableName, Map<String, Object> data) {
        if (operationsClient == null) {
            LogHandler.logError("Không thể thực hiện addRow: operationsClient là null. Người dùng có thể chưa đăng nhập.");
            return new ApiClient.ApiResponse(false, "Vui lòng đăng nhập trước khi thực hiện thao tác này");
        }
        return operationsClient.addRow(tableName, data);
    }

    public static ApiClient.ApiResponse updateRow(String tableName, String keyColumn, Object keyValue, Map<String, Object> data) {
        if (operationsClient == null) {
            LogHandler.logError("Không thể thực hiện updateRow: operationsClient là null. Người dùng có thể chưa đăng nhập.");
            return new ApiClient.ApiResponse(false, "Vui lòng đăng nhập trước khi thực hiện thao tác này");
        }
        return operationsClient.updateRow(tableName, keyColumn, keyValue, data);
    }

    public static ApiClient.ApiResponse updateRow(String tableName, java.util.List<String> primaryKeyColumns, Map<String, String> keyValues, Map<String, Object> data) {
        if (operationsClient == null) {
            LogHandler.logError("Không thể thực hiện updateRow: operationsClient là null. Người dùng có thể chưa đăng nhập.");
            return new ApiClient.ApiResponse(false, "Vui lòng đăng nhập trước khi thực hiện thao tác này");
        }
        if (primaryKeyColumns == null || primaryKeyColumns.isEmpty() || keyValues == null || keyValues.isEmpty()) {
            LogHandler.logError("primaryKeyColumns hoặc keyValues không hợp lệ.");
            return new ApiClient.ApiResponse(false, "Dữ liệu khóa chính không hợp lệ");
        }
        return operationsClient.updateRow(tableName, primaryKeyColumns, keyValues, data);
    }

    public static ApiClient.ApiResponse deleteRow(String tableName, String keyColumn, Object keyValue) {
        if (operationsClient == null) {
            LogHandler.logError("Không thể thực hiện deleteRow: operationsClient là null. Người dùng có thể chưa đăng nhập.");
            return new ApiClient.ApiResponse(false, "Vui lòng đăng nhập trước khi thực hiện thao tác này");
        }
        return operationsClient.deleteRow(tableName, keyColumn, keyValue);
    }

    public static ApiClient.ApiResponse deleteRow(String tableName, java.util.List<String> primaryKeyColumns, Map<String, String> keyValues) {
        if (operationsClient == null) {
            LogHandler.logError("Không thể thực hiện deleteRow: operationsClient là null. Người dùng có thể chưa đăng nhập.");
            return new ApiClient.ApiResponse(false, "Vui lòng đăng nhập trước khi thực hiện thao tác này");
        }
        if (primaryKeyColumns == null || primaryKeyColumns.isEmpty() || keyValues == null || keyValues.isEmpty()) {
            LogHandler.logError("primaryKeyColumns hoặc keyValues không hợp lệ.");
            return new ApiClient.ApiResponse(false, "Dữ liệu khóa chính không hợp lệ");
        }
        return operationsClient.deleteRow(tableName, primaryKeyColumns, keyValues);
    }

    public static void startServer(MainUI mainUI) {
        new Thread(() -> {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
                server.createContext("/api/login", new LoginHandler(mainUI));
                server.createContext("/api/tables", new TablesHandler());
                server.createContext("/api/table/", new TableDataHandler());
                server.setExecutor(null);
                server.start();
                LogHandler.logInfo("Server started on port 8080");
            } catch (IOException e) {
                LogHandler.logError("Lỗi server: " + e.getMessage(), e);
            }
        }).start();
    }
}