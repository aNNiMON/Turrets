package com.annimon.turrets;

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
