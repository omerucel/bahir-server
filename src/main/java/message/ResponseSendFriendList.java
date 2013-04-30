package message;

import java.util.ArrayList;
import socket.message.IResponse;

public class ResponseSendFriendList implements IResponse{
    private ArrayList<String> friendList;

    public ResponseSendFriendList(ArrayList<String> friendList) {
        this.friendList = friendList;
    }

    public ArrayList<String> getFriendList() {
        return friendList;
    }
}
