package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.dao.shape.CompleteDAOShape;
import org.example.business.dto.ProjectRequestDTO;
import org.example.business.dao.filter.FilterProject;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectRequestDAO extends CompleteDAOShape<ProjectRequestDTO, FilterProject> {
  private static final Logger LOGGER = LogManager.getLogger(ProjectRequestDAO.class);
  private static final String CREATE_QUERY =
    "INSERT INTO ProjectRequest (id_student, id_project, state, reason_of_state) VALUES (?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM ProjectRequest";
  private static final String GET_ALL_BY_EMAIL_QUERY = "SELECT * FROM ProjectRequest WHERE id_student = ?";
  private static final String GET_QUERY = "SELECT * FROM ProjectRequest WHERE id_student = ? AND id_project = ?";
  private static final String UPDATE_QUERY =
    "UPDATE ProjectRequest SET state = ?, reason_of_state = ? WHERE id_student = ? AND id_project = ?";
  private static final String DELETE_QUERY = "DELETE FROM ProjectRequest WHERE id_student = ? AND id_project = ?";

  @Override
  public ProjectRequestDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new ProjectRequestDTO.ProjectRequestBuilder()
      .setIDStudent(resultSet.getString("id_student"))
      .setIDProject(resultSet.getInt("id_project"))
      .setState(resultSet.getString("state"))
      .setReasonOfState(resultSet.getString("reason_of_state"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(ProjectRequestDTO dataObject) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getIDStudent());
      statement.setInt(2, dataObject.getIDProject());
      statement.setString(3, dataObject.getState());
      statement.setString(4, dataObject.getReasonOfState());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear la solicitud de proyecto.");
    }
  }

  @Override
  public List<ProjectRequestDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<ProjectRequestDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las solicitudes de proyecto.");
    }
  }

  public List<ProjectRequestDTO> getAllByEmail(String email) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_EMAIL_QUERY)
    ) {
      statement.setString(1, email);
      List<ProjectRequestDTO> list = new ArrayList<>();

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          list.add(createDTOInstanceFromResultSet(resultSet));
        }
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las solicitudes de proyecto.");
    }
  }

  @Override
  public ProjectRequestDTO getOne(FilterProject filter) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setInt(2, filter.getIDProject());

      ProjectRequestDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar la solicitud de proyecto.");
    }
  }

  @Override
  public void updateOne(ProjectRequestDTO dataObject) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.getState());
      statement.setString(2, dataObject.getReasonOfState());
      statement.setString(3, dataObject.getIDStudent());
      statement.setInt(4, dataObject.getIDProject());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar la solicitud de proyecto.");
    }
  }

  @Override
  public void deleteOne(FilterProject filter) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setInt(2, filter.getIDProject());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar la solicitud de proyecto.");
    }
  }
}