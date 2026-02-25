package view.MainRegion;

import java.util.List;
import java.util.Map;

public interface TableViewDataHandler {
    void updateTableData(List<Map<String, String>> data, Map<String, String> columnComments, Map<String, String> columnTypes, String keyColumn, String tableName, String tableComment);
    void refreshTable();
    String getTableName();
    String getKeyColumn();
    List<String> getColumnNames();
    List<String> getColumnComments();
    List<String> getColumnTypes();
}