package client;

import socket.ClientAbstract;

public class MainServerConnection extends ClientAbstract{

    public MainServerConnection(String host, int port) {
        super(host, port);
    }

    public void setUp()
    {
        setEventHandler(new MainServerConnectionEventHandler(this));
    }
}
