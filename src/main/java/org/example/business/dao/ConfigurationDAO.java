package org.example.business.dao;

import org.example.business.dao.shape.GetAllDAOShape;
import org.example.business.dao.shape.GetOneDAOShape;
import org.example.business.dao.shape.UpdateOneDAOShape;
import org.example.business.dto.ConfigurationDTO;
import org.example.business.dto.enumeration.ConfigurationName;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationDAO
  implements GetOneDAOShape<ConfigurationDTO, ConfigurationName>, GetAllDAOShape<ConfigurationDTO>, UpdateOneDAOShape<ConfigurationDTO>
{
  private static final String GET_QUERY = "SELECT * FROM Configuration WHERE name = ?";
  private static final String GET_ALL_QUERY = "SELECT * FROM Configuration";
  private static final String UPDATE_QUERY = "UPDATE Configuration SET is_enabled = ? WHERE name = ?";

  @Override
  public ConfigurationDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new ConfigurationDTO(
      ConfigurationName.valueOf(resultSet.getString("name")),
      resultSet.getBoolean("is_enabled")
    );
  }

  @Override
  public List<ConfigurationDTO> getAll() throws SQLException {
    try (
      Connection connection = DBConnector.getConnection();
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
      Connection connection = DBConnector.getConnection();
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
      Connection connection = DBConnector.getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setBoolean(1, configurationDTO.isEnabled());
      statement.setString(2, configurationDTO.name().toString());
      statement.executeUpdate();
    }
  }
}
