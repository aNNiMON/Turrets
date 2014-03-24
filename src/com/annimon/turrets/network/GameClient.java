package com.annimon.turrets.network;

import static com.annimon.turrets.Constants.PORT;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author aNNiMON
 */
public final class GameClient {

    private final SocketHelper helper;

    public GameClient(String host, NetworkListener listener) throws IOException {
        helper = new SocketHelper(new Socket(host, PORT), listener);
    }

    public SocketHelper getHelper() {
        return helper;
    }
}
