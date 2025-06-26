package org.example.business.dao;

import org.example.business.dto.AccountDTO;

import org.example.business.dto.enumeration.AccountRole;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends DAOPattern<AccountDTO, String> {
  private static final String CREATE_QUERY = "INSERT INTO Account (email, password, role) VALUES (?, ?, ?)";
  private static final String GET_QUERY = "SELECT * FROM Account WHERE email = ?";
  private static final String GET_ALL_QUERY = "SELECT * FROM Account";
  private static final String UPDATE_QUERY = "UPDATE Account SET password = ?, role = ? WHERE email = ?";
  private static final String DELETE_QUERY = "DELETE FROM Account WHERE email = ?";
  private static final String CHECK_COORDINATOR_ACCOUNT = "SELECT COUNT(*) FROM Account WHERE role = 'COORDINATOR'";

  @Override
  AccountDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new AccountDTO(
      resultSet.getString("email"),
      resultSet.getString("password"),
      AccountRole.valueOf(resultSet.getString("role")),
      resultSet.getBoolean("has_access")
    );
  }

  @Override
  public void createOne(AccountDTO accountDTO) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, accountDTO.email());
      statement.setString(2, accountDTO.password());
      statement.setString(3, accountDTO.role().toString());
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
  public AccountDTO getOne(String email) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, email);

      AccountDTO accountDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          accountDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return accountDTO;
    }
  }

  @Override
  public void updateOne(AccountDTO accountDTO) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, BCrypt.hashpw(accountDTO.password(), BCrypt.gensalt()));
      statement.setString(2, accountDTO.role().toString());
      statement.setString(3, accountDTO.email());
      statement.executeUpdate();
    }
  }

  @Override
  public void deleteOne(String email) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, email);
      statement.executeUpdate();
    }
  }

  public boolean hasCoordinatorAccount() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CHECK_COORDINATOR_ACCOUNT);
      ResultSet resultSet = statement.executeQuery()
    ) {
      if (resultSet.next()) {
        return resultSet.getInt(1) > 0;
      }
    }

    return false;
  }
}