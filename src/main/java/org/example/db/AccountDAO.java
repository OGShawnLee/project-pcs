package org.example.db;

import org.example.business.AccountDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends DBConnector implements DAO<AccountDTO> {
  private static final String CREATE_QUERY = "INSERT INTO Account (email, password) VALUES (?, ?)";
  private static final String GET_QUERY = "SELECT * FROM Account WHERE email = ?";
  private static final String GET_ALL_QUERY = "SELECT * FROM Account";
  private static final String UPDATE_QUERY = "UPDATE Account SET password = ? WHERE email = ?";
  private static final String DELETE_QUERY = "DELETE FROM Account WHERE email = ?";

  @Override
  public void create(AccountDTO element) throws SQLException {
    Connection conn = getConnection();
    PreparedStatement statement = conn.prepareStatement(CREATE_QUERY);

    try (statement) {
      statement.setString(1, element.email());
      statement.setString(2, element.password());
      statement.executeUpdate();
    }

    close();
  }

  @Override
  public List<AccountDTO> getAll() throws SQLException {
    Connection conn = getConnection();
    PreparedStatement statement = conn.prepareStatement(GET_ALL_QUERY);

    try (ResultSet resultSet = statement.executeQuery()) {
      List<AccountDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(
                new AccountDTO(
                        resultSet.getString("email"),
                        resultSet.getString("password")
                )
        );
      }

      close();

      return list;
    }
  }

  @Override
  public AccountDTO get(int id) throws SQLException {
    return null;
  }

  @Override
  public AccountDTO get(String email) throws SQLException {
    Connection conn = getConnection();
    PreparedStatement statement = conn.prepareStatement(GET_QUERY);
    AccountDTO element = null;

    try (statement) {
      statement.setString(1, email);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        element = new AccountDTO(
                resultSet.getString("email"),
                resultSet.getString("password")
        );
      }
    }

    return element;
  }

  @Override
  public void update(AccountDTO element) throws SQLException {
    Connection conn = getConnection();
    PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY);

    try (statement) {
      statement.setString(1, element.email());
      statement.setString(2, element.password());
      statement.executeUpdate();
    }

    close();
  }

  @Override
  public void delete(int id) throws SQLException {
    Connection conn = getConnection();
    PreparedStatement statement = conn.prepareStatement(DELETE_QUERY);

    try (statement) {
      statement.setInt(1, id);
      statement.executeUpdate();
    }

    close();
  }
}
