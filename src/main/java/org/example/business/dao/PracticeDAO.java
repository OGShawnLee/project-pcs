package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.dao.shape.CompleteDAOShape;
import org.example.business.dto.PracticeDTO;
import org.example.business.dao.filter.FilterPractice;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PracticeDAO extends CompleteDAOShape<PracticeDTO, FilterPractice> {
  private static final Logger LOGGER = LogManager.getLogger(PracticeDAO.class);
  private static final String CREATE_QUERY =
    "INSERT INTO Practice (id_student, id_project, reason_of_assignation) VALUES (?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Practice";
  private static final String GET_QUERY = "SELECT * FROM Practice WHERE id_student = ? AND id_project = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Practice SET reason_of_assignation = ? WHERE id_student = ? AND id_project = ?";
  private static final String DELETE_QUERY = "DELETE FROM Practice WHERE id_student = ? AND id_project = ?";
  private static final String GET_BY_STUDENT_QUERY =
    "SELECT * FROM Practice WHERE id_student = ?";

  @Override
  public PracticeDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new PracticeDTO.PracticeBuilder()
      .setIDStudent(resultSet.getString("id_student"))
      .setIDProject(resultSet.getInt("id_project"))
      .setReasonOfAssignation(resultSet.getString("reason_of_assignation"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(PracticeDTO practiceDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, practiceDTO.getIDStudent());
      statement.setInt(2, practiceDTO.getIDProject());
      statement.setString(3, practiceDTO.getReasonOfAssignation());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear la práctica.");
    }
  }

  public void createMany(List<PracticeDTO> practiceDTOs) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      connection.setAutoCommit(false);

      try {
        for (PracticeDTO practiceDTO : practiceDTOs) {
          statement.setString(1, practiceDTO.getIDStudent());
          statement.setInt(2, practiceDTO.getIDProject());
          statement.setString(3, practiceDTO.getReasonOfAssignation());
          statement.addBatch();
        }

        statement.executeBatch();
        connection.commit();
      } catch (SQLException e) {
        connection.rollback();
        throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear las prácticas.");
      } finally {
        connection.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear las prácticas.");
    }
  }

  @Override
  public List<PracticeDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<PracticeDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las prácticas.");
    }
  }

  @Override
  public PracticeDTO getOne(FilterPractice filter) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setInt(2, filter.getIDPractice());

      PracticeDTO practiceDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          practiceDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return practiceDTO;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener la práctica.");
    }
  }

  public PracticeDTO getOneByStudent(String idStudent) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_BY_STUDENT_QUERY)
    ) {
      statement.setString(1, idStudent);

      PracticeDTO practiceDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          practiceDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return practiceDTO;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener la práctica por estudiante.");
    }
  }

  @Override
  public void updateOne(PracticeDTO practiceDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, practiceDTO.getReasonOfAssignation());
      statement.setString(2, practiceDTO.getIDStudent());
      statement.setInt(3, practiceDTO.getIDProject());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar la práctica.");
    }
  }

  @Override
  public void deleteOne(FilterPractice filter) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setInt(2, filter.getIDPractice());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar la práctica.");
    }
  }

  public void deleteMany(List<FilterPractice> filterPracticeList) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      connection.setAutoCommit(false);

      try {
        for (FilterPractice filterPractice : filterPracticeList) {
          statement.setString(1, filterPractice.getIDStudent());
          statement.setInt(2, filterPractice.getIDPractice());
          statement.addBatch();
        }

        statement.executeBatch();
        connection.commit();
      } catch (SQLException e) {
        connection.rollback();
        throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar las prácticas.");
      } finally {
        connection.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar las prácticas.");
    }
  }
}