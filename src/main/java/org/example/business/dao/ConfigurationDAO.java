package org.example.business.dao;

import org.example.business.dto.ConfigurationDTO;
import org.example.business.dto.enumeration.ConfigurationName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationDAO extends DAOPattern<ConfigurationDTO, ConfigurationName> {
  private static final String GET_QUERY = "SELECT * FROM Configuration WHERE name = ?";
  private static final String GET_ALL_QUERY = "SELECT * FROM Configuration";
  private static final String UPDATE_QUERY = "UPDATE Configuration SET is_enabled = ? WHERE name = ?";

  @Override
  ConfigurationDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new ConfigurationDTO(
      ConfigurationName.valueOf(resultSet.getString("name")),
      resultSet.getBoolean("is_enabled")
    );
  }

  @Deprecated
  @Override
  public void createOne(ConfigurationDTO configurationDTO) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Configuration is READ and UPDATE only.");
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
  public ConfigurationDTO getOne(ConfigurationName name) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, name.toString());

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
  public void updateOne(ConfigurationDTO configurationDTO) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setBoolean(1, configurationDTO.isEnabled());
      statement.setString(2, configurationDTO.name().toString());
      statement.executeUpdate();
    }
  }

  @Deprecated
  @Override
  public void deleteOne(ConfigurationName name) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Configuration is READ and UPDATE only.");
  }
}
