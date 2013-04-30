package message;

import socket.message.IRequest;

public class RequestAddFriend implements IRequest{
    private String token;
    private String username;

    public RequestAddFriend(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
