package com.mkoi.over9000.message.response;

import com.mkoi.over9000.model.Friend;

import java.util.List;

/**
 * @author Wojciech Rauner
 */
public class FriendResponse {
    private List<Friend> friends;

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }
}
