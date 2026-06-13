package model;

import java.util.*;

public class Player {

    private String         name;
    private int            balance;
    private final List<Bet> betHistory = new ArrayList<>();

    public Player(String name, int startBalance) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Spielername darf nicht leer sein.");
        this.name    = name.strip();
        this.balance = startBalance;
    }

    public boolean canAfford(int amount) { return amount > 0 && balance >= amount; }

    public void deduct(int amount) {
        if (!canAfford(amount)) throw new IllegalStateException("Nicht genug Guthaben.");
        balance -= amount;
    }

    public void credit(int amount) { balance += amount; }

    public void recordBet(Bet b) { betHistory.add(b); }

    public int calcNetProfit() {
        int in  = betHistory.stream().mapToInt(Bet::getAmount).sum();
        int out = betHistory.stream().mapToInt(Bet::getPayout).sum();
        return out - in;
    }

    public String getStatsString() {
        int net = calcNetProfit();
        return String.format("Spieler: %-15s | Guthaben: %5d€ | G/V: %s%d€",
                name, balance, net >= 0 ? "+" : "", net);
    }

    public String  getName()         { return name; }
    public void    setName(String n) { this.name = n.strip(); }
    public int     getBalance()      { return balance; }
    public List<Bet> getBetHistory() { return betHistory; }

    @Override
    public String toString() { return name + " (" + balance + "€)"; }
}
