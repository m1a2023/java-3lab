package org.example;

import org.example.db.DatabaseService;

public class Main {
    /* Initializing database */
    public static void main(String[] args) {
        DatabaseService ds = new DatabaseService();
        if (ds.getConnection() == null) {
            System.out.println("\nConnection is null in main");
        }
        ds.createTables();
    }
}
