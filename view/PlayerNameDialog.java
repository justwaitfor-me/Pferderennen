package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class PlayerNameDialog extends JDialog {

    private String enteredName = null;
    private final JTextField nameField;

    public PlayerNameDialog(JFrame parent) {
        super(parent, "Spieler registrieren", true);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(MainFrame.BG_DARK);
        setResizable(false);

        // title banner
        JLabel title = new JLabel("🏇  ROYAL RACE", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(MainFrame.ACCENT);
        title.setBorder(new EmptyBorder(24, 24, 8, 24));
        add(title, BorderLayout.NORTH);

        // center
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(MainFrame.BG_DARK);
        center.setBorder(new EmptyBorder(8, 32, 16, 32));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0; g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0; g.insets = new Insets(6, 0, 6, 0);

        JLabel sub = new JLabel("Starte mit 1000€ Startguthaben.", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(MainFrame.TEXT_MUTED);
        center.add(sub, g); g.gridy++;

        JLabel lbl = new JLabel("Dein Spielername:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(MainFrame.TEXT_MAIN);
        center.add(lbl, g); g.gridy++;

        nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setBackground(new Color(28, 32, 46));
        nameField.setForeground(MainFrame.TEXT_MAIN);
        nameField.setCaretColor(MainFrame.ACCENT);
        nameField.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 65, 90)),
                new EmptyBorder(6, 10, 6, 10)));
        nameField.setPreferredSize(new Dimension(0, 38));
        nameField.addActionListener(e -> confirm());
        center.add(nameField, g);
        add(center, BorderLayout.CENTER);

        // button
        JButton ok = new JButton("Spiel starten  →");
        ok.setFont(new Font("Segoe UI", Font.BOLD, 13));
        ok.setBackground(MainFrame.ACCENT);
        ok.setForeground(Color.BLACK);
        ok.setFocusPainted(false); ok.setBorderPainted(false); ok.setOpaque(true);
        ok.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ok.setPreferredSize(new Dimension(0, 42));
        ok.addActionListener(e -> confirm());
        JPanel btnWrap = new JPanel(new BorderLayout());
        btnWrap.setBackground(MainFrame.BG_DARK);
        btnWrap.setBorder(new EmptyBorder(0, 32, 24, 32));
        btnWrap.add(ok);
        add(btnWrap, BorderLayout.SOUTH);

        pack();
        setSize(380, 300);
        setLocationRelativeTo(parent);
    }

    private void confirm() {
        String txt = nameField.getText().strip();
        if (txt.isBlank()) {
            nameField.setBorder(new CompoundBorder(
                    new LineBorder(MainFrame.ERROR_RED),
                    new EmptyBorder(6, 10, 6, 10)));
            return;
        }
        enteredName = txt;
        dispose();
    }

    public String getEnteredName() { return enteredName; }
}
