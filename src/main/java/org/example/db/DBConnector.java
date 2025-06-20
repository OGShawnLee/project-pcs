package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {

  private static final Properties properties = new Properties();

  static {
    try (InputStream input = DBConnector.class.getClassLoader().getResourceAsStream("db.properties")) {
      if (input == null) {
        throw new IOException("No se encontró el archivo de propiedades de la base de datos");
      }
      properties.load(input);
    } catch (IOException e) {
      System.err.println("Error al cargar configuración de la base de datos: " + e.getMessage());
    }
  }

  public static Connection getConnection() throws SQLException {
    String url = properties.getProperty("db.url");
    String user = properties.getProperty("db.user");
    String password = properties.getProperty("db.password");

    return DriverManager.getConnection(url, user, password);
  }
}

