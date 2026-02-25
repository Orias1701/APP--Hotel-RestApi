package view.HeaderRegion;

import controller.LogHandler;
import controller.UserSession;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import model.ApiClient;
import view.MainUI;
import view.Style;

public class HeaderPanel extends javax.swing.JPanel {
    private JLabel userLabel;

    public HeaderPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(Style.WIN_WIDTH, Style.HEAD_HEIGHT));
        setBorder(BorderFactory.createMatteBorder(0, 0, 7, 0, Style.ACT_CL));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        // Logo bên trái
        JLabel branchLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/view/HeaderRegion/Branch.png"));
            branchLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(100, 50, java.awt.Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            branchLabel.setText("Branch.png not found");
            System.err.println("Error loading Branch.png: " + e.getMessage());
        }
        branchLabel.setPreferredSize(new Dimension(100, 50));
        branchLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new java.awt.Insets(10, 20, 10, 0);
        add(branchLabel, gbc);

        // Label hiển thị TenNhanVien
        String username = UserSession.getCurrentUsername();
        String displayName = "Guest";
        if (username != null) {
            try {
                Map<String, String> employeeInfo = ApiClient.getEmployeeInfo(username);
                displayName = employeeInfo.getOrDefault("TenNhanVien", username);
            } catch (SQLException e) {
                LogHandler.logError("Lỗi khi lấy tên nhân viên: " + e.getMessage(), e);
                displayName = username;
            }
        }
        userLabel = new JLabel(displayName);
        userLabel.setFont(Style.SC_S);
        userLabel.setForeground(Style.ACT_CL);
        userLabel.setPreferredSize(new Dimension(150, 70));
        userLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        userLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        userLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                String currentUsername = UserSession.getCurrentUsername();
                if (currentUsername != null) {
                    try {
                        Map<String, String> employeeInfo = ApiClient.getEmployeeInfo(currentUsername);
                        if (!employeeInfo.isEmpty()) {
                            // Tạo JDialog hiện đại
                            JDialog infoDialog = new JDialog((java.awt.Frame) null, "Thông Tin Nhân Viên", true);
                            infoDialog.setLayout(new BorderLayout());
                            infoDialog.getContentPane().setBackground(Style.LIGHT_CL);
                            infoDialog.setResizable(false);

                            // Panel chính với layout đẹp
                            JPanel contentPanel = new JPanel(new GridBagLayout());
                            contentPanel.setBackground(Style.LIGHT_CL);
                            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

                            GridBagConstraints gbcDialog = new GridBagConstraints();
                            gbcDialog.insets = new Insets(10, 10, 10, 10);
                            gbcDialog.anchor = GridBagConstraints.WEST;
                            gbcDialog.fill = GridBagConstraints.HORIZONTAL;
                            gbcDialog.gridx = 0;
                            gbcDialog.gridy = 0;

                            JLabel maNV = new JLabel("Mã Nhân Viên: " + employeeInfo.getOrDefault("MaNhanVien", "N/A"));
                            maNV.setFont(Style.MONS_16);
                            maNV.setForeground(Style.DARK_CL);
                            contentPanel.add(maNV, gbcDialog);

                            gbcDialog.gridy++;
                            JLabel tenNV = new JLabel("Tên Nhân Viên: " + employeeInfo.getOrDefault("TenNhanVien", "N/A"));
                            tenNV.setFont(Style.MONS_16);
                            tenNV.setForeground(Style.DARK_CL);
                            contentPanel.add(tenNV, gbcDialog);

                            gbcDialog.gridy++;
                            JLabel group = new JLabel("Nhóm: " + employeeInfo.getOrDefault("Group", "N/A"));
                            group.setFont(Style.MONS_16);
                            group.setForeground(Style.DARK_CL);
                            contentPanel.add(group, gbcDialog);

                            // Nút đóng
                            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                            buttonPanel.setBackground(Style.LIGHT_CL);
                            Style.RoundedButton closeButton = new Style.RoundedButton("Đóng");
                            closeButton.setFont(Style.MONS_14);
                            closeButton.setBackground(Style.MAIN_CL);
                            closeButton.setForeground(Color.WHITE);
                            closeButton.setFocusPainted(false);
                            closeButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
                            closeButton.addActionListener(evt -> infoDialog.dispose());
                            buttonPanel.add(closeButton);

                            // Gộp vào dialog
                            infoDialog.add(contentPanel, BorderLayout.CENTER);
                            infoDialog.add(buttonPanel, BorderLayout.SOUTH);
                            infoDialog.pack();
                            infoDialog.setLocationRelativeTo(null);
                            infoDialog.setVisible(true);

                        } else {
                            JOptionPane.showMessageDialog(
                                HeaderPanel.this,
                                "Không tìm thấy thông tin nhân viên.",
                                "Thông Báo",
                                JOptionPane.WARNING_MESSAGE
                            );
                        }
                    } catch (SQLException ex) {
                        LogHandler.logError("Lỗi khi lấy thông tin nhân viên: " + ex.getMessage(), ex);
                        JOptionPane.showMessageDialog(
                            HeaderPanel.this,
                            "Lỗi khi lấy thông tin: " + ex.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        HeaderPanel.this,
                        "Vui lòng đăng nhập để xem thông tin.",
                        "Thông Báo",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new java.awt.Insets(0, 0, 0, 5);
        add(userLabel, gbc);

        // Hình tròn 50x50
        JPanel circlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Style.ACT_CL);
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawOval(5, 5, 40, 40);
            }
        };
        circlePanel.setPreferredSize(new Dimension(50, 50));
        circlePanel.setMinimumSize(new Dimension(50, 50));
        circlePanel.setMaximumSize(new Dimension(50, 50));
        circlePanel.setOpaque(false);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new java.awt.Insets(10, 5, 10, 5);
        add(circlePanel, gbc);

        // Nút Refresh
        JButton refreshButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Style.ACT_CL);
                // Vẽ biểu tượng refresh (mũi tên vòng tròn đơn giản)
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawArc(10, 10, 30, 30, 45, 270); // Vòng cung 3/4
                int[] xArrow = {35, 30, 40};
                int[] yArrow = {15, 20, 20};
                g2d.fillPolygon(xArrow, yArrow, 3); // Mũi tên
            }
        };
        refreshButton.setPreferredSize(new Dimension(45, 50));
        refreshButton.setMinimumSize(new Dimension(45, 50));
        refreshButton.setMaximumSize(new Dimension(45, 50));
        refreshButton.setBackground(Color.WHITE);
        refreshButton.setBorder(BorderFactory.createEmptyBorder());
        refreshButton.setFocusPainted(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setToolTipText("Làm mới ứng dụng");
        refreshButton.addActionListener(e -> {
            try {
                java.awt.Component topLevelAncestor = getTopLevelAncestor();
                if (topLevelAncestor instanceof MainUI) {
                    MainUI mainUI = (MainUI) topLevelAncestor;
                    mainUI.showMainInterface(); // Làm mới giao diện chính
                    // Đóng tất cả các cửa sổ con (nếu cần)
                    for (java.awt.Window window : java.awt.Window.getWindows()) {
                        if (window != mainUI && window.isVisible()) {
                            window.dispose();
                        }
                    }
                    JOptionPane.showMessageDialog(
                        this,
                        "Ứng dụng đã được làm mới.",
                        "Thông Báo",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    LogHandler.logError("Không thể làm mới: getTopLevelAncestor không phải MainUI");
                    JOptionPane.showMessageDialog(
                        this,
                        "Lỗi hệ thống: Không thể xác định cửa sổ chính để làm mới.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (Exception ex) {
                LogHandler.logError("Lỗi khi làm mới ứng dụng: " + ex.getMessage(), ex);
                JOptionPane.showMessageDialog(
                    this,
                    "Lỗi khi làm mới: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new java.awt.Insets(10, 5, 10, 5);
        add(refreshButton, gbc);

        // Nút đăng xuất với biểu tượng cánh cửa tự vẽ
        JButton logoutButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Style.ACT_CL);
                int[] xDoors = {30, 10, 10, 30, 25, 15, 15, 25};
                int[] yDoors = {10, 10, 40, 40, 35, 35, 15, 15};
                g2d.fillPolygon(xDoors, yDoors, 8);
                int[] xOuts = {33, 43, 33, 33, 25, 22, 25, 33};
                int[] yOuts = {31, 25, 19, 22, 22, 25, 28, 28};
                g2d.fillPolygon(xOuts, yOuts, 8);
            }
        };
        logoutButton.setPreferredSize(new Dimension(45, 50));
        logoutButton.setMinimumSize(new Dimension(45, 50));
        logoutButton.setMaximumSize(new Dimension(45, 50));
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setBorder(BorderFactory.createEmptyBorder());
        logoutButton.setFocusPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setToolTipText("Đăng xuất");
        logoutButton.addActionListener(e -> {
            if (UserSession.getCurrentUsername() == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Bạn chưa đăng nhập.",
                    "Thông Báo",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Tạo dialog tùy chỉnh
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Xác Nhận Đăng Xuất");
            dialog.setSize(350, 200);
            dialog.setLayout(new BorderLayout());
            dialog.setLocationRelativeTo(null);
            dialog.getContentPane().setBackground(Style.LIGHT_CL);

            // Label xác nhận
            JLabel messageLabel = new JLabel("Bạn có chắc chắn muốn đăng xuất?", SwingConstants.CENTER);
            messageLabel.setForeground(Style.DARK_CL);
            messageLabel.setFont(Style.MONS_14);
            messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
            dialog.add(messageLabel, BorderLayout.CENTER);

            // Tạo panel chứa nút
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.setBackground(Style.LIGHT_CL);

            Style.RoundedButton logoutBtn = new Style.RoundedButton("Đăng Xuất");
            logoutBtn.setFont(Style.MONS_14);
            logoutBtn.setBackground(Style.RED);
            logoutBtn.setForeground(Color.WHITE);
            logoutBtn.setPreferredSize(new Dimension(120, 40));

            Style.RoundedButton cancelBtn = new Style.RoundedButton("Hủy");
            cancelBtn.setFont(Style.MONS_14);
            cancelBtn.setBackground(Style.GRAY_CL);
            cancelBtn.setForeground(Style.LIGHT_CL);
            cancelBtn.setPreferredSize(new Dimension(120, 40));

            // Action cho nút
            logoutBtn.addActionListener(ev -> {
                dialog.dispose();
                try {
                    java.awt.Component topLevelAncestor = getTopLevelAncestor();
                    if (topLevelAncestor == null) {
                        LogHandler.logError("Không thể đăng xuất: getTopLevelAncestor trả về null");
                        JOptionPane.showMessageDialog(
                            this,
                            "Lỗi hệ thống: Không thể xác định cửa sổ chính để đăng xuất.",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    MainUI mainUI = (MainUI) topLevelAncestor;

                    UserSession.logOut();
                    UserSession.clearSession();
                    ApiClient.clearAuthHeader();

                    mainUI.showMainInterface();
                    updateUserLabel();
                } catch (Exception ex) {
                    LogHandler.logError("Lỗi khi đăng xuất: " + ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(
                        this,
                        "Lỗi khi đăng xuất: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            cancelBtn.addActionListener(ev -> dialog.dispose());

            buttonPanel.add(logoutBtn);
            buttonPanel.add(cancelBtn);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);
        });

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new java.awt.Insets(10, 5, 10, 15);
        add(logoutButton, gbc);
    }

    public void updateUserLabel() {
        String username = UserSession.getCurrentUsername();
        String displayName = "Guest";
        if (username != null) {
            try {
                Map<String, String> employeeInfo = ApiClient.getEmployeeInfo(username);
                displayName = employeeInfo.getOrDefault("TenNhanVien", username);
            } catch (SQLException e) {
                LogHandler.logError("Lỗi khi lấy tên nhân viên: " + e.getMessage(), e);
                displayName = username;
            }
        }
        userLabel.setText(displayName);
    }
}