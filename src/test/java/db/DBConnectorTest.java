package db;

import org.example.db.DBConnector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class DBConnectorTest {
  @Test
  void testGetConnection() {
    Assertions.assertDoesNotThrow(
      () -> {
        try (Connection connection = DBConnector.getInstance().getConnection()) {
          Assertions.assertNotNull(connection);
          Assertions.assertFalse(connection.isClosed());
        }
      }
    );
  }
}
