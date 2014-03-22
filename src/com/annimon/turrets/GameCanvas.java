package com.annimon.turrets;

import com.annimon.turrets.Turret.TurretInfo;
import com.annimon.turrets.network.GameClient;
import com.annimon.turrets.network.GameServer;
import com.annimon.turrets.network.NetworkListener;
import com.annimon.turrets.network.SocketHelper;
import com.annimon.turrets.util.Util;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author aNNiMON
 */
public class GameCanvas extends DoubleBufferedCanvas implements Runnable, NetworkListener {
    
    private static final String WAIT_MESSAGE = "Please, wait...";
    private static final int WIN = 1, LOOSE = -1, NOTHING = 0;
    private static final String WIN_MESSAGE = "YOU WIN", LOOSE_MESSAGE = "YOU LOOSE";

    private final BufferedImage background;
    private final Font font;
    private Terrain terrain;
    private Turret serverTurret, clientTurret;
    private Turret instanceTurret;
    private final Wind wind;
    
    private final boolean serverInstance;
    private SocketHelper socketHelper;
    
    private int roundWinCount, winState;
    private boolean gameStarted, serverMove;
    
    public GameCanvas(boolean serverInstance) {
        this.serverInstance = serverInstance;
        
        background = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);
        initBackground();
        font = new Font(Constants.FONT_NAME, Font.PLAIN, 24);
        
        wind = new Wind();
        gameStarted = false;
    }
    
    @Override
    protected void draw(Graphics2D g) {
        final FontMetrics metrics = g.getFontMetrics(font);
        g.drawImage(background, 0, 0, null);
        g.setFont(font);
        if (winState != NOTHING) {
            g.setColor(Color.WHITE);
            final String text = (winState == WIN) ? WIN_MESSAGE : LOOSE_MESSAGE;
            final int x = (Constants.WIDTH - metrics.stringWidth(text)) / 2;
            final int y = (Constants.HEIGHT + metrics.getHeight()) / 2;
            g.drawString(text, x, y);
        } else if (gameStarted) {
            terrain.draw(g);
            serverTurret.draw(g);
            clientTurret.draw(g);
            wind.drawInfo(g, metrics);
        } else {
            g.setColor(Color.WHITE);
            final int x = (Constants.WIDTH - metrics.stringWidth(WAIT_MESSAGE)) / 2;
            g.drawString(WAIT_MESSAGE, x, Constants.HEIGHT - metrics.getHeight() * 2);
        }
    }
    
    @Override
    public void onStatusChanged(int status, Object data) {
        if (status == ON_DISCONNECT) {
            onExit();
            return;
        }
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
            case ON_MOVE_RECEIVED:
                clientTurret.setTurretInfo((TurretInfo) data);
                clientTurret.shoot();
                break;
        }
    }

    private void clientNetworkStatus(int status, Object data) {
        switch (status) {
            case ON_SEED_RECEIVED:
                startGame((long) data);
                break;
            case ON_MOVE_RECEIVED:
                serverTurret.setTurretInfo((TurretInfo) data);
                serverTurret.shoot();
                break;
            case ON_NEW_ROUND:
                newRound((long) data);
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
        newRound(seed);
        
        serverTurret = new Turret(Turret.SERVER, terrain, wind);
        serverTurret.setTurretListener(serverTurretListener);
        clientTurret = new Turret(Turret.CLIENT, terrain, wind);
        clientTurret.setTurretListener(clientTurretListener);
        
        instanceTurret = (serverInstance) ? serverTurret : clientTurret;
        
        gameStarted = true;
        serverMove = true;
        roundWinCount = 0;
        winState = NOTHING;
    }
    
    private void newRound(long seed) {
        Util.setRandomSeed(seed);
        // Reinit background with same seed on server and client.
        initBackground();
        terrain.generate();
        wind.change();
        
        if (gameStarted) {
            serverTurret.reinit();
            clientTurret.reinit();
        }
    }
    
    private void finishRound(boolean serverWinRound) {
        if (serverInstance && serverWinRound) roundWinCount++;
        else if (!serverInstance && !serverWinRound) roundWinCount--;
        
        if (roundWinCount == Constants.MAX_ROUNDS) finishGame(true);
        else if (roundWinCount == -Constants.MAX_ROUNDS) finishGame(false);
        else {
            if (serverInstance) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) { }
                long seed = System.currentTimeMillis();
                newRound(seed);
                socketHelper.sendNewRoundSeed(seed);
            }
        }
    }
    
    private void finishGame(boolean instanceWin) {
        winState = (instanceWin) ? WIN : LOOSE;
        gameStarted = false;
    }
    
    @Override
    protected void onExit() {
        if (socketHelper != null) {
            try {
                socketHelper.close();
            } catch (IOException ex) { }
        }
        Main.getInstance().switchLayout(Main.getInstance().getMainMenu());
    }
    
    private boolean allowMove() {
        return (gameStarted && !(serverInstance ^ serverMove));
    }

    @Override
    protected void mousePressed(int x, int y) {
        if (!allowMove()) return;
        instanceTurret.setBarrelParams(x, Constants.HEIGHT - y);
    }

    @Override
    protected void mouseReleased(int x, int y) {
        if (!allowMove()) return;
        socketHelper.sendMove(instanceTurret.getTurretInfo());
        instanceTurret.shoot();
    }

    @Override
    protected void mouseDragged(int x, int y) {
        if (!allowMove()) return;
        instanceTurret.setBarrelParams(x, Constants.HEIGHT - y);
    }
    
    private final Turret.TurretListener serverTurretListener = new Turret.TurretListener() {

        @Override
        public void shootComplete(boolean hitOpponent) {
            serverMove = false;
            if (hitOpponent) {
                finishRound(serverInstance);
            }
        }
    };
    
    private final Turret.TurretListener clientTurretListener = new Turret.TurretListener() {

        @Override
        public void shootComplete(boolean hitOpponent) {
            serverMove = true;
            if (hitOpponent) {
                finishRound(!serverInstance);
            }
        }
    };
}
