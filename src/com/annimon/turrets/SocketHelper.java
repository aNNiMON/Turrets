package com.annimon.turrets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author aNNiMON
 */
public class SocketHelper extends Thread {

    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    
    private final NetworkListener listener;
    
    public SocketHelper(Socket socket, NetworkListener listener) throws IOException {
        this.socket = socket;
        this.listener = listener;
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }
    
    @Override
    public void run() {
        listener.onStatusChanged(NetworkListener.ON_CONNECT, null);
        while (true) {
            try {
                final int status = dis.readInt();
                switch (status) {
                    case NetworkListener.ON_SEED_RECEIVED:
                        listener.onStatusChanged(status, receiveSeed());
                        break;
                }
                
            } catch (IOException ex) { }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) { }
        }
    }
    
    public void sendSeed(long seed) {
        try {
            dos.writeInt(NetworkListener.ON_SEED_RECEIVED);
            dos.writeLong(seed);
        } catch (IOException ex) {}
    }
    
    private long receiveSeed() throws IOException {
        return dis.readLong();
    }
    
    public void close() throws IOException {
        if (dis != null) dis.close();
        if (dos != null) dos.close();
        if (socket != null) socket.close();
    }
}
