package org.example.discover;

import java.io.Serializable;
import java.util.List;


public class User implements Serializable {
//    private Long id;
    private String username;
    private String password;
    private String email;

    public User(String username, String email, String password) {
//        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

//    public Long getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public String getEmail() { return this.email; }
}
