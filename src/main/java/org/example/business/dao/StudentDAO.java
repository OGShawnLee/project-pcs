package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.business.dao.shape.CompleteDAOShape;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.StudentDTO;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class StudentDAO extends CompleteDAOShape<StudentDTO, String> {
  private static final Logger LOGGER = LogManager.getLogger(StudentDAO.class);
  private static final String CREATE_QUERY =
    "CALL create_student(?, ?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY =
    "SELECT * FROM Student";
  private static final String GET_ALL_BY_ACADEMIC =
    "SELECT * FROM Student WHERE id_student IN (SELECT id_student FROM Enrollment WHERE id_academic = ?)";
  private static final String GET_ALL_BY_STATE_QUERY =
    "SELECT * FROM Student WHERE state = ?";
  private static final String GET_ALL_WITH_NO_PROJECT_QUERY =
    "SELECT * FROM Student WHERE id_student NOT IN (SELECT id_student FROM Practice) AND state = 'ACTIVE'";
  private static final String GET_QUERY =
    "SELECT * FROM Student WHERE id_student = ?";
  private static final String GET_BY_EMAIL_QUERY =
    "SELECT * FROM Student WHERE email = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Student SET name = ?, paternal_last_name = ?, maternal_last_name = ?, phone_number = ?, state = ?, final_grade = ? WHERE id_student = ?";
  private static final String DELETE_QUERY =
    "DELETE FROM Student WHERE id_student = ?";

  @Override
  public StudentDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new StudentDTO.StudentBuilder()
      .setID(resultSet.getString("id_student"))
      .setEmail(resultSet.getString("email"))
      .setName(resultSet.getString("name"))
      .setPaternalLastName(resultSet.getString("paternal_last_name"))
      .setMaternalLastName(resultSet.getString("maternal_last_name"))
      .setState(resultSet.getString("state"))
      .setPhoneNumber(resultSet.getString("phone_number"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .setFinalGrade(resultSet.getInt("final_grade"))
      .build();
  }

  @Override
  public void createOne(StudentDTO studentDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      CallableStatement statement = connection.prepareCall(CREATE_QUERY)
    ) {
      statement.setString(1, studentDTO.getID());
      statement.setString(2, studentDTO.getEmail());
      statement.setString(3, studentDTO.getName());
      statement.setString(4, studentDTO.getPaternalLastName());
      statement.setString(5, studentDTO.getMaternalLastName());
      statement.setString(6, AccountDTO.getGeneratedHashedPassword(studentDTO.getID()));
      statement.setString(7, studentDTO.getPhoneNumber());

      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el estudiante.");
    }
  }

  @Override
  public List<StudentDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      ArrayList<StudentDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener la lista de estudiantes.");
    }
  }

  public List<StudentDTO> getAllByAcademic(String academicID) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_ACADEMIC)
    ) {
      statement.setString(1, academicID);
      List<StudentDTO> list = new ArrayList<>();

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          list.add(createDTOInstanceFromResultSet(resultSet));
        }
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener la lista de estudiantes por académico.");
    }
  }

  public List<StudentDTO> getAllByState(String state) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_STATE_QUERY)
    ) {
      statement.setString(1, state);
      List<StudentDTO> list = new ArrayList<>();

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          list.add(createDTOInstanceFromResultSet(resultSet));
        }
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener la lista de estudiantes por estado.");
    }
  }

  public List<StudentDTO> getAllWithNoProject() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_WITH_NO_PROJECT_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      ArrayList<StudentDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener la lista de estudiantes sin proyecto.");
    }
  }

  @Override
  public StudentDTO getOne(String id) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, id);

      StudentDTO studentDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          studentDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return studentDTO;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el estudiante.");
    }
  }

  public StudentDTO getOneByEmail(String email) throws NotFoundException, UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_BY_EMAIL_QUERY)
    ) {
      statement.setString(1, email);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return createDTOInstanceFromResultSet(resultSet);
        } else {
          throw new NotFoundException("No se ha encontrado un estudiante con el email proporcionado.");
        }
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el estudiante por email.");
    }
  }

  @Override
  public void updateOne(StudentDTO studentDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, studentDTO.getName());
      statement.setString(2, studentDTO.getPaternalLastName());
      statement.setString(3, studentDTO.getMaternalLastName());
      statement.setString(4, studentDTO.getPhoneNumber());
      statement.setString(5, studentDTO.getState());
      statement.setInt(6, studentDTO.getFinalGrade());
      statement.setString(7, studentDTO.getID());

      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el estudiante.");
    }
  }

  @Override
  public void deleteOne(String id) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, id);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar el estudiante.");
    }
  }
}