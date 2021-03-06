package com.annimon.turrets;

import static com.annimon.turrets.Constants.GAME_FONT;
import static com.annimon.turrets.Constants.MAX_ROUNDS;
import com.annimon.turrets.Turret.TurretInfo;
import com.annimon.turrets.network.GameClient;
import com.annimon.turrets.network.GameServer;
import com.annimon.turrets.network.NetworkListener;
import com.annimon.turrets.network.SocketHelper;
import com.annimon.turrets.util.ExceptionHandler;
import com.annimon.turrets.util.Util;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author aNNiMON
 */
public final class GameCanvas extends DoubleBufferedCanvas implements Runnable, NetworkListener {
    
    private static final String WAIT_MESSAGE = "Please, wait...";
    private static final int WIN = 1, LOOSE = -1, NOTHING = 0;
    private static final String WIN_MESSAGE = "YOU WIN", LOSE_MESSAGE = "YOU LOSE";

    private final BufferedImage background;
    private Terrain terrain;
    private Turret serverTurret, clientTurret;
    private Turret instanceTurret;
    private final Wind wind;
    
    private final boolean serverInstance;
    private SocketHelper socketHelper;
    private String inetAddress;
    
    private boolean gameStarted, serverMove;
    private int serverWinCount, clientWinCount, winState;
    
    public GameCanvas(boolean serverInstance) {
        this.serverInstance = serverInstance;
        
        background = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);
        initBackground();
        
        wind = new Wind();
        gameStarted = false;
    }
    
    public void setServerAddress(String inetAddress) {
        this.inetAddress = inetAddress;
    }
    
    @Override
    protected void draw(Graphics2D g) {
        final FontMetrics metrics = g.getFontMetrics(GAME_FONT);
        g.drawImage(background, 0, 0, null);
        g.setFont(GAME_FONT);
        if (winState != NOTHING) {
            g.setColor(Color.WHITE);
            final String text = (winState == WIN) ? WIN_MESSAGE : LOSE_MESSAGE;
            final int x = (Constants.WIDTH - metrics.stringWidth(text)) / 2;
            final int y = (Constants.HEIGHT + metrics.getHeight()) / 2;
            g.drawString(text, x, y);
        } else if (gameStarted) {
            terrain.draw(g);
            serverTurret.draw(g);
            clientTurret.draw(g);
            wind.drawInfo(g, metrics);
            drawStatus(g, metrics);
        } else {
            g.setColor(Color.WHITE);
            final String str;
            if (serverInstance && (inetAddress != null)) {
                str = WAIT_MESSAGE + " " + inetAddress;
            } else str = WAIT_MESSAGE;
            final int x = (Constants.WIDTH - metrics.stringWidth(str)) / 2;
            g.drawString(str, x, Constants.HEIGHT - metrics.getHeight() * 2);
        }
    }
    
    private void drawStatus(Graphics2D g, FontMetrics metrics) {
        final String leftLabel = (serverInstance ? "YOU" : "ENEMY") + ": " + serverWinCount;
        final String rightLabel = (serverInstance ? "ENEMY" : "YOU") + ": " + clientWinCount;
        final int xBound = Constants.WIDTH / 16;
        final int rightLabelWidth = metrics.stringWidth(rightLabel);
        final int y = metrics.getHeight();
        g.setColor(Color.BLUE);
        g.drawString(leftLabel, xBound, y);
        g.setColor(Color.RED);
        g.drawString(rightLabel, Constants.WIDTH - rightLabelWidth - xBound, y);
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
                inetAddress = GameServer.getInetAddress();
                GameServer server = new GameServer(this);
                socketHelper = server.getHelper();
            } else {
                GameClient client = new GameClient(inetAddress, this);
                socketHelper = client.getHelper();
            }
            socketHelper.start();
        } catch (IOException ex) {
            ExceptionHandler.handle(ex);
        }
    }
    
    private void initBackground() {
        Background.drawToImage(background);
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
        serverWinCount = 0;
        clientWinCount = 0;
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
        if (serverWinRound) serverWinCount++;
        else clientWinCount++;
        
        final boolean serverWin = (serverWinCount == MAX_ROUNDS);
        final boolean clientWin = (clientWinCount == MAX_ROUNDS);
        if (serverWin || clientWin) {
            // Show winners.
            finishGame( (serverWin && serverInstance) || (clientWin && !serverInstance) );
        } else if (serverInstance) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) { }
            long seed = System.currentTimeMillis();
            newRound(seed);
            socketHelper.sendNewRoundSeed(seed);
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
        Main.getInstance().switchToMainMenu();
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
    
    private final Turret.TurretListener serverTurretListener = (hitOpponent) -> {
        serverMove = false;
        if (hitOpponent) {
            finishRound(true);
        }
    };
    
    private final Turret.TurretListener clientTurretListener = (hitOpponent) -> {
        serverMove = true;
        if (hitOpponent) {
            finishRound(false);
        }
    };
}
