package util;

import model.HighscoreEntry;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class HighscoreManager {

    private static final String FILE_NAME = "highscores.csv";
    private static final int    MAX_ENTRIES = 20;
    private static final String HEADER = "name;finalBalance;netProfit;roundsPlayed;timestamp";

    private final Path filePath;
    private final List<HighscoreEntry> entries = new ArrayList<>();

    public HighscoreManager() {
        filePath = Paths.get(FILE_NAME);
        load();
    }

    private void load() {
        if (!Files.exists(filePath)) return;
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("name;") || line.isBlank()) continue;
                HighscoreEntry e = HighscoreEntry.fromCsvLine(line);
                if (e != null) entries.add(e);
            }
        } catch (IOException e) {
            System.err.println("Highscore laden fehlgeschlagen: " + e.getMessage());
        }
        Collections.sort(entries);
    }

    public void addEntry(HighscoreEntry entry) {
        entries.add(entry);
        Collections.sort(entries);
        if (entries.size() > MAX_ENTRIES) entries.subList(MAX_ENTRIES, entries.size()).clear();
        save();
    }

    private void save() {
        try (BufferedWriter bw = Files.newBufferedWriter(filePath)) {
            bw.write(HEADER);
            bw.newLine();
            for (HighscoreEntry e : entries) {
                bw.write(e.toCsvLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Highscore speichern fehlgeschlagen: " + e.getMessage());
        }
    }

    public List<HighscoreEntry> getEntries() { return Collections.unmodifiableList(entries); }

    public int getRank(HighscoreEntry target) {
        for (int i = 0; i < entries.size(); i++)
            if (entries.get(i) == target) return i + 1;
        return -1;
    }

    public String getFilePath() { return filePath.toAbsolutePath().toString(); }
}
