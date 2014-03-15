package com.annimon.turrets;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author aNNiMON
 */
public class Turret implements Constants {
    
    public static final boolean SERVER = true, CLIENT = false;
    
    private static final double ANGLE_45 = Math.PI / 4d;
    private static final double ANGLE_90 = Math.PI / 2d;
    private static final int TURRET_WIDTH = PLAYERS_BLOCK_COUNT;
    private static final int TURRET_HEIGHT = PLAYERS_BLOCK_COUNT;

    private final int turretX, turretY;
    private final int barrelRadius;
    private final boolean server;
    private int barrelX, barrelY;
    private double barrelAngle;
    private double shotPower;
    
    public Turret(boolean server, int turretY) {
        this.server = server;
        this.turretX = (server ? 5 : Constants.WIDTH - 5);
        this.turretY = turretY;
        barrelAngle = ANGLE_45;
        shotPower = 0.5d;
        barrelRadius = Constants.WIDTH / 20;
        calculateBarrelPosition();
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawLine(turretX, Constants.HEIGHT - turretY - 1, barrelX, Constants.HEIGHT - barrelY);
        g.setColor(server ? Color.BLUE : Color.RED);
        g.fillArc(turretX - TURRET_WIDTH / 2, Constants.HEIGHT - turretY - TURRET_HEIGHT / 2,
                TURRET_WIDTH, TURRET_HEIGHT, 0, 180);
    }
    
    private void calculateBarrelPosition() {
        final int sign = (server ? 1 : -1);
        barrelX = (int) (turretX + sign * barrelRadius * shotPower * Math.cos(barrelAngle));
        barrelY = (int) (turretY + barrelRadius * shotPower * Math.sin(barrelAngle));
    }
}
