package view;

import model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class ResultDialog extends JDialog {

    public ResultDialog(JFrame parent, String summary, Race race, Player player) {
        super(parent, "🏆 Rennergebnis", true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(MainFrame.BG_DARK);

        add(buildBanner(race), BorderLayout.NORTH);
        add(buildCenter(summary, race, player), BorderLayout.CENTER);
        add(buildClose(), BorderLayout.SOUTH);

        setSize(660, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private JPanel buildBanner(Race race) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(10, 12, 20));
        p.setBorder(new EmptyBorder(16, 20, 16, 20));
        Horse w = race.getWinner();
        JLabel title = new JLabel(
                w != null ? "🥇  " + w.getName().toUpperCase() + "  GEWINNT!" : "Rennen beendet",
                SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(MainFrame.ACCENT);
        p.add(title, BorderLayout.CENTER);
        if (w != null) {
            JLabel sub = new JLabel("Jockey: " + w.getJockey() +
                    "  |  Quote: " + String.format("%.1f", w.getOdds()) + ":1",
                    SwingConstants.CENTER);
            sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            sub.setForeground(MainFrame.TEXT_MUTED);
            p.add(sub, BorderLayout.SOUTH);
        }
        return p;
    }

    private JPanel buildCenter(String summary, Race race, Player player) {
        JPanel p = new JPanel(new GridLayout(1, 2, 10, 0));
        p.setBackground(MainFrame.BG_DARK);
        p.setBorder(new EmptyBorder(12, 16, 12, 16));
        p.add(scrollTA(summary));
        StringBuilder sb = new StringBuilder("=== Ihre Wetten ===\n\n");
        List<Bet> bets = race.getBets();
        if (bets.isEmpty()) {
            sb.append("Keine Wetten platziert.");
        } else {
            bets.forEach(b -> sb.append(b.toDisplayString()).append("\n"));
            if (player != null) {
                sb.append("\n").append("─".repeat(44)).append("\n");
                sb.append(player.getStatsString());
            }
        }
        p.add(scrollTA(sb.toString()));
        return p;
    }

    private JComponent buildClose() {
        JButton b = new JButton("Fenster schließen");
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(MainFrame.ACCENT); b.setForeground(Color.BLACK);
        b.setFocusPainted(false); b.setBorderPainted(false); b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 40));
        b.addActionListener(e -> dispose());
        JPanel w = new JPanel(new BorderLayout());
        w.setBackground(MainFrame.BG_DARK);
        w.setBorder(new EmptyBorder(0, 16, 14, 16));
        w.add(b); return w;
    }

    private JScrollPane scrollTA(String text) {
        JTextArea ta = new JTextArea(text);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 11));
        ta.setEditable(false);
        ta.setBackground(new Color(12, 14, 22));
        ta.setForeground(MainFrame.TEXT_MAIN);
        ta.setBorder(new EmptyBorder(8, 8, 8, 8));
        ta.setLineWrap(true); ta.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(ta);
        sp.setBorder(new LineBorder(new Color(45, 50, 65)));
        return sp;
    }
}
