package message;

import socket.message.IRequest;

public class RequestLogin implements IRequest{
    private String username;
    private String password;

    public RequestLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
