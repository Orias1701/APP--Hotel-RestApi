package view;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.geom.Ellipse2D;

public class Style {
    // Color
    public static final Color LIGHT_CL = Color.WHITE;
    public static final Color DARK_CL = Color.BLACK;
    public static final Color GREEN = Color.decode("#00A251");
    public static final Color BLUE = Color.decode("#7F55FF");
    public static final Color RED = Color.decode("#C90000");
    public static final Color GRAY_CL = Color.decode("#444444");
    public static final Color MAIN_CL = Color.decode("#9E88FF");
    public static final Color SEC_CL = Color.decode("#D7CEFF");
    public static final Color TRD_CL = Color.decode("#7C68D4");
    public static final Color FTH_CL = Color.decode("#F3E8FF");
    public static final Color ACT_CL = Color.decode("#6430AD");
    public static final Color BUTTON_CL = Color.decode("#43327C");
    public static final Color NO_CL = new Color(0, 0, 0, 0);

    // Font
    public static final Font MONS_12 = new Font("Montserrat", Font.BOLD, 12);
    public static final Font MONS_14 = new Font("Montserrat", Font.BOLD, 14);
    public static final Font MONS_15 = new Font("Montserrat", Font.BOLD, 15);    
    public static final Font MONS_16 = new Font("Montserrat", Font.BOLD, 16);
    public static final Font MONS_20 = new Font("Montserrat", Font.BOLD, 20);
    public static final Font MONS_24 = new Font("Montserrat", Font.BOLD, 24);
    public static final Font MONS_28 = new Font("Montserrat", Font.BOLD, 28);
    public static final Font MONS_32 = new Font("Montserrat", Font.BOLD, 32);
    public static final Font MONS_L18 = new Font("Montserrat", Font.PLAIN, 18);
    public static final Font ROB_12 = new Font("Roboto", Font.PLAIN, 12);
    public static final Font ROB_B12 = new Font("Roboto", Font.BOLD, 12);
    public static final Font ROB_B14 = new Font("Roboto", Font.BOLD, 14);
    public static final Font ROB_14 = new Font("Roboto", Font.PLAIN, 14);
    public static final Font ROB_L16 = new Font("Roboto", Font.PLAIN, 16);
    public static final Font ROB_16 = new Font("Roboto", Font.BOLD, 16);
    public static final Font ROB_20 = new Font("Roboto", Font.BOLD, 20);
    public static final Font ROB_24 = new Font("Roboto", Font.BOLD, 24);
    public static final Font ROB_28 = new Font("Roboto", Font.BOLD, 28);
    public static final Font NOR_12 = new Font("Arial", Font.BOLD, 12);
    public static final Font NOR_14 = new Font("Arial", Font.BOLD, 14);
    public static final Font NOR_16 = new Font("Arial", Font.BOLD, 16);
    public static final Font NOR_L16 = new Font("Arial", Font.PLAIN, 18);
    public static final Font NOR_20 = new Font("Arial", Font.BOLD, 20);
    public static final Font NOR_24 = new Font("Arial", Font.BOLD, 24);
    public static final Font NOR_28 = new Font("Arial", Font.BOLD, 28);
    public static final Font SC_BI = new Font("Overlock SC", Font.BOLD | Font.ITALIC, 24);
    public static final Font SC_B = new Font("Overlock SC", Font.BOLD, 24);
    public static final Font SC_S = new Font("Overlock SC", Font.BOLD, 18);

    // Size

    public static final Dimension BUTTON_SIZE = new Dimension(240, 50);

    // Padding & spacing
    public static final int WIN_WIDTH = 1440;
    public static final int WIN_HEIGHT = 900;
    public static final int MENU_WIDTH = 240;
    public static final int HEAD_HEIGHT = 70;
    public static final int FOOT_HEIGHT = 40;
    public static final int MENU_TOP_PADDING = 20;
    public static final int MENU_BOTTOM_PADDING = 10;
    public static final int BUTTON_SPACING = 7;

    // Border
    public static final Border BORDER_L20 = BorderFactory.createEmptyBorder(0, 20, 0, 0);

    // Rounded Button
    public static class RoundedButton extends JButton {
        private final int arcWidth;
        private final int arcHeight;

        public RoundedButton(String text) {
            this(text, 20, 20);
        }

        public RoundedButton(String text, int arcWidth, int arcHeight) {
            super(text);
            this.arcWidth = arcWidth;
            this.arcHeight = arcHeight;
            setOpaque(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class RoundBorder implements Border {
        private final int radius;
        private final Color color;

        public RoundBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public static class CircleButton extends RoundedButton {
        public CircleButton(String text) {
            super(text, 150, 150);
            setPreferredSize(new Dimension(150, 150));
            setMinimumSize(new Dimension(150, 150));
            setMaximumSize(new Dimension(150, 150));
            setFont(Style.MONS_12);
            setBackground(Style.MAIN_CL);
            setForeground(Color.WHITE);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setVerticalTextPosition(SwingConstants.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillOval(0, 0, getWidth(), getHeight());

            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public boolean contains(int x, int y) {
            Ellipse2D circle = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
            return circle.contains(x, y);
        }
    }

    public static class CustomScrollBarUI extends BasicScrollBarUI {
        private final Color THUMB_COLOR = Style.ACT_CL;
        private final Color TRACK_COLOR = Style.LIGHT_CL;

        @Override
        protected void configureScrollBarColors() {
            thumbColor = THUMB_COLOR;
            trackColor = TRACK_COLOR;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createEmptyButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createEmptyButton();
        }

        private JButton createEmptyButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(TRACK_COLOR);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(THUMB_COLOR);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width - 2, thumbBounds.height - 2, 10, 10);
            g2.dispose();
        }
    }
}   
