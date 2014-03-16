package com.annimon.turrets;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author aNNiMON
 */
public class GameClient implements Constants {

    private final SocketHelper helper;

    public GameClient(String host) throws IOException {
        helper = new SocketHelper(new Socket(host, PORT));
    }

    public SocketHelper getHelper() {
        return helper;
    }
}
