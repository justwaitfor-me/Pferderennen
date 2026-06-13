package view;

import controller.GameController;
import model.Race;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {

    private final JLabel playerLabel, roundLabel, balanceLabel, stateLabel;

    public InfoPanel(GameController ctrl) {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        setOpaque(false);

        playerLabel  = hdr("–");
        stateLabel   = hdr("Wettphase");  stateLabel.setForeground(MainFrame.ACCENT);
        roundLabel   = hdr("Runde 1");
        balanceLabel = hdr("1000 €");     balanceLabel.setForeground(MainFrame.ACCENT2);

        add(playerLabel); add(sep());
        add(stateLabel);  add(sep());
        add(roundLabel);  add(sep());
        add(balanceLabel);
    }

    private JLabel hdr(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(MainFrame.TEXT_MAIN);
        return l;
    }

    private JLabel sep() {
        JLabel s = new JLabel("|");
        s.setForeground(new Color(70, 75, 95));
        return s;
    }

    public void update(Race race) {
        roundLabel.setText("Runde " + race.getRoundNumber());
        String txt = switch (race.getState()) {
            case BETTING  -> "Wettphase";
            case RUNNING  -> "Rennen läuft";
            case FINISHED -> "Abgeschlossen";
        };
        Color col = switch (race.getState()) {
            case BETTING  -> MainFrame.ACCENT;
            case RUNNING  -> MainFrame.BLUE;
            case FINISHED -> MainFrame.ACCENT2;
        };
        stateLabel.setText(txt);
        stateLabel.setForeground(col);
    }

    public void updateBalance(int b) {
        balanceLabel.setText(b + " €");
        balanceLabel.setForeground(b < 50 ? MainFrame.ERROR_RED : MainFrame.ACCENT2);
    }

    public void updatePlayerName(String name) {
        playerLabel.setText("👤 " + name);
    }
}
