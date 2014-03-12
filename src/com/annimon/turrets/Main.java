package com.annimon.turrets;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author aNNiMON
 */
public class Main extends JFrame {

    public static void main(String[] args) {
        new Main().setVisible(true);
    }

    public Main() {
        super("Turrets");
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);
        setLocationByPlatform(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        add(new MenuPanel());
        pack();
    }
    
}
