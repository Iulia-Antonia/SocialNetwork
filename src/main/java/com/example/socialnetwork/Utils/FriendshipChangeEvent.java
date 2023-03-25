package com.example.socialnetwork.Utils;

import com.example.socialnetwork.Domain.Friendship;

public class FriendshipChangeEvent implements Event {
    private ChangeEventType eventType;
    private Friendship data, oldData;
    public FriendshipChangeEvent(ChangeEventType eventType, Friendship data){
        this.data=data;
        this.eventType=eventType;
    }

    public Friendship getOldData() {
        return oldData;
    }

    public Friendship getData() {
        return data;
    }

    public ChangeEventType getEventType() {
        return eventType;
    }
}
