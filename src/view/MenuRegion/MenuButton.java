package view.MenuRegion;

import view.Style;

import java.awt.*;

public class MenuButton extends Style.RoundedButton {
    private boolean isActive = false;

    public MenuButton(String text) {
        super(text, 7, 10);
        setForeground(Color.BLACK);
        setFont(getFont().deriveFont(14f));
        setBackground(Style.NO_CL);
    }

    public void setActive(boolean active) {
        this.isActive = active;
        if (active) {
            setForeground(Style.LIGHT_CL);
            setBackground(Style.NO_CL);
        } else {
            setForeground(Style.GRAY_CL);
            setBackground(Style.NO_CL);
        }
        repaint();
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(240, isActive ? 60 : 40);
    }
}