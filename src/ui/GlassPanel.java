package ui;

import javax.swing.*;
import java.awt.*;

public class GlassPanel extends JPanel {
    private int cornerRadius = 15;

    public GlassPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
    }

    public GlassPanel(LayoutManager layout) {
        setOpaque(false);
        setLayout(layout);
    }

    public GlassPanel(LayoutManager layout, int cornerRadius) {
        this(layout);
        this.cornerRadius = cornerRadius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Glassmorphism background - semi-translucent dark slate
        g2.setColor(new Color(25, 20, 48, 175));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Glass glare outline
        g2.setColor(new Color(255, 255, 255, 25));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();
    }
}
