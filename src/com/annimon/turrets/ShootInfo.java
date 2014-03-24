package com.annimon.turrets;

import static com.annimon.turrets.Constants.GRAVITATION_ACCELERATION;
import static com.annimon.turrets.Constants.HEIGHT;
import static com.annimon.turrets.Constants.PLAYERS_BLOCK_COUNT;
import static com.annimon.turrets.Constants.WIDTH;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author aNNiMON
 */
public final class ShootInfo {
    
    double x, y, speedX;
    double windSpeed, vsin, t;
        
    public void reset() {
        t = 0;
    }

    public void update(boolean server) {
        final double speedY = vsin - GRAVITATION_ACCELERATION * t;
        // The longer the missile flight, the greater the wind impact.
        if (server) x += getDeltaX();
        else x -= getDeltaX();
        y += speedY;
        t += 0.01;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        final int rad = 2;
        g.fillArc((int) x - rad, HEIGHT - (int) y - rad, rad * 2, rad * 2, 0, 360);
    }

    public boolean isOver() {
         return (x < 0) || (x >= WIDTH) || (y < 0);
    }
    
    public boolean isCollideOpponent(boolean server, Terrain tr) {
        boolean serverXCheck = server && (x >= WIDTH - PLAYERS_BLOCK_COUNT - 1);
        boolean clientXCheck = !server && (x <= PLAYERS_BLOCK_COUNT);
        if (serverXCheck || clientXCheck) {
            final int opponentY = server ? tr.getLastBlockHeight() : tr.getFirstBlockHeight();
            return (Math.abs(y - opponentY) <= 4);
        }
        return false;
    }

    public boolean isCollideTerrain(Terrain terrain) {
        return terrain.isCollide((int) x, (int) y);
    }
    
    private double getDeltaX() {
        return speedX + Math.sin(t) * windSpeed;
    }
}