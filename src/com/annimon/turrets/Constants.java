package com.annimon.turrets;

import java.awt.Dimension;
import java.awt.Font;

/**
 *
 * @author aNNiMON
 */
public final class Constants {

    // Screen
    public static final int WIDTH = 800, HEIGHT = 600;
    public static final boolean RESIZABLE = false;
    public static final Dimension SCREEN_DIMENSION = new Dimension(WIDTH, HEIGHT);
    public static final String MENU_FONT_NAME = "Stencil";
    public static final Font GAME_FONT = new Font("Times New Roman", Font.PLAIN, 24);
    
    // Parameters
    public static final boolean DEBUG_MODE = true;
    public static final int PORT = 7117;
    
    // Game
    public static final double GRAVITATION_ACCELERATION = 9.81;
    public static final int PLAYERS_BLOCK_COUNT = 24;
    public static final int MAX_ROUNDS = 5;
    public static final double MAX_WIND_STRENGTH = 2.0; 
}
