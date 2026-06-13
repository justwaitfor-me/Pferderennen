package model;

import java.io.Serializable;

/** Ein einzelner Highscore-Eintrag – wird in JSON-ähnlichem Format gespeichert */
public class HighscoreEntry implements Comparable<HighscoreEntry>, Serializable {

    private final String name;
    private final int    finalBalance;
    private final int    netProfit;
    private final int    roundsPlayed;
    private final String timestamp;

    public HighscoreEntry(String name, int finalBalance, int netProfit,
                          int roundsPlayed, String timestamp) {
        this.name         = name;
        this.finalBalance = finalBalance;
        this.netProfit    = netProfit;
        this.roundsPlayed = roundsPlayed;
        this.timestamp    = timestamp;
    }

    @Override
    public int compareTo(HighscoreEntry o) {
        // Sortierung: höchstes Guthaben zuerst
        return Integer.compare(o.finalBalance, this.finalBalance);
    }

    public String getName()        { return name; }
    public int    getFinalBalance(){ return finalBalance; }
    public int    getNetProfit()   { return netProfit; }
    public int    getRoundsPlayed(){ return roundsPlayed; }
    public String getTimestamp()   { return timestamp; }

    /** Serialisierung in eine CSV-Zeile */
    public String toCsvLine() {
        return String.join(";",
                name.replace(";", "_"),
                String.valueOf(finalBalance),
                String.valueOf(netProfit),
                String.valueOf(roundsPlayed),
                timestamp);
    }

    /** Deserialisierung aus einer CSV-Zeile */
    public static HighscoreEntry fromCsvLine(String line) {
        String[] p = line.split(";", -1);
        if (p.length < 5) return null;
        try {
            return new HighscoreEntry(p[0],
                    Integer.parseInt(p[1].trim()),
                    Integer.parseInt(p[2].trim()),
                    Integer.parseInt(p[3].trim()),
                    p[4]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        String profit = netProfit >= 0 ? "+" + netProfit : String.valueOf(netProfit);
        return String.format("%-18s %6d€   %7s€   %3d Runden   %s",
                name, finalBalance, profit, roundsPlayed, timestamp);
    }
}
