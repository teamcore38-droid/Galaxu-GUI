package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton {
    private Color color1 = new Color(130, 49, 211); // Purple
    private Color color2 = new Color(74, 18, 142);  // Dark violet
    private boolean hovered = false;
    private final int cornerRadius = 10;

    public StyledButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    public StyledButton(String text, Color primary, Color secondary) {
        this(text);
        this.color1 = primary;
        this.color2 = secondary;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color c1 = hovered ? color1.brighter() : color1;
        Color c2 = hovered ? color2.brighter() : color2;

        GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Subtle glowing border when hovered
        if (hovered) {
            g2.setColor(new Color(255, 255, 255, 80));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }

        super.paintComponent(g2);
        g2.dispose();
    }
}
