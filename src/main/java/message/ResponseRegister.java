package message;

import socket.message.IResponse;

public class ResponseRegister implements IResponse{
    private String token;

    public ResponseRegister(String token) {
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }
}
