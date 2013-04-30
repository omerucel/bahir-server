package client;

import desktop.DialogLoading;
import desktop.WindowApp;
import desktop.WindowConnect;
import desktop.WindowLogin;
import desktop.WindowRegister;
import javax.swing.JOptionPane;
import message.ResponseConnection;
import message.ResponseError;
import message.ResponseLogin;
import socket.IClientEventHandler;

public class MainServerConnectionEventHandler implements IClientEventHandler{
    private MainServerConnection mainServerConnection;

    public MainServerConnectionEventHandler(MainServerConnection mainServerConnection) {
        this.mainServerConnection = mainServerConnection;
    }

    public MainServerConnection getMainServerConnection()
    {
        return mainServerConnection;
    }

    public void handleConnected() {
    }

    public void handleConnectionFailed(Exception ex) {
        WindowApp.getInstance().setVisible(false);
        WindowLogin.getInstance().setVisible(false);
        WindowRegister.getInstance().setVisible(false);

        WindowConnect.getInstance().setVisible(true);
        JOptionPane.showMessageDialog(WindowConnect.getInstance(), 
                "Sunucuya bağlanmaya çalışılırken bir sorun oluştu.");
        ex.printStackTrace();
    }

    public void handleDisconnected() {
    }

    public void handle(Object message) {
        System.out.println(this.getClass().getName() + "::handle -> " + message.getClass().getName());

        if (message instanceof Exception)
        {
            Exception ex = (Exception)message;
            ex.printStackTrace();
        }else if (message instanceof ResponseConnection){
            DialogLoading.getInstance().setVisible(false);
            WindowConnect.getInstance().setVisible(false);

            WindowLogin.getInstance().setVisible(true);
        }else if(message instanceof ResponseError){
            DialogLoading.getInstance().setVisible(false);
            JOptionPane.showMessageDialog(null, ((ResponseError)message).getMessage());
        }else if(message instanceof ResponseLogin){
            DialogLoading.getInstance().setVisible(false);

            ResponseLogin response = (ResponseLogin)message;
            Container.setResponseLogin(response);

            WindowApp.getInstance().setVisible(true);
            WindowRegister.getInstance().setVisible(false);
            WindowLogin.getInstance().setVisible(false);
        }
    }

}
