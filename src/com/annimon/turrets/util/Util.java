package com.annimon.turrets.util;

import com.annimon.turrets.RandomExt;

/**
 *
 * @author aNNiMON
 */
public class Util {
    
    private static final RandomExt random = new RandomExt();
    
    public static void setRandomSeed(long seed) {
        random.setSeed(seed);
    }
    
    public static int rand(int to) {
	return random.rand(to);
    }
    
    public static int rand(int from, int to) {
	return random.rand(from, to);
    }
    
    public static double rand(double from, double to) {
	return random.rand(from, to);
    }

    public static int randomColor(int from, int to) {
	return random.randomColor(from, to);
    }
}
