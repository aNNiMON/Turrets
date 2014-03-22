package com.annimon.turrets;

import com.annimon.turrets.util.GuiUtil;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JLayer;

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
    
    private final Component mainMenu;

    public Main() {
        super("Turrets");
        setResizable(Constants.RESIZABLE);
        setLocationByPlatform(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        mainMenu = new JLayer<>(new MenuPanel(), new GuiUtil.PlanetBackground());
        add(mainMenu);
        pack();
    }
    
    public Component getMainMenu() {
        return mainMenu;
    }
    
    public void switchLayout(Component component) {
        getContentPane().removeAll();
        getContentPane().add(component);
        component.setFocusable(true);
        component.requestFocus();
        pack();
    }
}
