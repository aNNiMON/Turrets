package com.annimon.turrets;

import static com.annimon.turrets.Constants.DEBUG_MODE;
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
public final class Turret {
    
    public static final boolean SERVER = true, CLIENT = false;
    
    private static final double ANGLE_45 = Math.PI / 4d;
    private static final double ANGLE_90 = Math.PI / 2d;
    private static final int TURRET_WIDTH = PLAYERS_BLOCK_COUNT;
    private static final int TURRET_HEIGHT = PLAYERS_BLOCK_COUNT + 1;

    // Parameters
    private final int turretX;
    private int turretY;
    private final int barrelRadius;
    private final boolean server;
    private int barrelX, barrelY;
    private double barrelAngle;
    private double shotPower;
    
    // Shooting
    private boolean shootState;
    private final ShootInfo shootInfo;
    private final Terrain terrain;
    private final Wind wind;
    private TurretListener listener;
    
    public Turret(boolean server, Terrain terrain, Wind wind) {
        this.server = server;
        this.turretX = (server ? 5 : WIDTH - 5);
        this.terrain = terrain;
        this.wind = wind;
        barrelRadius = WIDTH / 20;
        shootInfo = new ShootInfo();
        reinit();
    }
    
    public final void reinit() {
        barrelAngle = ANGLE_45;
        shotPower = 0.5d;
        shootState = false;
        if (server) turretY = terrain.getFirstBlockHeight();
        else turretY = terrain.getLastBlockHeight();
        shootInfo.reset();
        calculateBarrelPosition();
    }
    
    public void setTurretListener(TurretListener listener) {
        this.listener = listener;
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawLine(turretX, HEIGHT - turretY, barrelX, HEIGHT - barrelY);
        g.setColor(server ? Color.BLUE : Color.RED);
        g.fillArc(turretX - TURRET_WIDTH / 2, HEIGHT - turretY - TURRET_HEIGHT / 2,
                TURRET_WIDTH, TURRET_HEIGHT, 0, 180);
        if (shootState) {
            shootInfo.update(server);
            shootInfo.draw(g);
            if (shootInfo.isOver()) {
                shootState = false;
                if (listener != null) listener.shootComplete(false);
            } else if (shootInfo.isCollideOpponent(server, terrain)) {
                shootState = false;
                Sound.EXPLOSION_2.play();
                if (listener != null) listener.shootComplete(true);
            } else if (shootInfo.isCollideTerrain(terrain)) {
                shootState = false;
                Sound.EXPLOSION_1.play();
                terrain.destroyTerrain((int) shootInfo.x);
                if (listener != null) listener.shootComplete(false);
            }
        }
        if (DEBUG_MODE) {
            g.setColor(Color.RED);
            double x = barrelX;
            double y = barrelY;
            final double speed = shotPower * (WIDTH / 80d);
            final double windSpeed = wind.getSpeed();
            final double speedX = speed * Math.cos(barrelAngle);
            final double vsin = speed * Math.sin(barrelAngle);
            
            boolean isOver;
            double t = 0;
            do {
                final double speedY = vsin - GRAVITATION_ACCELERATION * t;
                // The longer the missile flight, the greater the wind impact.
                if (server) x += speedX + Math.sin(t) * windSpeed;
                else x -= speedX + Math.sin(t) * -windSpeed;
                y += speedY;
                t += 0.01;
                
                g.drawLine((int) x, HEIGHT - (int) y, (int) x, HEIGHT - (int) y);
                isOver = (x < 0) || (x >= WIDTH) || (y < 0);
            } while (!isOver);
        }
    }
    
    public void setBarrelParams(int x, int y) {
        if (shootState) return;
        
        double angle = Math.atan2(y - turretY, x - turretX);
        if (!server) angle = Math.PI - angle;
        if ( (0d <= angle) && (angle <= ANGLE_90) ) {
            barrelAngle = angle;
            final int xlocal = (server ? x : WIDTH - x);
            shotPower = Math.sqrt(xlocal*xlocal + y*y) / (double) WIDTH;
            calculateBarrelPosition();
        }
    }
    
    public TurretInfo getTurretInfo() {
        TurretInfo t = new TurretInfo();
        t.barrelAngle = this.barrelAngle;
        t.shotPower = this.shotPower;
        t.barrelX = this.barrelX;
        t.barrelY = this.barrelY;
        return t;
    }
    
    public void setTurretInfo(TurretInfo t) {
        this.barrelAngle = t.barrelAngle;
        this.shotPower = t.shotPower;
        this.barrelX = t.barrelX;
        this.barrelY = t.barrelY;
    }
    
    public void shoot() {
        if (shootState) return;
        
        shootState = true;
        shootInfo.reset();
        shootInfo.x = barrelX;
        shootInfo.y = barrelY;
        final double speed = shotPower * (WIDTH / 80d);
        shootInfo.windSpeed = wind.getSpeed() * (server ? 1 : -1);
        shootInfo.speedX = speed * Math.cos(barrelAngle);
        shootInfo.vsin = speed * Math.sin(barrelAngle);
    }
    
    private void calculateBarrelPosition() {
        final int sign = (server ? 1 : -1);
        barrelX = (int) (turretX + sign * barrelRadius * shotPower * Math.cos(barrelAngle));
        barrelY = (int) (turretY + barrelRadius * shotPower * Math.sin(barrelAngle));
    }
    
    public interface TurretListener {
        void shootComplete(boolean hitOpponent);
    }
    
    public static class TurretInfo {
        public double barrelAngle, shotPower;
        public int barrelX, barrelY;
    }
}
