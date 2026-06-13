# Pferderennen Simulator

Kurzprojekt (Java Swing) — einfache Pferderennen-Simulation mit GUI, Wetten und Leaderboard.

## Übersicht

- Grafische Oberfläche mit Swing (JFrame).
- 6 Pferde, zufällige Quoten, 3 Wetttypen: Sieg / Platz / Show.
- Animierte Rennbahn via Graphics2D + Swing Timer.
- Persistentes Highscore-CSV (max. 20 Einträge), automatisch sortiert.

## Schnellstart (Java 17+)

Kompilieren:

```sh
javac -d out -sourcepath . Main.java model/*.java util/*.java controller/*.java view/*.java
```

Ausführen:

```sh
java -cp out Main
```

## Projektstruktur

- Hauptklasse: [`Main`](Main.java) — startet die Anwendung und setzt Look&Feel.
- Controller: [`controller.GameController`](controller/GameController.java)
- View: [`view.MainFrame`](view/MainFrame.java), [`view.BettingPanel`](view/BettingPanel.java), [`view.TrackPanel`](view/TrackPanel.java), [`view.StatusBar`](view/StatusBar.java), [`view.InfoPanel`](view/InfoPanel.java), [`view.PlayerNameDialog`](view/PlayerNameDialog.java), [`view.ResultDialog`](view/ResultDialog.java), [`view.LeaderboardDialog`](view/LeaderboardDialog.java)
- Model: [`model.Player`](model/Player.java), [`model.Horse`](model/Horse.java), [`model.Bet`](model/Bet.java), [`model.Race`](model/Race.java), [`model.HighscoreEntry`](model/HighscoreEntry.java)
- Utilities: [`util.HighscoreManager`](util/HighscoreManager.java), [`util.OddsCalculator`](util/OddsCalculator.java)
- Daten: [`highscores.csv`](highscores.csv)
- Beschreibungen: [`Kurzbeschreibung.txt`](Kurzbeschreibung.txt), ursprüngliches README: [`README.txt`](README.txt)

(Alle Dateien im Projektverzeichnis)

- [highscores.csv](highscores.csv)
- [Kurzbeschreibung.txt](Kurzbeschreibung.txt)
- [Main.java](Main.java)
- [README.txt](README.txt)
- [controller/GameController.java](controller/GameController.java)
- [model/Bet.java](model/Bet.java)
- [model/HighscoreEntry.java](model/HighscoreEntry.java)
- [model/Horse.java](model/Horse.java)
- [model/Player.java](model/Player.java)
- [model/Race.java](model/Race.java)
- [util/HighscoreManager.java](util/HighscoreManager.java)
- [util/OddsCalculator.java](util/OddsCalculator.java)
- [view/BettingPanel.java](view/BettingPanel.java)
- [view/InfoPanel.java](view/InfoPanel.java)
- [view/LeaderboardDialog.java](view/LeaderboardDialog.java)
- [view/MainFrame.java](view/MainFrame.java)
- [view/PlayerNameDialog.java](view/PlayerNameDialog.java)
- [view/ResultDialog.java](view/ResultDialog.java)
- [view/StatusBar.java](view/StatusBar.java)
- [view/TrackPanel.java](view/TrackPanel.java)

## Gameplay kurz

1. Spielername eingeben (Dialog).
2. Einsatz wählen und Wetttyp auswählen.
3. Rennen starten — Pferde laufen, Platzierungen werden bestimmt.
4. Gewinn/Verlust wird berechnet; Highscore ggf. in `highscores.csv` gespeichert.

## Highscore

Verwaltet durch [`util.HighscoreManager`](util/HighscoreManager.java). Datei: [`highscores.csv`](highscores.csv) (CSV, max. 20 Einträge, automatisch sortiert).

## Hinweise für die Bewertung

- MVC-Architektur umgesetzt.
- Verwendet OOP, Vererbung und Collections.
- GUI-Interaktion über Event-Dispatch-Thread.

## Autoren

Projekt für das Ravensberger Gymansium Herford — Namen eintragen in `Kurzbeschreibung.txt` ([Kurzbeschreibung.txt](Kurzbeschreibung.txt)).

## Links

GitHub: [github.com/](https://github.com/justwaitfor-me/Pferderennen)

## Lizenz

MIT License

Copyright (c) 2026 Diego Reim

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
