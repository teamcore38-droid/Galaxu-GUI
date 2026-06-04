package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class GalaxyPanel extends JPanel {
    private static final int STAR_COUNT = 100;
    private int[] starX;
    private int[] starY;
    private float[] starAlpha;
    private final Random random = new Random(100);

    public GalaxyPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // 1. Draw Space Gradient (Deep Space Dark Navy to Deep Purple/Indigo)
        Point2D start = new Point2D.Float(0, 0);
        Point2D end = new Point2D.Float(width, height);
        float[] fractions = {0.0f, 0.6f, 1.0f};
        Color[] colors = {
                new Color(11, 4, 25),    // Deep Space Navy
                new Color(22, 10, 45),   // Space Indigo
                new Color(5, 2, 12)      // Cosmic Black
        };
        LinearGradientPaint bgPaint = new LinearGradientPaint(start, end, fractions, colors);
        g2d.setPaint(bgPaint);
        g2d.fillRect(0, 0, width, height);

        // 2. Draw Nebula Glowing Gas
        // Purple Nebula (Top-Right)
        Point2D center1 = new Point2D.Float(width * 0.85f, height * 0.15f);
        float radius1 = Math.max(width, height) * 0.6f;
        float[] nebFractions = {0.0f, 1.0f};
        Color[] nebColors1 = {new Color(125, 45, 185, 45), new Color(0, 0, 0, 0)};
        RadialGradientPaint nebPaint1 = new RadialGradientPaint(center1, radius1, nebFractions, nebColors1);
        g2d.setPaint(nebPaint1);
        g2d.fillRect(0, 0, width, height);

        // Cyan/Teal Nebula (Bottom-Left)
        Point2D center2 = new Point2D.Float(width * 0.15f, height * 0.85f);
        float radius2 = Math.max(width, height) * 0.7f;
        Color[] nebColors2 = {new Color(0, 168, 204, 35), new Color(0, 0, 0, 0)};
        RadialGradientPaint nebPaint2 = new RadialGradientPaint(center2, radius2, nebFractions, nebColors2);
        g2d.setPaint(nebPaint2);
        g2d.fillRect(0, 0, width, height);

        // 3. Draw Stars
        if (starX == null || starX.length == 0) {
            initializeStars(width, height);
        }
        for (int i = 0; i < STAR_COUNT; i++) {
            g2d.setColor(new Color(255, 255, 255, (int) (starAlpha[i] * 255)));
            int size = (i % 5 == 0) ? 2 : 1; // Differing sizes
            g2d.fillOval(starX[i] % width, starY[i] % height, size, size);
        }

        g2d.dispose();
    }

    private void initializeStars(int width, int height) {
        starX = new int[STAR_COUNT];
        starY = new int[STAR_COUNT];
        starAlpha = new float[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            starX[i] = random.nextInt(2500);
            starY[i] = random.nextInt(2500);
            starAlpha[i] = 0.2f + random.nextFloat() * 0.8f;
        }
    }
}
