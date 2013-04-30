package message;

import socket.message.IResponse;

public class ResponseLogin implements IResponse{
    private String token;

    public ResponseLogin(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
