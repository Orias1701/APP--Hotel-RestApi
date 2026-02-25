package view.FooterRegion;

import view.Style;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FooterPanel extends JPanel {
    public FooterPanel() {
        setPreferredSize(new Dimension(Style.WIN_WIDTH, Style.FOOT_HEIGHT));
        setBackground(new Color(0x43327C));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel timeLabel = new JLabel();
        timeLabel.setFont(Style.NOR_14);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new java.awt.Insets(0, 0, 0, 20);
        add(timeLabel, gbc);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm : ss   dd - MM - yyyy");
        timeLabel.setText(LocalDateTime.now().format(formatter));

        Timer timer = new Timer(1000, e -> {
            timeLabel.setText(LocalDateTime.now().format(formatter));
        });
        timer.start();
    }
}