package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.dao.shape.CompleteDAOShape;
import org.example.business.dto.CourseDTO;
import org.example.business.dto.enumeration.CourseState;
import org.example.business.dto.enumeration.Section;
import org.example.business.dto.enumeration.Semester;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends CompleteDAOShape<CourseDTO, String> {
  private static final Logger LOGGER = LogManager.getLogger(CourseDAO.class);
  private static final String CREATE_QUERY =
    "INSERT INTO Course (nrc, id_academic, section) VALUES (?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM CourseWithAcademic";
  private static final String GET_ALL_BY_ACADEMIC = "SELECT * FROM CourseWithAcademic WHERE id_academic = ?";
  private static final String GET_ALL_BY_STATE = "SELECT * FROM CourseWithAcademic WHERE state = ?";
  private static final String GET_QUERY = "SELECT * FROM CourseWithAcademic WHERE nrc = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Course SET id_academic = ?, section = ?, semester = ?, state = ? WHERE nrc = ?";
  private static final String DELETE_QUERY = "DELETE FROM Course WHERE nrc = ?";

  @Override
  public CourseDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new CourseDTO.CourseBuilder()
      .setNRC(resultSet.getString("nrc"))
      .setIDAcademic(resultSet.getString("id_academic"))
      .setSection(Section.valueOf(resultSet.getString("section")))
      .setSemester(Semester.valueOf(resultSet.getString("semester")))
      .setState(CourseState.valueOf(resultSet.getString("state")))
      .setFullNameAcademic(resultSet.getString("full_name_academic"))
      .setTotalStudents(resultSet.getInt("total_students"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(CourseDTO courseDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, courseDTO.getNRC());
      statement.setString(2, courseDTO.getIDAcademic());
      statement.setString(3, courseDTO.getSection().toString());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el curso.");
    }
  }

  @Override
  public List<CourseDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<CourseDTO> courseList = new ArrayList<>();

      while (resultSet.next()) {
        courseList.add(createDTOInstanceFromResultSet(resultSet));
      }

      return courseList;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los cursos.");
    }
  }

  public List<CourseDTO> getAllByAcademic(String idAcademic) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_ACADEMIC)
    ) {
      statement.setString(1, idAcademic);
      List<CourseDTO> courseList = new ArrayList<>();

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          courseList.add(createDTOInstanceFromResultSet(resultSet));
        }
      }

      return courseList;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los cursos del académico.");
    }
  }

  public List<CourseDTO> getAllByState(CourseState state) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_STATE)
    ) {
      statement.setString(1, state.toString());
      List<CourseDTO> courseList = new ArrayList<>();

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          courseList.add(createDTOInstanceFromResultSet(resultSet));
        }
      }

      return courseList;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los cursos por estado.");
    }
  }

  @Override
  public CourseDTO getOne(String nrc) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, nrc);

      CourseDTO courseDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          courseDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return courseDTO;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar el curso.");
    }
  }

  @Override
  public void updateOne(CourseDTO courseDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, courseDTO.getIDAcademic());
      statement.setString(2, courseDTO.getSection().toString());
      statement.setString(3, courseDTO.getSemester().toDBString());
      statement.setString(4, courseDTO.getState().toDBString());
      statement.setString(5, courseDTO.getNRC());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el curso.");
    }
  }

  @Override
  public void deleteOne(String nrc) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, nrc);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar el curso.");
    }
  }
}