package view;

import model.Horse;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TrackPanel extends JPanel {

    private final List<Horse> horses;

    private static final Color GRASS   = new Color(34,  80,  34);
    private static final Color LANE_A  = new Color(155, 110, 48);
    private static final Color LANE_B  = new Color(125,  85, 32);
    private static final Color RAIL    = new Color(235, 225, 195);
    private static final Color SHADOW  = new Color(0, 0, 0, 70);

    static final Color[] HORSE_COLORS = {
        new Color(220,  50,  50),
        new Color( 50, 120, 230),
        new Color(215, 155,  25),
        new Color( 55, 185,  95),
        new Color(170,  70, 215),
        new Color(225, 100,  35),
    };

    public TrackPanel(List<Horse> horses) {
        this.horses = horses;
        setBackground(GRASS);
        setPreferredSize(new Dimension(730, 560));
        setMinimumSize(new Dimension(500, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int W = getWidth(), H = getHeight();
        int n = horses.size();

        final int TOP    = 58;
        final int BOT    = 45;
        final int LEFT   = 135;
        final int RIGHT  = 55;
        int trackH = H - TOP - BOT;
        int laneH  = trackH / Math.max(n, 1);
        int trackW = W - LEFT - RIGHT;

        // grass background
        g2.setColor(GRASS);
        g2.fillRect(0, 0, W, H);

        // dirt lanes
        for (int i = 0; i < n; i++) {
            g2.setColor(i % 2 == 0 ? LANE_A : LANE_B);
            g2.fillRect(LEFT, TOP + i * laneH, trackW + RIGHT, laneH);
        }

        // rails
        g2.setColor(RAIL);
        g2.setStroke(new BasicStroke(3f));
        g2.drawLine(LEFT, TOP, W, TOP);
        g2.drawLine(LEFT, TOP + n * laneH, W, TOP + n * laneH);

        // distance markers
        g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
        for (int pct : new int[]{25, 50, 75}) {
            int x = LEFT + (int)(trackW * pct / 100.0);
            g2.setColor(new Color(255, 255, 255, 90));
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    10, new float[]{5, 4}, 0));
            g2.drawLine(x, TOP, x, TOP + n * laneH);
            g2.setColor(new Color(255, 255, 200, 160));
            g2.drawString(pct + "%", x - 10, TOP - 6);
        }

        // checkered finish line
        int fx = W - RIGHT - 1;
        int sq = 9;
        for (int row = 0; row < (n * laneH) / sq; row++)
            for (int col = 0; col < 4; col++) {
                g2.setColor((row + col) % 2 == 0 ? Color.WHITE : Color.BLACK);
                g2.fillRect(fx - col * sq, TOP + row * sq, sq, sq);
            }

        // lane labels
        Font lf = new Font("Segoe UI", Font.BOLD, 11);
        g2.setFont(lf);
        for (int i = 0; i < n; i++) {
            Horse hc = horses.get(i);
            int   cy = TOP + i * laneH + laneH / 2;
            Color col = HORSE_COLORS[i % HORSE_COLORS.length];

            // badge
            g2.setColor(col);
            g2.fillOval(5, cy - 10, 20, 20);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g2.drawString(String.valueOf(i + 1), 11, cy + 5);

            // name + quote
            g2.setColor(new Color(255, 255, 255, 210));
            g2.setFont(lf);
            g2.drawString(hc.getName(), 30, cy - 1);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.setColor(new Color(255, 240, 150, 180));
            g2.drawString(String.format("%.1f:1", hc.getOdds()), 30, cy + 12);
        }

        // horses
        for (int i = 0; i < n; i++) {
            Horse hc = horses.get(i);
            int   cy = TOP + i * laneH + laneH / 2;
            int   hx = LEFT + (int)(trackW * hc.getProgress());
            drawSprite(g2, hx, cy, HORSE_COLORS[i % HORSE_COLORS.length],
                       hc.isFinished(), hc.getFinishRank());
        }

        // header overlay
        g2.setColor(new Color(0, 0, 0, 175));
        g2.fillRect(0, 0, W, TOP - 2);
        g2.setColor(MainFrame.ACCENT);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 19));
        g2.drawString("ROYAL RACE  –  2000m Flachrennen", 18, 36);

        g2.dispose();
    }

    private void drawSprite(Graphics2D g2, int cx, int cy, Color col, boolean finished, int rank) {
        if (finished) {
            g2.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 50));
            g2.fillOval(cx - 24, cy - 24, 48, 48);
        }
        // shadow
        g2.setColor(SHADOW);
        g2.fillOval(cx - 14, cy + 7, 28, 8);
        // body
        g2.setColor(col);
        g2.fillOval(cx - 18, cy - 8, 36, 18);
        // head
        g2.fillOval(cx + 12, cy - 14, 14, 12);
        // legs
        g2.setColor(col.darker());
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int[][] legs = {{-10,-3,5,12},{-3,0,12,5},{5,7,14},{12,14,14}};
        g2.drawLine(cx-10, cy+9, cx-12, cy+18);
        g2.drawLine(cx-3,  cy+9, cx-2,  cy+18);
        g2.drawLine(cx+5,  cy+9, cx+7,  cy+18);
        g2.drawLine(cx+12, cy+9, cx+14, cy+18);
        // mane
        g2.setColor(col.brighter());
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(cx+12, cy-12, cx+10, cy-5);
        g2.drawLine(cx+15, cy-14, cx+13, cy-7);
        // rank badge
        if (finished && rank > 0) {
            g2.setColor(rank == 1 ? MainFrame.ACCENT : MainFrame.TEXT_MAIN);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.drawString("#" + rank, cx - 6, cy - 20);
        }
    }
}
