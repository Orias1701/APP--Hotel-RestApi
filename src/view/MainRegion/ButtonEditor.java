package view.MainRegion;

import java.awt.Component;
import javax.swing.*;

public class ButtonEditor extends DefaultCellEditor {
    private final JButton button;
    private final String actionType;
    private final FormDialogHandler formDialogHandler;
    private int row;

    public ButtonEditor(JCheckBox checkBox, String actionType, FormDialogHandler formDialogHandler) {
        super(checkBox);
        this.actionType = actionType;
        this.formDialogHandler = formDialogHandler;

        button = new JButton();
        button.addActionListener(e -> {
            fireEditingStopped();
            formDialogHandler.showFormDialog(actionType, row);
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        button.setText((value == null) ? "" : value.toString());
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }
}
