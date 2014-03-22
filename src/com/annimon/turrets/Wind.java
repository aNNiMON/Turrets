package com.annimon.turrets;

import com.annimon.turrets.util.Util;

/**
 *
 * @author aNNiMON
 */
public class Wind implements Constants {
    
    private double speed;

    public double getSpeed() {
        return speed;
    }
    
    public void change() {
        speed = Util.rand(-MAX_WIND_STRENGTH, MAX_WIND_STRENGTH);
    }
    
}
