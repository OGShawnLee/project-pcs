package org.example.db;

import org.example.business.AccountDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends DAO<AccountDTO, String> {
  private static final String CREATE_QUERY = "INSERT INTO Account (email, password) VALUES (?, ?)";
  private static final String GET_QUERY = "SELECT * FROM Account WHERE email = ?";
  private static final String GET_ALL_QUERY = "SELECT * FROM Account";
  private static final String UPDATE_QUERY = "UPDATE Account SET password = ? WHERE email = ?";
  private static final String DELETE_QUERY = "DELETE FROM Account WHERE email = ?";

  @Override
  protected AccountDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new AccountDTO(resultSet.getString("email"), resultSet.getString("password"));
  }

  @Override
  public void create(AccountDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.email());
      statement.setString(2, dataObject.password());
      statement.executeUpdate();
    }
  }

  @Override
  public List<AccountDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<AccountDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public AccountDTO get(String email) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, email);

      AccountDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    }
  }

  @Override
  public void update(AccountDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.password());
      statement.setString(2, dataObject.email());
      statement.executeUpdate();
    }
  }

  @Override
  public void delete(String email) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, email);
      statement.executeUpdate();
    }
  }
}