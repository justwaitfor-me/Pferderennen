package model;

import java.util.*;

public class Race {

    public enum State { BETTING, RUNNING, FINISHED }

    private final List<Horse> horses       = new ArrayList<>();
    private final List<Bet>   bets         = new ArrayList<>();
    private State             state        = State.BETTING;
    private int               finishedCount = 0;
    private final Random      rng          = new Random();
    private int               roundNumber  = 1;

    public void addHorse(Horse h) { horses.add(h); }
    public void placeBet(Bet b)   { bets.add(b); }
    public void clearBets()       { bets.clear(); }

    public void tick() {
        if (state != State.RUNNING) return;
        for (Horse h : horses) {
            if (h.isFinished()) continue;
            double step = (0.018 / h.getOdds()) + rng.nextDouble() * 0.025;
            h.setProgress(h.getProgress() + step);
            if (h.getProgress() >= 1.0) {
                h.setFinished(true);
                h.setFinishRank(++finishedCount);
            }
        }
        if (finishedCount == horses.size()) state = State.FINISHED;
    }

    public void startRace() {
        for (Horse h : horses) h.reset();
        finishedCount = 0;
        state         = State.RUNNING;
    }

    /** Bereitet nächste Runde vor – Runde hochzählen, Wetten leeren, zurück auf BETTING */
    public void prepareNextRound() {
        clearBets();
        for (Horse h : horses) h.reset();
        finishedCount = 0;
        state         = State.BETTING;
        roundNumber++;
    }

    public Horse getWinner() {
        return horses.stream().filter(h -> h.getFinishRank() == 1).findFirst().orElse(null);
    }

    public String buildResultSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Rennergebnis – Runde ").append(roundNumber).append(" ===\n\n");
        horses.stream()
              .filter(h -> h.getFinishRank() > 0)
              .sorted(Comparator.comparingInt(Horse::getFinishRank))
              .forEach(h -> sb.append(h.toResultString(h.getFinishRank())).append("\n"));
        return sb.toString();
    }

    public List<Horse> getHorses()      { return horses; }
    public List<Bet>   getBets()        { return bets; }
    public State       getState()       { return state; }
    public int         getRoundNumber() { return roundNumber; }

    public String[] getHorseNames() {
        return horses.stream().map(Horse::getName).toArray(String[]::new);
    }
}
