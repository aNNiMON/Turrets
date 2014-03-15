package com.annimon.turrets;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author aNNiMON
 */
public class GameCanvas extends Canvas {
    
    private final Graphics2D G;
    private final BufferedImage buffer;
    private final Terrain terrain;
    private BufferedImage background;
    
    public GameCanvas() {
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setPreferredSize(Constants.SCREEN_DIMENSION);
        
        buffer = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);
        G = buffer.createGraphics();
        
        initBackground();
        terrain = new Terrain(Constants.WIDTH);
        terrain.generate(System.currentTimeMillis());
        
        new DrawingThread().start();
    }

    @Override
    public void paint(Graphics g) {
        G.drawImage(background, 0, 0, null);
        terrain.draw(G);
        g.drawImage(buffer, 0, 0, null);
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }
    
    private void initBackground() {
        background = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = background.createGraphics();
        new Background().draw(g);
        g.dispose();
    }
    
    private class DrawingThread extends Thread {

        private boolean keepRunning = true;

        @Override
        public void run() {
            while (keepRunning) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {}
                repaint();
            }
        }
    }
}
