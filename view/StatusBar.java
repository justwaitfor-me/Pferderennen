package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatusBar extends JPanel {

    private final JLabel msg;

    public StatusBar() {
        setLayout(new BorderLayout());
        setBackground(new Color(10, 12, 20));
        setBorder(new EmptyBorder(5, 14, 5, 14));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        msg = new JLabel("Spielernamen eingeben um zu starten…");
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        msg.setForeground(MainFrame.TEXT_MUTED);
        add(msg, BorderLayout.WEST);

        JLabel ver = new JLabel("v2.0  |  MIT License ");
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        ver.setForeground(new Color(50, 55, 75));
        add(ver, BorderLayout.EAST);
    }

    public void setMessage(String text, Color color) {
        msg.setText(text);
        msg.setForeground(color);
    }
}
