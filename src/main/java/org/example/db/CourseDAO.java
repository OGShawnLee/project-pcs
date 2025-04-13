package org.example.db;

import org.example.business.CourseDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends DAOPattern<CourseDTO, String> {
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
      .setNRC(resultSet.getString("nrc"))
      .setIDAcademic(resultSet.getString("id_academic"))
      .setSection(resultSet.getString("section"))
      .setStartedAt(resultSet.getTimestamp("started_at").toLocalDateTime())
      .setEndedAt(resultSet.getTimestamp("ended_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(CourseDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getNRC());
      statement.setString(2, dataObject.getIDAcademic());
      statement.setString(3, dataObject.getSection());
      statement.setDate(4, Common.fromLocalDateTime(dataObject.getStartedAt()));
      statement.setDate(5, Common.fromLocalDateTime(dataObject.getEndedAt()));
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
  public CourseDTO getOne(String nrc) throws SQLException {
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
  public void updateOne(CourseDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.getIDAcademic());
      statement.setString(2, dataObject.getSection());
      statement.setDate(3, Common.fromLocalDateTime(dataObject.getStartedAt()));
      statement.setDate(4, Common.fromLocalDateTime(dataObject.getEndedAt()));
      statement.setString(5, dataObject.getNRC());
      statement.executeUpdate();
    }
  }

  @Override
  public void deleteOne(String nrc) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, nrc);
      statement.executeUpdate();
    }
  }
}