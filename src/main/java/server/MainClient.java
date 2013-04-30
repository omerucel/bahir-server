package server;

import java.net.Socket;
import java.util.UUID;
import server.model.User;
import socket.ClientAbstract;

public class MainClient extends ClientAbstract{
    private MainServer mainServer;
    private String clientId;
    private User user;

    public MainClient(MainServer mainServer, Socket socket) {
        super(socket);
        this.mainServer = mainServer;
        this.clientId = UUID.randomUUID().toString();
    }

    public String getClientId() {
        return clientId;
    }

    public MainServer getMainServer()
    {
        return mainServer;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void setUp() {
        setEventHandler(new MainClientEventHandler(this));
    }

}
