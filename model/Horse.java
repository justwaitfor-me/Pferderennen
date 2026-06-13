package model;

public class Horse {
    private String  name;
    private String  jockey;
    private double  odds;
    private double  progress;
    private boolean finished;
    private int     finishRank;

    public Horse(String name, String jockey, double odds) {
        this.name      = name;
        this.jockey    = jockey;
        this.odds      = odds;
    }

    public void reset() { progress = 0.0; finished = false; finishRank = -1; }

    public String  getName()             { return name; }
    public void    setName(String n)     { this.name = n; }
    public String  getJockey()           { return jockey; }
    public double  getOdds()             { return odds; }
    public void    setOdds(double o)     { this.odds = o; }
    public double  getProgress()         { return progress; }
    public void    setProgress(double p) { this.progress = Math.min(1.0, Math.max(0.0, p)); }
    public boolean isFinished()          { return finished; }
    public void    setFinished(boolean f){ this.finished = f; }
    public int     getFinishRank()       { return finishRank; }
    public void    setFinishRank(int r)  { this.finishRank = r; }

    @Override
    public String toString() {
        return String.format("%s  (Jockey: %s | Quote: %.1f:1)", name, jockey, odds);
    }

    public String toResultString(int rank) {
        return String.format("#%d  %-22s  Jockey: %-15s  Quote: %.1f:1",
                rank, name, jockey, odds);
    }
}
