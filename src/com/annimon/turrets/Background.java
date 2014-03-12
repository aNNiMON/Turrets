package com.annimon.turrets;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author aNNiMON
 */
public class Background implements Constants {
    
    private static final int STARS_COUNT = (int) Math.sqrt(WIDTH * HEIGHT);
    
    private final Color bg, color;
    private final double shadeAmount;

    public Background() {
        bg = new Color(Util.randomColor(0, 35));
        color = new Color(Util.randomColor(150, 235));
        shadeAmount = Util.rand(0.5, 2.5);
    }
    
    public void draw(Graphics g) {
        draw(g, WIDTH / 2, HEIGHT / 2, HEIGHT / 3);
    }
    
    public void draw(Graphics g, int planetX, int planetY, int planetRadius) {
        g.setColor(bg);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        drawStars(g, STARS_COUNT);
        drawPlanet(g, planetX, planetY, planetRadius, color, shadeAmount);
    }
    
    private static void drawStars(Graphics g, int count) {
        g.setColor(Color.WHITE);
        for (int i = 0; i < count; i++) {
            drawPoint(g, Util.rand(WIDTH), Util.rand(HEIGHT));
        }
    }

    private static void drawPlanet(Graphics g, int xc, int yc, int rad, Color color, double shade) {
        g.setColor(Color.BLACK);
        fillCircle(g, xc, yc, rad, 0, 360);
        
        g.setColor(color);
        for (int y = -rad; y < rad; y++) {
            int x1 = (int) (Math.sqrt(rad * rad - y * y));
            for (int x = -x1; x < x1; x++) {
                final int n = (int) (Util.rand(x1) / shade);
                if (n > x1 + x) {
                    drawPoint(g, x + xc, y + yc);
                }
            }
        }
    }

    private static void fillCircle(Graphics g, int xc, int yc, int radius, int startAngle, int arcAngle) {
        g.fillArc(xc - radius, yc - radius, 2 * radius, 2 * radius, startAngle, arcAngle);
    }
    
    private static void drawPoint(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }
}
