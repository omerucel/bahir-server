package server;

import message.RequestLogin;
import message.RequestRegister;
import message.ResponseConnection;
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
        System.out.println(this.getClass().getName() + "::handleConnected");

        getMainClient()
                .writeObject(new ResponseConnection());
    }

    public void handleConnectionFailed(Exception ex) {
        System.out.println(this.getClass().getName() + "::handleConnectionFailed");
        ex.printStackTrace();
    }

    public void handleDisconnected() {
        System.out.println(this.getClass().getName() + "::handleDisconnected");
        getMainClient().getMainServer().logout(mainClient);
    }

    public void handle(Object message) {
        System.out.println(this.getClass().getName() + "::handle -> " + message.getClass().getName());

        if (message instanceof Exception)
        {
            Exception ex = (Exception)message;
            ex.printStackTrace();
        }else if (message instanceof RequestLogin){
            RequestLogin request = (RequestLogin)message;

            getMainClient().getMainServer()
                    .login(getMainClient(), request);
        }else if(message instanceof RequestRegister){
            RequestRegister request = (RequestRegister)message;
            getMainClient().getMainServer()
                    .register(getMainClient(), request);
        }
    }

}
