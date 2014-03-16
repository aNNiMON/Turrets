package com.annimon.turrets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author aNNiMON
 */
public class GameCanvas extends DoubleBufferedCanvas implements Runnable, NetworkListener {

    private final BufferedImage background;
    private Terrain terrain;
    private Turret serverTurret, clientTurret;
    
    private final boolean serverInstance;
    private SocketHelper socketHelper;
    
    private boolean gameStarted;
    
    public GameCanvas(boolean serverInstance) {
        this.serverInstance = serverInstance;
        
        background = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);
        initBackground();
        
        gameStarted = false;
    }
    
    @Override
    protected void draw(Graphics2D g) {
        g.drawImage(background, 0, 0, null);
        if (gameStarted) {
            terrain.draw(g);
            serverTurret.draw(g);
            clientTurret.draw(g);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Please, wait...", Constants.WIDTH - 100, Constants.HEIGHT - 20);
        }
    }
    
    @Override
    public void onStatusChanged(int status, Object data) {
        if (serverInstance) serverNetworkStatus(status, data);
        else clientNetworkStatus(status, data);
    }
    
    private void serverNetworkStatus(int status, Object data) {
        switch (status) {
            case ON_CONNECT:
                long seed = System.currentTimeMillis();
                socketHelper.sendSeed(seed);
                startGame(seed);
                break;
        }
    }

    private void clientNetworkStatus(int status, Object data) {
        switch (status) {
            case ON_SEED_RECEIVED:
                startGame((long) data);
                break;
        }
    }
    
    @Override
    public void run() {
        initNetwork();
    }
    
    private void initNetwork() {
        try {
            if (serverInstance) {
                GameServer server = new GameServer(this);
                socketHelper = server.getHelper();
            } else {
                GameClient client = new GameClient("localhost", this);
                socketHelper = client.getHelper();
            }
            socketHelper.start();
        } catch (IOException ex) {
        }
    }
    
    private void initBackground() {
        final Graphics g = background.createGraphics();
        new Background().draw(g);
        g.dispose();
    }
    
    private void startGame(long seed) {
        terrain = new Terrain(Constants.WIDTH);
        terrain.generate(seed);
        
        serverTurret = new Turret(Turret.SERVER, terrain.getFirstBlockHeight(), terrain);
        clientTurret = new Turret(Turret.CLIENT, terrain.getLastBlockHeight(), terrain);
        
        gameStarted = true;
    }

    @Override
    protected void mousePressed(int x, int y) {
        if (!gameStarted) return;
        clientTurret.setBarrelParams(x, Constants.HEIGHT - y);
    }

    @Override
    protected void mouseReleased(int x, int y) {
        if (!gameStarted) return;
        clientTurret.shoot();
    }

    @Override
    protected void mouseDragged(int x, int y) {
        if (!gameStarted) return;
        clientTurret.setBarrelParams(x, Constants.HEIGHT - y);
    }
}
