package org.example.service;

import org.example.db.DatabaseService;
import org.example.db.UserTable;
import org.example.models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final Connection connection;

    public UserService() {
        DatabaseService databaseService = new DatabaseService();
        this.connection = databaseService.getConnection();
    }

    public List<User> getUsers() {
        try {
            return UserTable.getUsers(this.connection);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get users");
        }
    }

    public int insertUser(User u) {
        try {
            return UserTable.insertUser(this.connection, u);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot insert user");
        }
    }

    public boolean exists(User u) {
        try {
            { // Debug
                System.out.printf("\nConnection is %b", this.connection.isClosed());
            }
            return UserTable.exists(this.connection, u);
        } catch (SQLException e) {
            throw new RuntimeException("Error while checking user existance");
        }
    }

    public boolean exists(String username, String password) {
        try {
            { // Debug
                System.out.printf("Connection is %b", this.connection.isClosed());
            }
            return UserTable.exists(this.connection, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error while checking user existance");
        }
    }

    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while closing connection");
        }
    }
}

