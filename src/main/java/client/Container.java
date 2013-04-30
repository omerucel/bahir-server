package client;

import message.ResponseLogin;

public class Container {
    private static MainServerConnection mainServerConnection;
    private static ResponseLogin responseLogin;

    public static MainServerConnection getMainServerConnection()
    {
        return mainServerConnection;
    }

    public static void setMainServerConnection(MainServerConnection mainServerConnection)
    {
        Container.mainServerConnection = mainServerConnection;
    }

    public static ResponseLogin getResponseLogin() {
        return responseLogin;
    }

    public static void setResponseLogin(ResponseLogin responseLogin) {
        Container.responseLogin = responseLogin;
    }
}
