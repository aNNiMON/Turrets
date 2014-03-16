package com.annimon.turrets;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author aNNiMON
 */
public class GameServer implements Constants {

    private final ServerSocket serverSocket;
    private final SocketHelper helper;

    public GameServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        helper = new SocketHelper(serverSocket.accept());
    }
    
    public SocketHelper getHelper() {
        return helper;
    }
}
