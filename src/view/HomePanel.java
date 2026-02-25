package view;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    private Image backgroundImage;

    public HomePanel() {
        setLayout(null);
        loadBackgroundImage();
    }

    private void loadBackgroundImage() {
        try {
            java.net.URL imgURL = getClass().getResource("Manager.jpg");
            if (imgURL == null) {
                throw new IllegalArgumentException("Không tìm thấy file ảnh: images/Manager.jpg");
            }
            backgroundImage = new ImageIcon(imgURL).getImage();
        } catch (Exception e) {
            System.err.println("Lỗi tải ảnh: " + e.getMessage());
            backgroundImage = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Lấy kích thước panel
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            // Lấy kích thước ảnh gốc
            int imgWidth = backgroundImage.getWidth(this);
            int imgHeight = backgroundImage.getHeight(this);

            if (imgWidth <= 0 || imgHeight <= 0) {
                return; // Tránh lỗi nếu ảnh không hợp lệ
            }

            // Tính tỉ lệ khung hình
            double imgAspectRatio = (double) imgWidth / imgHeight;
            double panelAspectRatio = (double) panelWidth / panelHeight;

            int scaledWidth, scaledHeight;
            int x, y;

            // Điều chỉnh kích thước để lấp đầy panel theo chiều lớn nhất
            if (imgAspectRatio > panelAspectRatio) {
                // Ảnh rộng hơn panel, khớp chiều cao, cắt chiều rộng
                scaledHeight = panelHeight;
                scaledWidth = (int) (panelHeight * imgAspectRatio);
                x = (panelWidth - scaledWidth) / 2; // Căn giữa theo chiều ngang
                y = 0;
            } else {
                // Ảnh cao hơn panel, khớp chiều rộng, cắt chiều cao
                scaledWidth = panelWidth;
                scaledHeight = (int) (panelWidth / imgAspectRatio);
                x = 0;
                y = (panelHeight - scaledHeight) / 2; // Căn giữa theo chiều dọc
            }

            // Vẽ ảnh với kích thước đã tính
            g.drawImage(backgroundImage, x, y, scaledWidth, scaledHeight, this);
        }
    }
}