package com.annimon.turrets;

import static com.annimon.turrets.Constants.RESIZABLE;
import com.annimon.turrets.util.GuiUtil;
import com.annimon.turrets.util.Prefs;
import java.awt.Component;
import javax.swing.JFrame;

/**
 *
 * @author aNNiMON
 */
public final class Main extends JFrame {
    
    private static Main instance;

    public static void main(String[] args) {
        getInstance().setVisible(true);
    }
    
    public static synchronized Main getInstance() {
        if (instance == null) instance = new Main();
        return instance;
    }
    
    private final Component mainMenu;

    public Main() {
        super("Turrets");
        setResizable(RESIZABLE);
        setLocationByPlatform(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        mainMenu = GuiUtil.createPlanetLayer(new MenuPanel());
        add(mainMenu);
        pack();
        
        // Set sound
        Sound.setEnabled(Prefs.getInstance().soundEnabled());
    }
    
    public void switchToMainMenu() {
        switchLayout(mainMenu);
    }
    
    public void switchLayout(Component component) {
        getContentPane().removeAll();
        getContentPane().add(component);
        component.setFocusable(true);
        component.requestFocus();
        pack();
        repaint();
    }
}
