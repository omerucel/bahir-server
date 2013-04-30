package message;

import socket.message.IRequest;

public class RequestSendFriendList implements IRequest{
    private String token;

    public RequestSendFriendList(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
