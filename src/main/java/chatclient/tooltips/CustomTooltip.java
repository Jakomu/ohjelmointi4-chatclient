package chatclient.tooltips;

import static chatclient.resources.constants.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class CustomTooltip extends JLabel {
    private Dimension tooltipSize;

    public CustomTooltip(String text, Dimension size) {
        super(text);
        tooltipSize = size;

        setBackground(TOOLTIP_COLOR);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setFont(DEFAULT_FONT);
        setOpaque(true);
        setVisible(false);
    }

    public void showTooltip(Point location) {
        setBounds(location.x, location.y, tooltipSize.width, tooltipSize.height);
        setVisible(true);
    }

    public void hideTooltip() {
        setVisible(false);
    }
}
