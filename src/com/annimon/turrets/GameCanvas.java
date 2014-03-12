package com.annimon.turrets;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author aNNiMON
 */
public class GameCanvas extends Canvas {
    
    private BufferedImage background;
    
    public GameCanvas() {
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setPreferredSize(Constants.SCREEN_DIMENSION);
        
        initBackground();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);
    }

    private void initBackground() {
        background = new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = background.createGraphics();
        new Background().draw(g);
        g.dispose();
    }
}
