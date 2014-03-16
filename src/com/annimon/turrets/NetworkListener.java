package com.annimon.turrets;

/**
 *
 * @author aNNiMON
 */
public interface NetworkListener {
    
    public static final int
            ON_CONNECT = 1,
            ON_SEED_RECEIVED = 2;

    public void onStatusChanged(int status, Object data);
}
