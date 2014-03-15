package com.annimon.turrets;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author aNNiMON
 */
public class GameCanvas extends DoubleBufferedCanvas {

    private final BufferedImage background;
    private final Terrain terrain;
    
    public GameCanvas() {
        background = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);
        initBackground();
        
        terrain = new Terrain(Constants.WIDTH);
        terrain.generate(System.currentTimeMillis());
    }
    
    @Override
    protected void draw(Graphics2D g) {
        g.drawImage(background, 0, 0, null);
        terrain.draw(g);
    }

    @Override
    protected void update() {
    }

    private void initBackground() {
        final Graphics g = background.createGraphics();
        new Background().draw(g);
        g.dispose();
    }
}
