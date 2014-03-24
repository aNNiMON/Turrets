package com.annimon.turrets;

import static com.annimon.turrets.Constants.MAX_WIND_STRENGTH;
import static com.annimon.turrets.Constants.WIDTH;
import com.annimon.turrets.util.Util;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 *
 * @author aNNiMON
 */
public final class Wind {
    
    private double speed;

    public double getSpeed() {
        return speed;
    }
    
    public void change() {
        speed = Util.rand(-MAX_WIND_STRENGTH, MAX_WIND_STRENGTH);
    }
    
    public void drawInfo(Graphics2D g, FontMetrics metrics) {
        final int speedPercent = (int) (Math.abs(speed) * 100d / MAX_WIND_STRENGTH);
        final String value = String.valueOf(speedPercent);
        final int valueWidth = metrics.stringWidth(value);
        final int x = (WIDTH - valueWidth) / 2;
        final int y = metrics.getHeight();
        
        g.setColor(Color.RED);
        if (speed < 0) {
            g.drawString("←", x - 2 * valueWidth, y);
        } else if (speed > 0) {
            g.drawString("→", x + 2 * valueWidth, y);
        }
        g.drawString(String.valueOf(speedPercent), x, y);
    }
    
}
