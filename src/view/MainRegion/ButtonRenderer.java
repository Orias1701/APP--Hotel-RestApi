package view.MainRegion;

import javax.swing.*;
import java.awt.*;
import view.Style;

public class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
        setFont(Style.ROB_B14);
        setBackground(Style.NO_CL);
        setForeground(Style.ACT_CL);
        setMargin(new Insets(0, 0, 0, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Style.ACT_CL));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}