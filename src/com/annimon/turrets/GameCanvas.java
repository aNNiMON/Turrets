package com.annimon.turrets;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author aNNiMON
 */
public class GameCanvas extends DoubleBufferedCanvas implements NetworkListener {

    private final BufferedImage background;
    private final Terrain terrain;
    private Turret serverTurret, clientTurret;
    
    public GameCanvas() {
        background = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);
        initBackground();
        
        terrain = new Terrain(Constants.WIDTH);
        terrain.generate(System.currentTimeMillis());
        
        serverTurret = new Turret(Turret.SERVER, terrain.getFirstBlockHeight(), terrain);
        clientTurret = new Turret(Turret.CLIENT, terrain.getLastBlockHeight(), terrain);
    }
    
    @Override
    protected void draw(Graphics2D g) {
        g.drawImage(background, 0, 0, null);
        terrain.draw(g);
        serverTurret.draw(g);
        clientTurret.draw(g);
    }
    
    @Override
    public void onStatusChanged(int status, Object data) {
        switch (status) {
            case ON_CONNECT:
                System.out.println("connect!");
                break;
        }
    }

    @Override
    protected void update() {
        
    }

    private void initBackground() {
        final Graphics g = background.createGraphics();
        new Background().draw(g);
        g.dispose();
    }

    @Override
    protected void mousePressed(int x, int y) {
        clientTurret.setBarrelParams(x, Constants.HEIGHT - y);
    }

    @Override
    protected void mouseReleased(int x, int y) {
        clientTurret.shoot();
    }

    @Override
    protected void mouseDragged(int x, int y) {
        clientTurret.setBarrelParams(x, Constants.HEIGHT - y);
    }
}
