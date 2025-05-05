package org.example.db;

import org.example.models.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Deprecated
public class UserTable {
    public static boolean createTable(Connection con)
        throws SQLException {
        String q = "CREATE TABLE IF NOT EXISTS Users (" +
                "Id int NOT NULL AUTO_INCREMENT," +
                "Email varchar(128) NOT NULL," +
                "Username varchar(128) NOT NULL," +
                "Password varchar(128) NOT NULL," +
                "UNIQUE (Id, Email)," +
                "PRIMARY KEY (Id)" +
                ");";
        try (Statement st = con.createStatement()) {
            return st.execute(q);
        }
    }

    public static List<User> getUsers(Connection con)
        throws SQLException {
        String q = "SELECT * FROM Users;";
        List<User> list = new LinkedList<>();

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(q);
            while (rs.next()) {
                Long id = rs.getLong("Id");
                String email = rs.getString("Email");
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                User u = new User(id, email, username, password);
                list.add(u);
            }
        }
        return list;
    }

    public static int insertUser(Connection con, User u)
            throws SQLException {
        String q = "INSERT INTO Users (Email, Username, Password) " +
                "VALUES (?, ?, ?);";
        try (PreparedStatement st = con.prepareStatement(q)) {
            st.setString(1, u.getEmail());
            st.setString(2, u.getUsername());
            st.setString(3, u.getPassword());

            return st.executeUpdate();
        }
    }

    public static boolean exists(Connection con, User u)
        throws SQLException {
        String q = "SELECT Id FROM Users WHERE " +
                "Users.Email = ?;";
        try (PreparedStatement st = con.prepareStatement(q)) {
            st.setString(1, u.getEmail());
            try (ResultSet rs = st.executeQuery()) {
                { // Debug
                    System.out.printf("ResultSet: %s", rs.toString());
                }
                return rs.next();
            }
        }
    }

    public static boolean exists(Connection con,
                                 String username,
                                 String password)
            throws SQLException {
        String q = "SELECT Users.Username, Users.Password " +
                "FROM Users WHERE Users.Username = ? and Users.Password = ?;";
        try (PreparedStatement st = con.prepareStatement(q)) {
            st.setString(1, username);
            st.setString(2, password);
            try (ResultSet rs = st.executeQuery()) {
                { // Debug
                    System.out.printf("ResultSet: %s", rs.toString());
                }
                return rs.next();
            }
        }
    }
}
