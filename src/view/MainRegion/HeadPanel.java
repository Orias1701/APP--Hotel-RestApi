package view.MainRegion;

import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;

import controller.UserSession;
import view.Style;

public class HeadPanel extends JPanel {
    private final JLabel tableNameLabel = new JLabel("");
    private final Style.RoundedButton changeLayoutButton;
    private Consumer<Boolean> changeLayoutCallback;
    private boolean isButtonView = false;

    public HeadPanel(Consumer<Void> addButtonCallback, String tableName) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 10));
        setOpaque(false);

        // Label tên bảng
        tableNameLabel.setFont(Style.MONS_28);
        tableNameLabel.setForeground(Style.MAIN_CL);
        tableNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        add(tableNameLabel, BorderLayout.WEST);

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        // Kiểm tra quyền thêm bản ghi
        boolean canAdd = UserSession.hasPermission(tableName, "10");

        // Nút thêm bản ghi
        Style.RoundedButton addRecordButton = new Style.RoundedButton(" + ");
        addRecordButton.setFont(Style.MONS_24);
        addRecordButton.setForeground(Style.LIGHT_CL);
        addRecordButton.setBackground(Style.GREEN);
        addRecordButton.setFocusPainted(false);
        addRecordButton.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        addRecordButton.setEnabled(canAdd);
        if (canAdd) {
            addRecordButton.addActionListener(e -> addButtonCallback.accept(null));
        }
        buttonPanel.add(addRecordButton);

        // Nút ChangeLayout
        changeLayoutButton = new Style.RoundedButton("</>");
        changeLayoutButton.setFont(Style.MONS_20);
        changeLayoutButton.setForeground(Style.LIGHT_CL);
        changeLayoutButton.setBackground(Style.TRD_CL);
        changeLayoutButton.setFocusPainted(false);
        changeLayoutButton.setBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9));
        changeLayoutButton.addActionListener(e -> {
            isButtonView = !isButtonView;
            if (changeLayoutCallback != null) {
                changeLayoutCallback.accept(isButtonView);
            }
        });
        buttonPanel.add(changeLayoutButton);

        add(buttonPanel, BorderLayout.EAST);
    }

    public void setChangeLayoutCallback(Consumer<Boolean> callback) {
        this.changeLayoutCallback = callback;
    }

    public void updateTableNameLabel(String text) {
        tableNameLabel.setText(text != null ? text : "");
    }

    public boolean isButtonViewActive() {
        return isButtonView;
    }
}