package server;

import java.net.Socket;
import socket.ClientAbstract;

public class MainClient extends ClientAbstract{
    private MainServer mainServer;

    public MainClient(MainServer mainServer, Socket socket) {
        super(socket);
        this.mainServer = mainServer;
    }

    public MainServer getMainServer()
    {
        return mainServer;
    }

    public void setUp() {
        setEventHandler(new MainClientEventHandler(this));
    }

}
