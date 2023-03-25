package com.example.socialnetwork.Utils.Observer;

import com.example.socialnetwork.Utils.Event;

import java.sql.SQLException;

public interface Observer<E extends Event> {
    void update(E e) throws SQLException;
}
