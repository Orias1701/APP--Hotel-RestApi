package view.MainRegion;

import controller.LogHandler;
import java.util.*;
import javax.swing.*;
import model.ApiClient;
import model.ApiClient.TableDataResult;
import view.Style;

public class StatusComboBox {

    // Tạo JComboBox chứa các giá trị động từ cơ sở dữ liệu
    public JComboBox<String> createStatusComboBox() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        Set<String> uniqueStatuses = new HashSet<>();

        // Lấy dữ liệu từ bảng và cột cụ thể (ví dụ: bảng "statuses", cột "status")
        try {
            TableDataResult tableData = ApiClient.getTableData("statuses");
            if (tableData != null && tableData.data != null && !tableData.data.isEmpty()) {
                for (Map<String, String> row : tableData.data) {
                    String value = row.get("status"); // Cột chứa giá trị
                    if (value != null && !value.trim().isEmpty()) {
                        uniqueStatuses.add(value.toUpperCase()); // Chuẩn hóa giá trị
                    }
                }
                if (!uniqueStatuses.isEmpty()) {
                    model.addAll(uniqueStatuses);
                } else {
                    LogHandler.logWarn("Không tìm thấy giá trị hợp lệ trong bảng 'statuses', cột 'status'.");
                }
            } else {
                LogHandler.logWarn("Không thể lấy dữ liệu từ bảng 'statuses' hoặc bảng rỗng.");
            }
        } catch (Exception e) {
            LogHandler.logError("Lỗi khi lấy dữ liệu từ bảng 'statuses': " + e.getMessage(), e);
        }

        JComboBox<String> comboBox = new JComboBox<>(model);
        comboBox.setFont(Style.MONS_14); // Đồng bộ với FormDialogPanel
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new Style.RoundBorder(Style.MAIN_CL, 10), // Sử dụng RoundBorder để đồng bộ
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return comboBox;
    }
}