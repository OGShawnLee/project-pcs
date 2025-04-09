package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
  private Connection connection;

  protected Connection getConnection() throws SQLException {
    if (connection != null) {
      return connection;
    }

    connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/practice",
            "practice_admin",
            "ADMIN"
    );

    return connection;
  }

  protected void close() throws SQLException {
    if (connection != null) {
      connection.close();
    }

    connection = null;
  }
}
