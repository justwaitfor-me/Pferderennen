package view;

import controller.GameController;
import model.HighscoreEntry;
import model.Race;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MainFrame extends JFrame {

    // --- palette ---
    public static final Color BG_DARK    = new Color(14,  16,  24);
    public static final Color BG_PANEL   = new Color(22,  25,  38);
    public static final Color BG_HEADER  = new Color( 9,  11,  18);
    public static final Color ACCENT     = new Color(212, 175, 55);
    public static final Color ACCENT2    = new Color( 80, 200, 110);
    public static final Color TEXT_MAIN  = new Color(230, 230, 225);
    public static final Color TEXT_MUTED = new Color(130, 130, 140);
    public static final Color ERROR_RED  = new Color(210,  65,  65);
    public static final Color BLUE       = new Color( 60, 140, 220);

    private final GameController ctrl;

    private TrackPanel   trackPanel;
    private BettingPanel bettingPanel;
    private InfoPanel    infoPanel;
    private StatusBar    statusBar;

    public MainFrame(GameController ctrl) {
        super("Pferderennen Simulator");
        this.ctrl = ctrl;
        ctrl.setMainFrame(this);
        buildUI();
        setMinimumSize(new Dimension(1150, 740));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        // Spielername direkt beim Start abfragen
        SwingUtilities.invokeLater(this::askPlayerName);
    }

    private void buildUI() {
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);

        // Track links (feste Breite), BettingPanel rechts
        trackPanel   = new TrackPanel(ctrl.getRace().getHorses());
        bettingPanel = new BettingPanel(ctrl);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, trackPanel, bettingPanel);
        split.setDividerSize(3);
        split.setDividerLocation(730);
        split.setResizeWeight(0.68);
        split.setBorder(null);
        split.setBackground(BG_DARK);
        add(split, BorderLayout.CENTER);

        statusBar = new StatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout(16, 0));
        h.setBackground(BG_HEADER);
        h.setBorder(new EmptyBorder(10, 18, 10, 18));

        JLabel title = new JLabel("🏇  ROYAL RACE");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(ACCENT);
        h.add(title, BorderLayout.WEST);

        infoPanel = new InfoPanel(ctrl);
        h.add(infoPanel, BorderLayout.EAST);
        return h;
    }

    // ---- Name-Dialog ----

    private void askPlayerName() {
        PlayerNameDialog dlg = new PlayerNameDialog(this);
        dlg.setVisible(true);
        String name = dlg.getEnteredName();
        if (name != null && !name.isBlank()) {
            ctrl.startSession(name);
        } else {
            // Default
            ctrl.startSession("Spieler 1");
        }
    }

    // ---- Callbacks vom Controller ----

    public void onSessionStarted(String name) {
        infoPanel.updateBalance(ctrl.getPlayerBalance());
        infoPanel.updatePlayerName(name);
        bettingPanel.setControlsEnabled(true);
        statusBar.setMessage("Willkommen, " + name + "! Wetten platzieren und Rennen starten.", ACCENT);
    }

    public void onRaceStarted() {
        bettingPanel.setRaceRunning(true);
        statusBar.setMessage("🏁  Rennen läuft…", TEXT_MAIN);
    }

    public void onTick() {
        trackPanel.repaint();
        infoPanel.update(ctrl.getRace());
    }

    public void onRaceFinished(String summary) {
        trackPanel.repaint();
        new ResultDialog(this, summary, ctrl.getRace(), ctrl.getPlayer()).setVisible(true);

        // BUGFIX: Controls nach Rennen wieder freischalten
        bettingPanel.setRaceRunning(false);
        bettingPanel.enableNextRound(true);
        infoPanel.updateBalance(ctrl.getPlayerBalance());
        statusBar.setMessage("✅  Rennen beendet! Nächste Runde oder Spiel beenden.", ACCENT2);
    }

    public void onNewRound() {
        trackPanel.repaint();
        bettingPanel.onNewRound();
        infoPanel.update(ctrl.getRace());
        infoPanel.updateBalance(ctrl.getPlayerBalance());
        statusBar.setMessage("💰  Runde " + ctrl.getRace().getRoundNumber() + " – Wetten platzieren!", ACCENT);
    }

    public void showLeaderboard() {
        new LeaderboardDialog(this, ctrl.getHighscoreManager()).setVisible(true);
    }

    public void endSession() {
        HighscoreEntry entry = ctrl.finishSession();
        if (entry != null) {
            int rank = ctrl.getHighscoreManager().getRank(entry);
            String msg = String.format(
                    "<html><b>Spiel beendet!</b><br><br>" +
                    "Endguthaben: <b>%d€</b><br>" +
                    "Gewinn/Verlust: <b>%+d€</b><br>" +
                    "Dein Rang: <b>#%d</b></html>",
                    entry.getFinalBalance(), entry.getNetProfit(), rank);
            JOptionPane.showMessageDialog(this, msg, "Spielende", JOptionPane.INFORMATION_MESSAGE);
        }
        showLeaderboard();
        // Neues Spiel starten
        askPlayerName();
    }
}
