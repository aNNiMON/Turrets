package com.annimon.turrets;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author aNNiMON
 */
public abstract class DoubleBufferedCanvas extends Canvas {
    
    private final Graphics2D G;
    private final BufferedImage buffer;
    private final DrawingThread thread;
    
    public DoubleBufferedCanvas() {
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setPreferredSize(Constants.SCREEN_DIMENSION);
        
        buffer = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);
        G = buffer.createGraphics();
        thread = new DrawingThread();
    }
    
    public void start() {
        thread.start();
    }
    
    public void stop() {
        thread.keepRunning = false;
    }

    @Override
    public void paint(Graphics g) {
        draw(G);
        G.dispose();
        g.drawImage(buffer, 0, 0, null);
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }
    
    protected abstract void draw(Graphics2D g);

    protected abstract void update();
    
    private final class DrawingThread extends Thread {

        private boolean keepRunning = true;

        @Override
        public void run() {
            while (keepRunning) {
                update();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {}
                repaint();
            }
        }
    }
}
