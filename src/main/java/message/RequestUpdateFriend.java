package message;

import socket.message.IRequest;

public class RequestUpdateFriend implements IRequest{
    private String token;
    private String username;
    private Boolean status;

    public RequestUpdateFriend(String token, String username, Boolean status) {
        this.token = token;
        this.username = username;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }
}
