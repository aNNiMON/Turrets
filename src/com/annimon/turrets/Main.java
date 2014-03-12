package com.annimon.turrets;

import java.awt.Component;
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
        setResizable(Constants.RESIZABLE);
        setLocationByPlatform(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        add(new JLayer<>(new MenuPanel(), new LayerUI.GradientBackground()));
        pack();
    }
    
    public void switchLayout(Component component) {
        getContentPane().removeAll();
        getContentPane().add(component);
        pack();
    }
}
