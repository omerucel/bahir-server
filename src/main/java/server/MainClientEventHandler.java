package server;

import socket.IClientEventHandler;

public class MainClientEventHandler implements IClientEventHandler{
    private MainClient mainClient;

    public MainClientEventHandler(MainClient mainClient) {
        this.mainClient = mainClient;
    }

    public MainClient getMainClient()
    {
        return mainClient;
    }

    public void handleConnected() {
    }

    public void handleConnectionFailed(Exception ex) {
    }

    public void handleDisconnected() {
    }

    public void handle(Object message) {
    }

}
