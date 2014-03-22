package com.annimon.turrets;

import java.awt.Dimension;

/**
 *
 * @author aNNiMON
 */
public interface Constants {

    // Screen
    public static final int WIDTH = 800, HEIGHT = 600;
    public static final boolean RESIZABLE = false;
    public static final Dimension SCREEN_DIMENSION = new Dimension(Constants.WIDTH, Constants.HEIGHT);
    public static final String FONT_NAME = "Times New Roman";
    
    // Parameters
    public static final boolean DEBUG_MODE = true;
    public static final int PORT = 7117;
    
    // Game
    public static final double GRAVITATION_ACCELERATION = 9.81;
    public static final int PLAYERS_BLOCK_COUNT = 24;
    public static final int MAX_ROUNDS = 5;
    public static final double MAX_WIND_STRENGTH = 2.0; 
}
