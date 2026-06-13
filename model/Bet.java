package model;

public class Bet {

    public enum Type { WIN, PLACE, SHOW }

    private final String playerName;
    private final Horse  horse;
    private final int    amount;
    private final Type   type;
    private boolean      settled;
    private int          payout;

    public Bet(String playerName, Horse horse, int amount, Type type) {
        this.playerName = playerName;
        this.horse      = horse;
        this.amount     = amount;
        this.type       = type;
    }

    public void settle(int rank) {
        if (settled) return;
        settled = true;
        boolean won = switch (type) {
            case WIN   -> rank == 1;
            case PLACE -> rank <= 2;
            case SHOW  -> rank <= 3;
        };
        if (won) {
            double mult = switch (type) {
                case WIN   -> horse.getOdds();
                case PLACE -> horse.getOdds() * 0.45;
                case SHOW  -> horse.getOdds() * 0.20;
            };
            payout = (int) Math.round(amount * mult);
        }
    }

    public String toDisplayString() {
        String t = switch (type) {
            case WIN   -> "Sieg";
            case PLACE -> "Platz";
            case SHOW  -> "Show";
        };
        return String.format("%-20s | %-6s | Einsatz: %4d€ | Gewinn: %4d€",
                horse.getName(), t, amount, payout);
    }

    public String getPlayerName() { return playerName; }
    public Horse  getHorse()      { return horse; }
    public int    getAmount()     { return amount; }
    public Type   getType()       { return type; }
    public boolean isSettled()    { return settled; }
    public int    getPayout()     { return payout; }
}
