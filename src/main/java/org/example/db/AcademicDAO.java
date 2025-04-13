package org.example.db;

import org.example.business.AcademicDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcademicDAO extends DAO<AcademicDTO, String> {
  private static final String CREATE_QUERY =
    "INSERT INTO Academic (id_academic, email, name, paternal_last_name, maternal_last_name, role) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Academic";
  private static final String GET_QUERY = "SELECT * FROM Academic WHERE id_academic = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Academic SET name = ?, paternal_last_name = ?, maternal_last_name = ? WHERE id_academic = ?";
  private static final String DELETE_QUERY = "DELETE FROM Academic WHERE id_academic = ?";

  @Override
  protected AcademicDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new AcademicDTO.AcademicBuilder()
      .setID(resultSet.getString("id_academic"))
      .setEmail(resultSet.getString("email"))
      .setName(resultSet.getString("name"))
      .setPaternalLastName(resultSet.getString("paternal_last_name"))
      .setMaternalLastName(resultSet.getString("maternal_last_name"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .setState(resultSet.getString("state"))
      .setRole(resultSet.getString("role")).build();
  }

  @Override
  public void create(AcademicDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getID());
      statement.setString(2, dataObject.getEmail());
      statement.setString(3, dataObject.getName());
      statement.setString(4, dataObject.getPaternalLastName());
      statement.setString(5, dataObject.getMaternalLastName());
      statement.setString(6, dataObject.getRole());
      statement.executeUpdate();
    }
  }

  @Override
  public List<AcademicDTO> getAll() throws SQLException {
    try (
      Connection conn = getConnection();
      PreparedStatement statement = conn.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<AcademicDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public AcademicDTO get(String id) throws SQLException {
    try (
      Connection conn = getConnection();
      PreparedStatement statement = conn.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, id);

      AcademicDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    }
  }

  @Override
  public void update(AcademicDTO dataObject) throws SQLException {
    try (
      Connection conn = getConnection();
      PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.getName());
      statement.setString(2, dataObject.getPaternalLastName());
      statement.setString(3, dataObject.getMaternalLastName());
      statement.setString(4, dataObject.getID());
      statement.executeUpdate();
    }
  }

  @Override
  public void delete(String id) throws SQLException {
    try (
      Connection conn = getConnection();
      PreparedStatement statement = conn.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, id);
      statement.executeUpdate();
    }
  }
}