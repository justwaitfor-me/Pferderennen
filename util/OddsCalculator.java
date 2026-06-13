package util;

import java.util.Random;

public class OddsCalculator {

    private static final Random   rng      = new Random();
    private static final double[] BRACKETS = { 1.5, 2.0, 3.0, 4.0, 5.0, 7.0, 10.0, 15.0 };

    private OddsCalculator() {}

    public static double randomOdds(int seed) {
        int    idx    = (seed + rng.nextInt(BRACKETS.length)) % BRACKETS.length;
        double jitter = Math.round((rng.nextDouble() * 0.6 - 0.3) * 10.0) / 10.0;
        return Math.max(1.2, BRACKETS[idx] + jitter);
    }

    public static String format(double odds)      { return String.format("%.1f : 1", odds); }
    public static int impliedProbability(double o) { return (int) Math.round(100.0 / o); }
}
