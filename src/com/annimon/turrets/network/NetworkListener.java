package com.annimon.turrets.network;

/**
 *
 * @author aNNiMON
 */
public interface NetworkListener {
    
    public static final int
            ON_CONNECT = 1,
            ON_DISCONNECT = 2,
            ON_SEED_RECEIVED = 3,
            ON_MOVE_RECEIVED = 4,
            ON_NEW_ROUND = 5;

    public void onStatusChanged(int status, Object data);
}
