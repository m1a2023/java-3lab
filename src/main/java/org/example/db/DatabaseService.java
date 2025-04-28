package org.example.db;

import org.example.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
    /* Database base settings */
    private final String DATABASE = "ServletApplication";
    private final String HOST = "localhost:3306";
    private final String USER = "root";
    private final String PASSWORD = "admin";

    /* Database driver settings */
    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String JDBC_CONNECTION =
            String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

    /* Get the connection to db */
    public Connection getConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(JDBC_CONNECTION, USER, PASSWORD);
            if (con.isClosed()) {
                System.out.println("\nConnection closed\n");
            } else {
                System.out.println("\nConnection opened\n");
            }
            return con;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.printf("Error: %s%n", e);
        }
        return null;
    }

    public DatabaseService() { }

    public void createTables() {
        try (Connection con = getConnection()) {
            if (con != null) {
                UserTable.createTable(con);
            } else {
                System.out.println("\nConnection is null.\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create tables");
        }
    }
}
