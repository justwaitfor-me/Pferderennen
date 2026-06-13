package controller;

import model.*;
import util.OddsCalculator;
import util.HighscoreManager;
import view.MainFrame;

import javax.swing.Timer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GameController {

    private static final int TICK_MS    = 60;
    private static final int START_BANK = 1000;

    private final Race             race;
    private       Player           player;
    private       Timer            raceTimer;
    private       MainFrame        mainFrame;
    private final HighscoreManager highscoreManager;

    public GameController() {
        race             = new Race();
        highscoreManager = new HighscoreManager();
        initHorses();
    }

    private void initHorses() {
        String[][] data = {
            { "Donner Blitz",  "M. Müller"  },
            { "Silberpfeil",   "L. Schmidt" },
            { "Sturmwind",     "K. Weber"   },
            { "Goldmähne",     "F. Braun"   },
            { "Schattenjäger", "R. Klein"   },
            { "Feuergeist",    "T. Wolf"    },
        };
        for (int i = 0; i < data.length; i++)
            race.addHorse(new Horse(data[i][0], data[i][1], OddsCalculator.randomOdds(i)));
    }

    public void setMainFrame(MainFrame f) { this.mainFrame = f; }

    /** Wird nach Name-Eingabe aufgerufen */
    public void startSession(String playerName) {
        this.player = new Player(playerName, START_BANK);
        mainFrame.onSessionStarted(player.getName());
    }

    // ---- Wetten ----

    public String placeBet(String horseName, int amount, Bet.Type type) {
        if (player == null)        return "Erst einen Spielernamen eingeben.";
        if (race.getState() != Race.State.BETTING)
                                   return "Wetten nur vor dem Rennen möglich.";
        if (!player.canAfford(amount))
                                   return "Nicht genug Guthaben! (Verfügbar: " + player.getBalance() + "€)";
        if (amount < 10)           return "Mindesteinsatz ist 10€.";

        Horse target = race.getHorses().stream()
                           .filter(h -> h.getName().equals(horseName))
                           .findFirst().orElse(null);
        if (target == null) return "Pferd nicht gefunden.";

        Bet bet = new Bet(player.getName(), target, amount, type);
        race.placeBet(bet);
        player.recordBet(bet);
        player.deduct(amount);
        return "✓ Wette: " + bet.toDisplayString();
    }

    // ---- Rennen ----

    public void startRace() {
        if (player == null)                           return;
        if (race.getState() != Race.State.BETTING)    return;

        race.startRace();
        mainFrame.onRaceStarted();

        raceTimer = new Timer(TICK_MS, e -> {
            race.tick();
            mainFrame.onTick();
            if (race.getState() == Race.State.FINISHED) {
                raceTimer.stop();
                settleAllBets();
                mainFrame.onRaceFinished(race.buildResultSummary());
            }
        });
        raceTimer.start();
    }

    /** Nächste Runde – Zustand zurücksetzen, neue Quoten */
    public void prepareNextRound() {
        race.prepareNextRound();
        rerollOdds();
        mainFrame.onNewRound();
    }

    /** Spiel beenden + Highscore speichern */
    public HighscoreEntry finishSession() {
        if (player == null) return null;
        String ts = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        HighscoreEntry entry = new HighscoreEntry(
                player.getName(),
                player.getBalance(),
                player.calcNetProfit(),
                race.getRoundNumber() - 1,
                ts);
        highscoreManager.addEntry(entry);
        return entry;
    }

    private void rerollOdds() {
        List<Horse> h = race.getHorses();
        for (int i = 0; i < h.size(); i++) h.get(i).setOdds(OddsCalculator.randomOdds(i));
    }

    private void settleAllBets() {
        for (Bet b : race.getBets()) {
            b.settle(b.getHorse().getFinishRank());
            if (b.getPayout() > 0) player.credit(b.getPayout());
        }
    }

    public int              getPlayerBalance()  { return player != null ? player.getBalance() : START_BANK; }
    public Player           getPlayer()         { return player; }
    public Race             getRace()           { return race; }
    public HighscoreManager getHighscoreManager(){ return highscoreManager; }
}
