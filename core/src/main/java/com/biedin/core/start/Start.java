package com.biedin.core.start;

import com.biedin.core.database.SQLiteConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Start {
    public static void main(String[] args) {
      try (Statement statement = SQLiteConnection.getConnection().createStatement();
           ResultSet resultSet = statement.executeQuery("SELECT * FROM storage;")) {
          while (resultSet.next()) {
              System.out.print(resultSet.getString("name").concat(" "));
              System.out.print(resultSet.getString("id").concat(" "));
              System.out.println();
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
    }
}
