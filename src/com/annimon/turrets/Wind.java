package com.annimon.turrets;

import com.annimon.turrets.util.Util;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 *
 * @author aNNiMON
 */
public class Wind {
    
    private double speed;

    public double getSpeed() {
        return speed;
    }
    
    public void change() {
        speed = Util.rand(-Constants.MAX_WIND_STRENGTH, Constants.MAX_WIND_STRENGTH);
    }
    
    public void drawInfo(Graphics2D g, FontMetrics metrics) {
        final int speedPercent = (int) (Math.abs(speed) * 100d / Constants.MAX_WIND_STRENGTH);
        final String value = String.valueOf(speedPercent);
        final int valueWidth = metrics.stringWidth(value);
        final int x = (Constants.WIDTH - valueWidth) / 2;
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
