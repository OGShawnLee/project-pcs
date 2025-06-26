package db;

import org.example.db.DBConnector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectorTest {
  @Test
  void testGetConnection() {
    Assertions.assertDoesNotThrow(
      () -> {
        try (Connection connection = DBConnector.getConnection()) {
          Assertions.assertNotNull(connection);
          Assertions.assertFalse(connection.isClosed());
        }
      }
    );
  }

  @Test
  void testClose() {
    Assertions.assertDoesNotThrow(DBConnector::close);
    Assertions.assertThrows(
      SQLException.class,
      DBConnector::getConnection
    );
  }
}
