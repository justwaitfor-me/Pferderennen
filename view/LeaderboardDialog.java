package view;

import model.HighscoreEntry;
import util.HighscoreManager;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class LeaderboardDialog extends JDialog {

    private static final Color ROW_EVEN = new Color(18,  21,  34);
    private static final Color ROW_ODD  = new Color(24,  27,  42);
    private static final Color ROW_GOLD = new Color(50,  42,  12);
    private static final Color ROW_SILV = new Color(30,  35,  48);
    private static final Color ROW_BRON = new Color(38,  28,  18);

    public LeaderboardDialog(JFrame parent, HighscoreManager mgr) {
        super(parent, "🏆 Leaderboard – Highscores", true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(MainFrame.BG_DARK);

        add(buildBanner(), BorderLayout.NORTH);
        add(buildTable(mgr.getEntries()), BorderLayout.CENTER);
        add(buildFooter(mgr), BorderLayout.SOUTH);

        setSize(720, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private JPanel buildBanner() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(10, 12, 20));
        p.setBorder(new EmptyBorder(16, 20, 16, 20));
        JLabel t = new JLabel("🏆  LEADERBOARD  –  Top Spieler", SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));
        t.setForeground(MainFrame.ACCENT);
        p.add(t);
        return p;
    }

    private JScrollPane buildTable(List<HighscoreEntry> entries) {
        String[] cols = { "Rang", "Spieler", "Endguthaben", "Gewinn/Verlust", "Runden", "Datum" };
        Object[][] data = new Object[entries.size()][6];

        for (int i = 0; i < entries.size(); i++) {
            HighscoreEntry e = entries.get(i);
            String medal = switch (i) {
                case 0 -> "🥇 1";
                case 1 -> "🥈 2";
                case 2 -> "🥉 3";
                default -> String.valueOf(i + 1);
            };
            int net = e.getNetProfit();
            data[i][0] = medal;
            data[i][1] = e.getName();
            data[i][2] = e.getFinalBalance() + " €";
            data[i][3] = (net >= 0 ? "+" : "") + net + " €";
            data[i][4] = e.getRoundsPlayed();
            data[i][5] = e.getTimestamp();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setForeground(MainFrame.TEXT_MAIN);
        table.setBackground(ROW_EVEN);
        table.setGridColor(new Color(35, 40, 58));
        table.setRowHeight(28);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(55, 60, 90));
        table.setSelectionForeground(MainFrame.TEXT_MAIN);

        // header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(14, 16, 26));
        header.setForeground(MainFrame.ACCENT);
        header.setBorder(new MatteBorder(0, 0, 2, 0, MainFrame.ACCENT));

        // column widths
        int[] widths = { 65, 160, 110, 120, 70, 130 };
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // row renderer – highlight top 3 + alternating rows + color profit/loss
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setFont(new Font("Segoe UI", Font.PLAIN, 12));
                if (!sel) {
                    Color bg = switch (row) {
                        case 0 -> ROW_GOLD;
                        case 1 -> ROW_SILV;
                        case 2 -> ROW_BRON;
                        default -> row % 2 == 0 ? ROW_EVEN : ROW_ODD;
                    };
                    setBackground(bg);
                }
                setForeground(MainFrame.TEXT_MAIN);
                if (col == 3 && val instanceof String s) {
                    setForeground(s.startsWith("+") ? MainFrame.ACCENT2 : MainFrame.ERROR_RED);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                }
                if (col == 0 && row < 3) {
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                setHorizontalAlignment(col == 0 || col >= 2 ? CENTER : LEFT);
                return this;
            }
        });

        if (entries.isEmpty()) {
            JLabel empty = new JLabel("Noch keine Einträge.", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            empty.setForeground(MainFrame.TEXT_MUTED);
            JPanel ep = new JPanel(new BorderLayout());
            ep.setBackground(MainFrame.BG_DARK);
            ep.add(empty);
            JScrollPane sp = new JScrollPane(ep);
            sp.setBorder(new MatteBorder(1, 0, 0, 0, new Color(40, 45, 65)));
            return sp;
        }

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new MatteBorder(1, 0, 0, 0, new Color(40, 45, 65)));
        sp.getViewport().setBackground(ROW_EVEN);
        return sp;
    }

    private JPanel buildFooter(HighscoreManager mgr) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setBackground(MainFrame.BG_DARK);
        p.setBorder(new EmptyBorder(10, 16, 14, 16));

        JLabel path = new JLabel("💾 Gespeichert: " + mgr.getFilePath());
        path.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        path.setForeground(MainFrame.TEXT_MUTED);
        p.add(path, BorderLayout.WEST);

        JButton close = new JButton("Schließen");
        close.setFont(new Font("Segoe UI", Font.BOLD, 12));
        close.setBackground(MainFrame.ACCENT); close.setForeground(Color.BLACK);
        close.setFocusPainted(false); close.setBorderPainted(false); close.setOpaque(true);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.setPreferredSize(new Dimension(110, 34));
        close.addActionListener(e -> dispose());
        p.add(close, BorderLayout.EAST);
        return p;
    }
}
