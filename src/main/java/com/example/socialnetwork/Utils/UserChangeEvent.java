package com.example.socialnetwork.Utils;

import com.example.socialnetwork.Domain.User;

public class UserChangeEvent implements Event{
    private ChangeEventType eventType;
    private User data, oldData;
    public UserChangeEvent(ChangeEventType type, User data){
        this.eventType = type;
        this.data = data;
    }

    public UserChangeEvent(ChangeEventType type, User data, User oldData){
        this.eventType = type;
        this.data = data;
        this.oldData = oldData;
    }

    public User getOldData() {
        return oldData;
    }

    public User getData() {
        return data;
    }

    public ChangeEventType getEventType() {
        return eventType;
    }
}
