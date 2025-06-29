package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.dao.shape.*;
import org.example.business.dto.EnrollmentDTO;
import org.example.business.dao.filter.FilterEnrollment;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO extends DAOShape<EnrollmentDTO> implements
  CreateOneDAOShape<EnrollmentDTO>,
  GetAllDAOShape<EnrollmentDTO>,
  GetOneDAOShape<EnrollmentDTO,
  FilterEnrollment>,
  DeleteOneDAOShape<FilterEnrollment>
{
  private static final Logger LOGGER = LogManager.getLogger(EnrollmentDAO.class);
  private static final String CREATE_QUERY =
    "INSERT INTO Enrollment (id_course, id_student, id_academic) VALUES (?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Enrollment";
  private static final String GET_QUERY = "SELECT * FROM Enrollment WHERE id_student = ? and id_course = ?";
  private static final String DELETE_QUERY = "DELETE FROM Enrollment WHERE id_student = ? and id_course = ?";

  @Override
  public EnrollmentDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new EnrollmentDTO.EnrollmentBuilder()
      .setIDAcademic(resultSet.getString("id_academic"))
      .setIDCourse(resultSet.getString("id_course"))
      .setIDStudent(resultSet.getString("id_student"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(EnrollmentDTO dataObject) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getIDCourse());
      statement.setString(2, dataObject.getIDStudent());
      statement.setString(3, dataObject.getIDAcademic());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear la inscripción.");
    }
  }

  @Override
  public List<EnrollmentDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<EnrollmentDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener las inscripciones.");
    }
  }

  @Override
  public EnrollmentDTO getOne(FilterEnrollment filter) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setString(2, filter.getIDCourse());

      EnrollmentDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener la inscripción.");
    }
  }

  @Override
  public void deleteOne(FilterEnrollment filter) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setString(2, filter.getIDCourse());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar la inscripción.");
    }
  }
}