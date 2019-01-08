import javax.swing.*;
import java.awt.*;

public class Slot extends JPanel {

    private static final Color BACKGROUND_COLOR = Color.BLUE;
    private Color circleColor;

    public Slot() {
        circleColor = Color.WHITE;
    }

    public Slot(Dimension size) {
        circleColor = Color.WHITE;

        setPreferredSize(size);
        setBackground(BACKGROUND_COLOR);
    }

    public Slot(Color checkerColor, Dimension size) {
        circleColor = checkerColor;

        setPreferredSize(size);
        setBackground(BACKGROUND_COLOR);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int radius = (int) calcRadius();

        g.setColor(circleColor);
        g.fillOval(0, 0, radius, radius);
    }

    private double calcRadius() {
        int width = getPreferredSize().width;
        return width * 0.9;
    }

    public void setCircleColor(Color color) {
        circleColor = color;
        repaint();
    }

    public Color getCircleColor() {
        return circleColor;
    }
}
