package message;

import socket.message.IResponse;

public class ResponseError implements IResponse{
    private String message;

    public ResponseError(String message) {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
