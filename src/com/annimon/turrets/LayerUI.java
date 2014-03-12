package com.annimon.turrets;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 *
 * @author aNNiMON
 */
public class LayerUI {

    public static class GradientBackground extends javax.swing.plaf.LayerUI<JComponent> {

        @Override
        public void paint(Graphics g, JComponent component) {
            super.paint(g, component);
            final int w = component.getWidth();
            final int h = component.getHeight();
            
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
            g2d.setPaint(new GradientPaint(0, 0, Color.BLACK, 0, h, Color.GREEN));
            g2d.fillRect(0, 0, w, h);
            g2d.dispose();
        }
    }
    
    public static class PlanetBackground extends javax.swing.plaf.LayerUI<JComponent>
                                         implements Constants {
        
        private final BufferedImage image;
        
        public PlanetBackground() {
            image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            new Background().draw(g);
            g.dispose();
        }

        @Override
        public void paint(Graphics g, JComponent component) {
            super.paint(g, component);
            
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
        }
    }
}
