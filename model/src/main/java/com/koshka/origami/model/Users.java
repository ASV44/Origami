package com.koshka.origami.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hackintosh on 3/14/17.
 */

public class Users {
    private List<User> users;

    public Users() { users = new ArrayList<>(); }

    public List<User> getUsersDB() { return users; }
}
