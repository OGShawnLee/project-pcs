package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
      "jdbc:mysql://localhost:3306/practice",
      "practice_admin",
      "ADMIN"
    );
  }
}
