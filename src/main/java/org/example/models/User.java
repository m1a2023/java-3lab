package org.example.models;

import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Table(name="users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @Column(name="Username", nullable = false)
    private String username;

    @Column(name="Password", nullable = false)
    private String password;

    @Column(name="Email", unique = true, nullable = false)
    private String email;

    protected User() { }

    public User(String username, String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Long getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public String getEmail() { return this.email; }
}
