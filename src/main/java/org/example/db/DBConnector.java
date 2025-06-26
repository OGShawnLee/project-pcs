package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector {
  private static final HikariDataSource dataSource;

  static {
    HikariConfig config = new HikariConfig("src/main/resources/db.properties");
    dataSource = new HikariDataSource(config);
  }

  private DBConnector() {}

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  public static void close() {
    if (dataSource != null) {
      dataSource.close();
    }
  }
}
