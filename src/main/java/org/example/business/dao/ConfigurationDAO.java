package org.example.business.dao;

import org.example.business.dto.ConfigurationDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationDAO extends DAOPattern<ConfigurationDTO, String> {
  private static final String CREATE_QUERY = "INSERT INTO Configuration (name, value) VALUES (?, ?)";
  private static final String GET_QUERY = "SELECT * FROM Configuration WHERE name = ?";
  private static final String GET_ALL_QUERY = "SELECT * FROM Configuration";
  private static final String UPDATE_QUERY = "UPDATE Configuration SET value = ? WHERE name = ?";
  private static final String DELETE_QUERY = "DELETE FROM Configuration WHERE name = ?";

  @Override
  ConfigurationDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new ConfigurationDTO(resultSet.getString("name"), resultSet.getString("value"));
  }

  @Override
  public void createOne(ConfigurationDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.name());
      statement.setString(2, dataObject.value());
      statement.executeUpdate();
    }
  }

  @Override
  public List<ConfigurationDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<ConfigurationDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public ConfigurationDTO getOne(String name) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, name);

      ConfigurationDTO configurationDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          configurationDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return configurationDTO;
    }
  }

  @Override
  public void updateOne(ConfigurationDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.value());
      statement.setString(2, dataObject.name());
      statement.executeUpdate();
    }
  }

  @Override
  public void deleteOne(String name) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, name);
      statement.executeUpdate();
    }
  }
}
