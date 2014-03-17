package com.annimon.turrets.network;

import com.annimon.turrets.Constants;
import com.annimon.turrets.SocketHelper;
import com.annimon.turrets.network.NetworkListener;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author aNNiMON
 */
public class GameClient implements Constants {

    private final SocketHelper helper;

    public GameClient(String host, NetworkListener listener) throws IOException {
        helper = new SocketHelper(new Socket(host, PORT), listener);
    }

    public SocketHelper getHelper() {
        return helper;
    }
}
