package com.annimon.turrets;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 *
 * @author aNNiMON
 */
public abstract class DoubleBufferedCanvas extends Canvas implements MouseListener, MouseMotionListener, KeyListener {
    
    private final Graphics2D G;
    private final BufferedImage buffer;
    private final DrawingThread thread;
    
    public DoubleBufferedCanvas() {
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setPreferredSize(Constants.SCREEN_DIMENSION);
        
        addMouseListener(DoubleBufferedCanvas.this);
        addMouseMotionListener(DoubleBufferedCanvas.this);
        addKeyListener(DoubleBufferedCanvas.this);
        
        buffer = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);
        G = buffer.createGraphics();
        thread = new DrawingThread();
        thread.start();
    }

    @Override
    public void paint(Graphics g) {
        draw(G);
        g.drawImage(buffer, 0, 0, null);
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }
    
    protected abstract void draw(Graphics2D g);

    protected abstract void mousePressed(int x, int y);
    
    protected abstract void mouseReleased(int x, int y);
    
    protected abstract void mouseDragged(int x, int y);
    
    protected abstract void onExit();
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseReleased(e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseDragged(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            onExit();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    private final class DrawingThread extends Thread {

        private boolean keepRunning = true;

        @Override
        public void run() {
            while (keepRunning) {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ex) {}
                repaint();
            }
        }
    }
}
