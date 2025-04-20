package org.example.discover;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class NonDatabase {
    static AtomicLong userId = new AtomicLong(1);
    static HashMap<String, User> userTable = new HashMap<>();

    static public void createUser(String username, String email, String password) {
        User u = new User(
                username, email, password
        );
        userTable.put(email, u);
    }

    static public List<User> getUsers() {
        return (List<User>) userTable.values();
    }

    static public User getUserByEmail(String email) {
        return userTable.get(email);
    }

    static public User getUserByUsername(String username) {
        for (Map.Entry<String, User> entry : userTable.entrySet()) {
            User u = entry.getValue();
            if (Objects.equals(u.getUsername(), username)) {
                return u;
            }
        }
        return null;
    }

    static public boolean checkIfUserPresent(String email) {
        return userTable.containsKey(email);
    }
}
