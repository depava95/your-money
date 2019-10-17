package com.biedin.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteConnection {

    private static Connection con;

    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlite:C:\\Users\\depav\\Desktop\\full db\\money.db";
            if (con == null) {
                con = DriverManager.getConnection(url);
            }
            con.createStatement().execute("PRAGMA foreign_keys = ON");
            con.createStatement().execute("PRAGMA encoding = \"UTF-8\"");
            return con;
        } catch (SQLException e) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
