package com.annimon.turrets.network;

import static com.annimon.turrets.Constants.PORT;
import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author aNNiMON
 */
public class GameServer {

    private final ServerSocket serverSocket;
    private final SocketHelper helper;

    public GameServer(NetworkListener listener) throws IOException {
        serverSocket = new ServerSocket(PORT);
        helper = new SocketHelper(serverSocket.accept(), listener);
    }
    
    public SocketHelper getHelper() {
        return helper;
    }
}
