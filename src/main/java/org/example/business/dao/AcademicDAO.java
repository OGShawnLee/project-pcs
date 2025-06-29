package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.dao.shape.CompleteDAOShape;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.enumeration.AcademicRole;
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

public class AcademicDAO extends CompleteDAOShape<AcademicDTO, String> {
  private static final Logger LOGGER = LogManager.getLogger(AcademicDAO.class);
  private static final String CREATE_QUERY = "CALL create_academic(?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Academic";
  private static final String GET_ALL_BY_STATE_QUERY = "SELECT * FROM Academic WHERE state = ?";
  private static final String GET_QUERY = "SELECT * FROM Academic WHERE id_academic = ?";
  private static final String GET_BY_EMAIL_QUERY = "SELECT * FROM Academic WHERE email = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Academic SET name = ?, paternal_last_name = ?, maternal_last_name = ?, phone_number = ?, role = ?, state = ? WHERE id_academic = ?";
  private static final String DELETE_QUERY = "DELETE FROM Academic WHERE id_academic = ?";

  @Override
  public AcademicDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
      return new AcademicDTO.AcademicBuilder()
        .setID(resultSet.getString("id_academic"))
        .setEmail(resultSet.getString("email"))
        .setName(resultSet.getString("name"))
        .setPaternalLastName(resultSet.getString("paternal_last_name"))
        .setMaternalLastName(resultSet.getString("maternal_last_name"))
        .setPhoneNumber(resultSet.getString("phone_number"))
        .setState(resultSet.getString("state"))
        .setRole(AcademicRole.valueOf(resultSet.getString("role")))
        .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
        .build();
  }

  @Override
  public void createOne(AcademicDTO academicDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      CallableStatement statement = connection.prepareCall(CREATE_QUERY)
    ) {
      statement.setString(1, academicDTO.getID());
      statement.setString(2, academicDTO.getEmail());
      statement.setString(3, academicDTO.getName());
      statement.setString(4, academicDTO.getPaternalLastName());
      statement.setString(5, academicDTO.getMaternalLastName());
      statement.setString(6, AccountDTO.getGeneratedHashedPassword(academicDTO.getID()));
      statement.setString(7, academicDTO.getPhoneNumber());
      statement.setString(8, academicDTO.getRole().toString());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el académico.");
    }
  }

  @Override
  public List<AcademicDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<AcademicDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener la lista de académicos.");
    }
  }

  public List<AcademicDTO> getAllByState(String state) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_STATE_QUERY)
    ) {
      statement.setString(1, state);
      List<AcademicDTO> list = new ArrayList<>();

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          list.add(createDTOInstanceFromResultSet(resultSet));
        }
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener la lista de académicos por estado.");
    }
  }

  @Override
  public AcademicDTO getOne(String id) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, id);

      AcademicDTO academicDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          academicDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return academicDTO;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el académico.");
    }
  }

  public AcademicDTO getOneByEmail(String email) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_BY_EMAIL_QUERY)
    ) {
      statement.setString(1, email);

      AcademicDTO academicDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          academicDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return academicDTO;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el académico por email.");
    }
  }

  @Override
  public void updateOne(AcademicDTO academicDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, academicDTO.getName());
      statement.setString(2, academicDTO.getPaternalLastName());
      statement.setString(3, academicDTO.getMaternalLastName());
      statement.setString(4, academicDTO.getPhoneNumber());
      statement.setString(5, academicDTO.getRole().toString());
      statement.setString(6, academicDTO.getState());
      statement.setString(7, academicDTO.getID());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el académico.");
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
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar el académico.");
    }
  }
}