package view.MenuRegion;

import controller.LogHandler;
import controller.UserSession;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import model.ApiClient;
import view.Style;

public class MenuPanel extends JPanel {
    private final List<MenuButton> menuButtons = new ArrayList<>();
    private MenuButton activeButton = null;
    private String currentTableName;
    private Rectangle highlightRect;
    private final Timer animationTimer;
    private TableSelectionListener tableSelectionListener;
    private Runnable homeSelectionListener;

    private static final int MENU_WIDTH = Style.MENU_WIDTH;
    private static final int BUTTON_HEIGHT = 36;
    private static final int ACTIVE_BUTTON_HEIGHT = 50;
    private static final int HOME_BUTTON_HEIGHT = 50;
    private static final int MENU_START_Y = 80;

    public interface TableSelectionListener {
        void onTableSelected(String tableName, String tableComment);
    }

    public void setTableSelectionListener(TableSelectionListener listener) {
        this.tableSelectionListener = listener;
    }

    public void setHomeSelectionListener(Runnable listener) {
        this.homeSelectionListener = listener;
    }

    public String getCurrentTableName() {
        return currentTableName;
    }

    public MenuPanel() {
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(MENU_WIDTH, 660));
        highlightRect = new Rectangle(0, 0, MENU_WIDTH, ACTIVE_BUTTON_HEIGHT);
        animationTimer = new Timer(0, e -> animateHighlight());
        int y = 20;

        JScrollPane MenuScroll = new JScrollPane(this);
        MenuScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        MenuScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        MenuScroll.setBorder(null);
        MenuScroll.getViewport().setOpaque(false);
        MenuScroll.setOpaque(false);
        MenuScroll.getVerticalScrollBar().setUnitIncrement(20);
        MenuScroll.getHorizontalScrollBar().setEnabled(false);

        MenuButton homeButton = createMenuButton("TRANG CHỦ", y);
        homeButton.setFont(Style.MONS_15);
        homeButton.setForeground(Style.LIGHT_CL);
        homeButton.putClientProperty("tableName", "HOME");
        homeButton.setBounds(0, y, MENU_WIDTH, HOME_BUTTON_HEIGHT);
        homeButton.setBorder(Style.BORDER_L20);
        homeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        activeButton = homeButton;
        currentTableName = "HOME";
        highlightRect.setLocation(0, homeButton.getY());
        add(homeButton);
        menuButtons.add(homeButton);

        LogHandler.logInfo("MenuPanel initialized");
    }

    public void refreshTableList() {
        int y = MENU_START_Y;
        for (int i = menuButtons.size() - 1; i >= 0; i--) {
            MenuButton button = menuButtons.get(i);
            if (!"HOME".equals(button.getClientProperty("tableName"))) {
                remove(button);
                menuButtons.remove(i);
            }
        }

        Map<String, String> tableInfo = ApiClient.getTableInfo();
        if (tableInfo.containsKey("error")) {
            LogHandler.logError("Không thể tải danh sách bảng: " + tableInfo.get("error"));
            return;
        }

        Map<String, String> permissions = UserSession.getTablePermissions();
        for (Map.Entry<String, String> entry : tableInfo.entrySet()) {
            String tableName = entry.getKey();
            String tableComment = entry.getValue().toUpperCase();
            // Chỉ hiển thị bảng nếu người dùng có quyền xem
            if (permissions.getOrDefault(tableName, "00").compareTo("00") > 0) {
                MenuButton button = createMenuButton(tableComment, y);
                button.putClientProperty("tableName", tableName);
                add(button);
                menuButtons.add(button);
                y += BUTTON_HEIGHT;
            }
        }

        revalidate();
        repaint();
    }

    private MenuButton createMenuButton(String text, int y) {
        MenuButton button = new MenuButton(text);
        button.setBounds(0, y, MENU_WIDTH, BUTTON_HEIGHT);
        button.setFont(Style.MONS_15);
        button.setForeground(Style.GRAY_CL);
        button.setBackground(Style.NO_CL);
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.addActionListener(e -> moveHighlightTo(button));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!button.isActive()) {
                    button.setBackground(Style.SEC_CL);
                    button.setForeground(Style.LIGHT_CL);
                    button.repaint();
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!button.isActive()) {
                    button.setBackground(Style.NO_CL);
                    button.setForeground(Style.GRAY_CL);
                    button.repaint();
                }
            }
        });

        return button;
    }

    private void moveHighlightTo(MenuButton targetButton) {
        if (activeButton != null) {
            activeButton.setActive(false);
            activeButton.setBounds(activeButton.getX(), activeButton.getY(), MENU_WIDTH, BUTTON_HEIGHT);
        }

        activeButton = targetButton;
        activeButton.setActive(true);
        activeButton.setBounds(activeButton.getX(), activeButton.getY(), MENU_WIDTH, ACTIVE_BUTTON_HEIGHT);
        currentTableName = (String) activeButton.getClientProperty("tableName");
        String currentTableComment = activeButton.getText();
        LogHandler.logInfo("Tên bảng: " + currentTableName + ", Chú thích: " + currentTableComment);

        updateButtonPositions();

        if ("HOME".equals(currentTableName)) {
            if (homeSelectionListener != null) {
                homeSelectionListener.run();
            }
        } else if (tableSelectionListener != null) {
            tableSelectionListener.onTableSelected(currentTableName, currentTableComment);
        }

        animationTimer.start();
    }

    private void updateButtonPositions() {
        int y = MENU_START_Y;
        for (MenuButton button : menuButtons) {
            if (!"HOME".equals(button.getClientProperty("tableName"))) {
                int height = button.isActive() ? ACTIVE_BUTTON_HEIGHT : BUTTON_HEIGHT;
                button.setBounds(0, y, MENU_WIDTH, height);
                y += height;
            }
        }
        revalidate();
    }

    private void animateHighlight() {
        if (activeButton == null) return;
        int targetY = activeButton.getY();
        int targetHeight = activeButton.getHeight();
        int dy = targetY - highlightRect.y;
        int dh = targetHeight - highlightRect.height;

        if (Math.abs(dy) <= 1 && Math.abs(dh) <= 1) {
            highlightRect.y = targetY;
            highlightRect.height = targetHeight;
            animationTimer.stop();
        } else {
            int stepY = (int)(dy * 0.16);
            int stepH = (int)(dh * 0.16);
            if (stepY == 0) stepY = (dy > 0) ? 1 : -1;
            if (stepH == 0) stepH = (dh > 0) ? 1 : -1;
            highlightRect.y += stepY;
            highlightRect.height += stepH;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(0xFB, 0xFA, 0xFC),
            0, getHeight(), new Color(0x8E, 0x84, 0xB9)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Style.MAIN_CL);
        g2d.fillRoundRect(highlightRect.x, highlightRect.y, highlightRect.width, highlightRect.height, 7, 10);
        g2d.dispose();
    }
}