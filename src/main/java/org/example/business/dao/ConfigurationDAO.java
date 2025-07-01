package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.dao.shape.DAOShape;
import org.example.business.dao.shape.GetAllDAOShape;
import org.example.business.dao.shape.GetOneDAOShape;
import org.example.business.dao.shape.UpdateOneDAOShape;
import org.example.business.dto.ConfigurationDTO;
import org.example.business.dto.enumeration.ConfigurationName;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationDAO extends DAOShape<ConfigurationDTO>
  implements GetOneDAOShape<ConfigurationDTO, ConfigurationName>, GetAllDAOShape<ConfigurationDTO>, UpdateOneDAOShape<ConfigurationDTO>
{
  private static final Logger LOGGER = LogManager.getLogger(ConfigurationDAO.class);
  private static final String GET_QUERY = "SELECT * FROM Configuration WHERE name = ?";
  private static final String GET_ALL_QUERY = "SELECT * FROM Configuration";
  private static final String UPDATE_QUERY = "UPDATE Configuration SET is_enabled = ? WHERE name = ?";

  @Override
  public ConfigurationDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new ConfigurationDTO(
      ConfigurationName.valueOf(resultSet.getString("name")),
      resultSet.getBoolean("is_enabled")
    );
  }

  @Override
  public List<ConfigurationDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<ConfigurationDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las configuraciones.");
    }
  }

  @Override
  public ConfigurationDTO getOne(ConfigurationName name) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, name.toDBName());

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return createDTOInstanceFromResultSet(resultSet);
        } else {
          LOGGER.fatal("Configuración {} No Registrada. Revisar Inserción en db.sql", name);
          throw new UserDisplayableException(
            "No ha sido posible encontrar configuración del sistema. Por favor, contacte al desarrollador del sistema."
          );
        }
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar la configuración." );
    }
  }

  @Override
  public void updateOne(ConfigurationDTO configurationDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setBoolean(1, configurationDTO.isEnabled());
      statement.setString(2, configurationDTO.name().toDBName());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar la configuración.");
    }
  }
}
