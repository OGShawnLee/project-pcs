package org.example.db;

import org.example.business.CourseDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends DAO<CourseDTO, String> {
  private static final String CREATE_QUERY =
    "INSERT INTO Course (nrc, id_academic, section, started_at, ended_at) VALUES (?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Course";
  private static final String GET_QUERY = "SELECT * FROM Course WHERE nrc = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Course SET id_academic = ?, section = ?, started_at = ?, ended_at = ? WHERE nrc = ?";
  private static final String DELETE_QUERY = "DELETE FROM Course WHERE nrc = ?";

  @Override
  public CourseDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new CourseDTO.CourseBuilder()
      .setNrc(resultSet.getString("nrc"))
      .setIdAcademic(resultSet.getString("id_academic"))
      .setSection(resultSet.getString("section"))
      .setStartedAt(resultSet.getString("started_at"))
      .setEndedAt(resultSet.getString("ended_at"))
      .build();
  }

  @Override
  public void create(CourseDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getNrc());
      statement.setString(2, dataObject.getIdAcademic());
      statement.setString(3, dataObject.getSection());
      statement.setString(4, dataObject.getStartedAt());
      statement.setString(5, dataObject.getEndedAt());
      statement.executeUpdate();
    }
  }

  @Override
  public List<CourseDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<CourseDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public CourseDTO get(String nrc) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, nrc);

      CourseDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    }
  }

  @Override
  public void update(CourseDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.getIdAcademic());
      statement.setString(2, dataObject.getSection());
      statement.setString(3, dataObject.getStartedAt());
      statement.setString(4, dataObject.getEndedAt());
      statement.setString(5, dataObject.getNrc());
      statement.executeUpdate();
    }
  }

  @Override
  public void delete(String nrc) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, nrc);
      statement.executeUpdate();
    }
  }
}