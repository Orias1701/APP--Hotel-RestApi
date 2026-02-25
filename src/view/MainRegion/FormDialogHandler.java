package view.MainRegion;

import javax.swing.JTable;

public interface FormDialogHandler {
    void showFormDialog(String action, int rowIndex);

    void handleAction(String action, JTable table, int selectedRow);
}
