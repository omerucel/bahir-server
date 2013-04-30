package server;

import java.net.Socket;
import socket.IServerEventHandler;

public class MainServerEventHandler implements IServerEventHandler{

    private MainServer mainServer;

    public MainServerEventHandler(MainServer mainServer) {
        this.mainServer = mainServer;
    }

    public MainServer getMainServer()
    {
        return mainServer;
    }

    public void handleStartingFailed(Exception ex) {
        System.out.println(this.getClass().getName() + "::handleStartingFailed");
        ex.printStackTrace();
    }

    public void handleStarted() {
        System.out.println(this.getClass().getName() + "::handleStarted");
    }

    public void handleClientConnected(Socket socket) {
        System.out.println(this.getClass().getName() + "::handleClientConnected");
        new Thread(new MainClient(mainServer, socket)).start();
    }

    public void handleClientConnectionFailed(Exception ex) {
        System.out.println(this.getClass().getName() + "::handleClientConnectionFailed");
        ex.printStackTrace();
    }

    public void handleClosed() {
        System.out.println(this.getClass().getName() + "::handleClosed");
    }

}
