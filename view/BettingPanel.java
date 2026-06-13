package view;

import controller.GameController;
import model.Bet;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Rechtes Panel mit Wettformular, Log und Steuerknöpfen.
 * Feste Breite, kein Overlap mit TrackPanel.
 */
public class BettingPanel extends JPanel {

    private final GameController ctrl;

    private JComboBox<String> horseCombo;
    private JSpinner          amountSpinner;
    private JComboBox<String> betTypeCombo;
    private JButton           betButton;
    private JButton           startButton;
    private JButton           nextRoundButton;
    private JButton           endSessionButton;
    private JButton           leaderboardButton;
    private JTextArea         betLog;
    private JLabel            balanceLabel;

    public BettingPanel(GameController ctrl) {
        this.ctrl = ctrl;
        setLayout(new BorderLayout(0, 6));
        setBackground(MainFrame.BG_PANEL);
        setBorder(new CompoundBorder(
            new MatteBorder(0, 1, 0, 0, new Color(45, 50, 68)),
            new EmptyBorder(12, 12, 12, 12)));
        setPreferredSize(new Dimension(340, 0));
        setMinimumSize(new Dimension(300, 0));
        build();
    }

    private void build() {
        add(titleLabel("💰  Wettschalter"), BorderLayout.NORTH);
        add(buildForm(),    BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
    }

    // ---- Form ----

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(MainFrame.BG_PANEL);
        GridBagConstraints g = gbc();

        // balance label
        balanceLabel = new JLabel("Guthaben:  0 €");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        balanceLabel.setForeground(MainFrame.ACCENT2);
        g.gridwidth = 2; p.add(balanceLabel, g); row(g); g.gridwidth = 1;

        // horse
        p.add(lbl("Pferd:"), g); g.gridx = 1;
        horseCombo = new JComboBox<>(ctrl.getRace().getHorseNames());
        style(horseCombo); p.add(horseCombo, g); row(g);

        // bet type
        g.gridx = 0; p.add(lbl("Wetttyp:"), g); g.gridx = 1;
        betTypeCombo = new JComboBox<>(new String[]{"Sieg", "Platz (Top 2)", "Show (Top 3)"});
        style(betTypeCombo); p.add(betTypeCombo, g); row(g);

        // amount
        g.gridx = 0; p.add(lbl("Einsatz (€):"), g); g.gridx = 1;
        amountSpinner = new JSpinner(new SpinnerNumberModel(50, 10, 9999, 10));
        amountSpinner.setPreferredSize(new Dimension(200, 28));
        styleSpinner(amountSpinner); p.add(amountSpinner, g); row(g);

        // log
        g.gridx = 0; g.gridwidth = 2;
        g.fill = GridBagConstraints.BOTH; g.weighty = 1.0;
        betLog = new JTextArea();
        betLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        betLog.setEditable(false);
        betLog.setBackground(new Color(12, 14, 22));
        betLog.setForeground(MainFrame.TEXT_MUTED);
        betLog.setBorder(new EmptyBorder(6, 6, 6, 6));
        betLog.setLineWrap(true);
        betLog.setWrapStyleWord(true);
        JScrollPane sc = new JScrollPane(betLog);
        sc.setBorder(new LineBorder(new Color(45, 50, 65)));
        sc.setPreferredSize(new Dimension(0, 160));
        p.add(sc, g);

        return p;
    }

    // ---- Buttons ----

    private JPanel buildButtons() {
        JPanel p = new JPanel(new GridLayout(5, 1, 0, 5));
        p.setBackground(MainFrame.BG_PANEL);
        p.setBorder(new EmptyBorder(8, 0, 0, 0));

        betButton = btn("Wette platzieren", MainFrame.ACCENT, Color.BLACK);
        betButton.addActionListener(e -> handleBet());
        p.add(betButton);

        startButton = btn("🏁  Rennen starten", MainFrame.BLUE, Color.WHITE);
        startButton.addActionListener(e -> ctrl.startRace());
        p.add(startButton);

        nextRoundButton = btn("▶  Nächste Runde", MainFrame.ACCENT2, Color.BLACK);
        nextRoundButton.setEnabled(false);
        nextRoundButton.addActionListener(e -> ctrl.prepareNextRound());
        p.add(nextRoundButton);

        leaderboardButton = btn("🏆  Leaderboard", new Color(100, 80, 200), Color.WHITE);
        leaderboardButton.addActionListener(e -> ((MainFrame) SwingUtilities.getWindowAncestor(this)).showLeaderboard());
        p.add(leaderboardButton);

        endSessionButton = btn("⏹  Spiel beenden", new Color(160, 50, 50), Color.WHITE);
        endSessionButton.addActionListener(e -> ((MainFrame) SwingUtilities.getWindowAncestor(this)).endSession());
        p.add(endSessionButton);

        return p;
    }

    private void handleBet() {
        String   horse = (String) horseCombo.getSelectedItem();
        int      amt   = (Integer) amountSpinner.getValue();
        Bet.Type type  = Bet.Type.values()[betTypeCombo.getSelectedIndex()];
        appendLog(ctrl.placeBet(horse, amt, type));
        balanceLabel.setText("Guthaben:  " + ctrl.getPlayerBalance() + " €");
        balanceLabel.setForeground(ctrl.getPlayerBalance() < 50 ? MainFrame.ERROR_RED : MainFrame.ACCENT2);
    }

    // ---- Public update methods ----

    public void setControlsEnabled(boolean on) {
        horseCombo.setEnabled(on);
        amountSpinner.setEnabled(on);
        betTypeCombo.setEnabled(on);
        betButton.setEnabled(on);
        startButton.setEnabled(on);
    }

    /** Wird beim Rennstart aufgerufen */
    public void setRaceRunning(boolean running) {
        horseCombo.setEnabled(!running);
        amountSpinner.setEnabled(!running);
        betTypeCombo.setEnabled(!running);
        betButton.setEnabled(!running);
        startButton.setEnabled(!running);
        nextRoundButton.setEnabled(false);
    }

    /** Wird nach Rennen aufgerufen */
    public void enableNextRound(boolean on) {
        nextRoundButton.setEnabled(on);
    }

    /** Wird bei neuer Runde aufgerufen */
    public void onNewRound() {
        horseCombo.removeAllItems();
        for (String name : ctrl.getRace().getHorseNames()) horseCombo.addItem(name);
        betLog.setText("");
        appendLog("=== Runde " + ctrl.getRace().getRoundNumber() + " – Wetten platzieren ===");
        setControlsEnabled(true);
        nextRoundButton.setEnabled(false);
        balanceLabel.setText("Guthaben:  " + ctrl.getPlayerBalance() + " €");
        balanceLabel.setForeground(ctrl.getPlayerBalance() < 50 ? MainFrame.ERROR_RED : MainFrame.ACCENT2);
    }

    public void appendLog(String msg) {
        betLog.append(msg + "\n");
        betLog.setCaretPosition(betLog.getDocument().getLength());
    }

    // ---- Style helpers ----

    private JLabel titleLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(MainFrame.ACCENT);
        l.setBorder(new EmptyBorder(0, 0, 8, 0));
        return l;
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(MainFrame.TEXT_MAIN);
        return l;
    }

    private void style(JComboBox<?> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cb.setBackground(new Color(30, 34, 50));
        cb.setForeground(MainFrame.TEXT_MAIN);
        cb.setPreferredSize(new Dimension(200, 28));
    }

    private void styleSpinner(JSpinner sp) {
        JComponent ed = sp.getEditor();
        if (ed instanceof JSpinner.DefaultEditor de) {
            de.getTextField().setBackground(new Color(30, 34, 50));
            de.getTextField().setForeground(MainFrame.TEXT_MAIN);
            de.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }
    }

    private JButton btn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false); b.setBorderPainted(false); b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 34));
        return b;
    }

    private GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0;
        g.fill  = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0; g.insets = new Insets(3, 2, 3, 4);
        g.anchor = GridBagConstraints.WEST;
        return g;
    }

    private void row(GridBagConstraints g) {
        g.gridy++; g.gridx = 0; g.weighty = 0;
        g.fill = GridBagConstraints.HORIZONTAL;
    }
}
