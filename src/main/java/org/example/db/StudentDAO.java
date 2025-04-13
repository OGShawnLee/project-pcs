package org.example.db;

import org.example.business.StudentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO extends DAO<StudentDTO, String> {
  protected static final String CREATE_QUERY =
    "INSERT INTO Student (id_student, email, name, paternal_last_name, maternal_last_name) VALUES (?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY =
    "SELECT * FROM Student";
  private static final String GET_QUERY =
    "SELECT * FROM Student WHERE id_student = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Student SET name = ?, paternal_last_name = ?, maternal_last_name = ? WHERE id_student = ?";
  private static final String DELETE_QUERY =
    "DELETE FROM Student WHERE id_student = ?";

  @Override
  protected StudentDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new StudentDTO.StudentBuilder()
      .setID(resultSet.getString("id_student"))
      .setEmail(resultSet.getString("email"))
      .setName(resultSet.getString("name"))
      .setPaternalLastName(resultSet.getString("paternal_last_name"))
      .setMaternalLastName(resultSet.getString("maternal_last_name"))
      .setState(resultSet.getString("state"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void create(StudentDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getID());
      statement.setString(2, dataObject.getEmail());
      statement.setString(3, dataObject.getName());
      statement.setString(4, dataObject.getPaternalLastName());
      statement.setString(5, dataObject.getMaternalLastName());

      statement.executeUpdate();
    }
  }

  @Override
  public List<StudentDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      ArrayList<StudentDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public StudentDTO get(String filter) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, filter);

      StudentDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    }
  }

  @Override
  public void update(StudentDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
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
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, id);
      statement.executeUpdate();
    }
  }
}