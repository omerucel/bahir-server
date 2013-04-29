package message;

import socket.message.IRequest;

public class RequestRegister implements IRequest{
    private String email;
    private String password;

    public RequestRegister(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public String password()
    {
        return password;
    }
}
