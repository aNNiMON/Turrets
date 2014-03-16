package com.annimon.turrets;

import java.util.Random;

/**
 * Extended random
 * @author aNNiMON
 */
public class RandomExt extends Random {
    
    public RandomExt() {
        super();
    }
    
    public RandomExt(long seed) {
        super(seed);
    }

    public int rand(int to) {
	return nextInt(to);
    }
    
    public int rand(int from, int to) {
	return nextInt(to - from) + from;
    }
    
    public double rand(double from, double to) {
	return nextDouble() * (to - from) + from;
    }

    public int randomColor(int from, int to) {
	if (from < 0) {
	    from = 0;
	} else if (from > 255) {
	    from = 255;
	}
	if (to < 0) {
	    to = 0;
	} else if (to > 255) {
	    to = 255;
	}
	if (from == to) {
	    return (0xFF000000 | (from << 16) | (from << 8) | from);
	} else if (from > to) {
	    final int temp = to;
	    to = from;
	    from = temp;
	}

	final int red = rand(from, to);
	final int green = rand(from, to);
	final int blue = rand(from, to);

	return (0xFF000000 | (red << 16) | (green << 8) | blue);
    }
}