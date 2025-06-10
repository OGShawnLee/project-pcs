package org.example.business.dao;

import org.example.business.dto.AcademicDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcademicDAO extends DAOPattern<AcademicDTO, String> {
  private static final String CREATE_QUERY =
    "INSERT INTO Academic (id_academic, email, name, paternal_last_name, maternal_last_name, role) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Academic";
  private static final String GET_QUERY = "SELECT * FROM Academic WHERE id_academic = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Academic SET name = ?, paternal_last_name = ?, maternal_last_name = ?, role = ?, state = ? WHERE id_academic = ?";
  private static final String DELETE_QUERY = "DELETE FROM Academic WHERE id_academic = ?";

  @Override
  AcademicDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new AcademicDTO.AcademicBuilder()
      .setID(resultSet.getString("id_academic"))
      .setEmail(resultSet.getString("email"))
      .setName(resultSet.getString("name"))
      .setPaternalLastName(resultSet.getString("paternal_last_name"))
      .setMaternalLastName(resultSet.getString("maternal_last_name"))
      .setState(resultSet.getString("state"))
      .setRole(AcademicDTO.Role.valueOf(resultSet.getString("role")))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(AcademicDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getID());
      statement.setString(2, dataObject.getEmail());
      statement.setString(3, dataObject.getName());
      statement.setString(4, dataObject.getPaternalLastName());
      statement.setString(5, dataObject.getMaternalLastName());
      statement.setString(6, dataObject.getRole().toString());
      statement.executeUpdate();
    }
  }

  @Override
  public List<AcademicDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
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
  public AcademicDTO getOne(String id) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
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
  public void updateOne(AcademicDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.getName());
      statement.setString(2, dataObject.getPaternalLastName());
      statement.setString(3, dataObject.getMaternalLastName());
      statement.setString(4, dataObject.getRole().toString());
      statement.setString(5, dataObject.getState());
      statement.setString(6, dataObject.getID());
      statement.executeUpdate();
    }
  }

  @Override
  public void deleteOne(String id) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, id);
      statement.executeUpdate();
    }
  }
}