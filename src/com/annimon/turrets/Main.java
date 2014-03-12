package com.annimon.turrets;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.WindowConstants;

/**
 *
 * @author aNNiMON
 */
public class Main extends JFrame {
    
    private static Main instance;

    public static void main(String[] args) {
        Main.getInstance().setVisible(true);
    }
    
    public static synchronized Main getInstance() {
        if (instance == null) instance = new Main();
        return instance;
    }

    public Main() {
        super("Turrets");
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setResizable(Constants.RESIZABLE);
        setLocationByPlatform(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        add(new JLayer<>(new MenuPanel(), new LayerUI.GradientBackground()));
        pack();
    }
}
