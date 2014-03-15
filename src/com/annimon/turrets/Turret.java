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
    private static final int TURRET_HEIGHT = PLAYERS_BLOCK_COUNT + 1;

    // Parameters
    private final int turretX, turretY;
    private final int barrelRadius;
    private final boolean server;
    private int barrelX, barrelY;
    private double barrelAngle;
    private double shotPower;
    
    // Shooting
    private boolean shootState;
    private final ShootInfo shootInfo;
    private final Terrain terrain; // TODO GameInfo
    
    public Turret(boolean server, int turretY, Terrain terrain) {
        this.server = server;
        this.turretX = (server ? 5 : Constants.WIDTH - 5);
        this.turretY = turretY;
        this.terrain = terrain;
        barrelAngle = ANGLE_45;
        shotPower = 0.5d;
        barrelRadius = Constants.WIDTH / 20;
        shootState = false;
        shootInfo = new ShootInfo();
        calculateBarrelPosition();
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawLine(turretX, Constants.HEIGHT - turretY, barrelX, Constants.HEIGHT - barrelY);
        g.setColor(server ? Color.BLUE : Color.RED);
        g.fillArc(turretX - TURRET_WIDTH / 2, Constants.HEIGHT - turretY - TURRET_HEIGHT / 2,
                TURRET_WIDTH, TURRET_HEIGHT, 0, 180);
        if (shootState) {
            shootInfo.update(server);
            shootInfo.draw(g);
            if (shootInfo.isOver()) {
                shootState = false;
            } else if (shootInfo.isCollideTerrain(terrain)) {
                shootState = false;
                terrain.destroyTerrain((int) shootInfo.x);
            }
        }
        if (DEBUG_MODE) {
            g.setColor(Color.RED);
            double x = barrelX;
            double y = barrelY;
            final double speed = shotPower * (Constants.WIDTH / 80d);
            final double windSpeed = 0d;
            final double speedX = speed * Math.cos(barrelAngle);
            final double vsin = speed * Math.sin(barrelAngle);
            
            boolean isOver;
            double t = 0;
            do {
                final double speedY = vsin - GRAVITATION_ACCELERATION * t;
                // The longer the missile flight, the greater the wind impact.
                if (server) x += speedX + Math.sin(t) * windSpeed;
                else x -= speedX + Math.sin(t) * windSpeed;
                y += speedY;
                t += 0.01;
                
                g.drawLine((int) x, Constants.HEIGHT - (int) y, (int) x, Constants.HEIGHT - (int) y);
                isOver = (x < 0) || (x >= Constants.WIDTH) || (y < 0);
            } while (!isOver);
        }
    }
    
    public void setBarrelParams(int x, int y) {
        if (shootState) return;
        
        double angle = Math.atan2(y - turretY, x - turretX);
        if (!server) angle = Math.PI - angle;
        if ( (0d <= angle) && (angle <= ANGLE_90) ) {
            barrelAngle = angle;
            final int xlocal = (server ? x : Constants.WIDTH - x);
            shotPower = Math.sqrt(xlocal*xlocal + y*y) / (double) Constants.WIDTH;
            calculateBarrelPosition();
        }
    }
    
    public void shoot() {
        if (shootState) return;
        
        shootState = true;
        shootInfo.reset();
        shootInfo.x = barrelX;
        shootInfo.y = barrelY;
        final double speed = shotPower * (Constants.WIDTH / 80d);
        shootInfo.windSpeed = 0d;//Wind.getInstance().getSpeed();
        shootInfo.speedX = speed * Math.cos(barrelAngle);
        shootInfo.vsin = speed * Math.sin(barrelAngle);
    }
    
    private void calculateBarrelPosition() {
        final int sign = (server ? 1 : -1);
        barrelX = (int) (turretX + sign * barrelRadius * shotPower * Math.cos(barrelAngle));
        barrelY = (int) (turretY + barrelRadius * shotPower * Math.sin(barrelAngle));
    }
}
