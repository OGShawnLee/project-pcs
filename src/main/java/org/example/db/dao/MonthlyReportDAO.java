package org.example.db.dao;

import org.example.business.dto.MonthlyReportDTO;
import org.example.db.dao.filter.FilterMonthlyReport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MonthlyReportDAO extends DAOPattern<MonthlyReportDTO, FilterMonthlyReport> {
  private static final String CREATE_QUERY =
    "INSERT INTO MonthlyReport (id_project, id_student, month, year, worked_hours, report) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM MonthlyReport";
  private static final String GET_QUERY =
    "SELECT * FROM MonthlyReport WHERE id_project = ? AND id_student = ? AND month = ? AND year = ?";
  private static final String UPDATE_QUERY =
    "UPDATE MonthlyReport SET worked_hours = ?, report = ? WHERE id_project = ? AND id_student = ? AND month = ? AND year = ?";
  private static final String DELETE_QUERY =
    "DELETE FROM MonthlyReport WHERE id_project = ? AND id_student = ? AND month = ? AND year = ?";

  @Override
  public MonthlyReportDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new MonthlyReportDTO.MonthlyReportBuilder()
      .setIDProject(resultSet.getInt("id_project"))
      .setIDStudent(resultSet.getString("id_student"))
      .setMonth(resultSet.getInt("month"))
      .setYear(resultSet.getInt("year"))
      .setWorkedHours(resultSet.getInt("worked_hours"))
      .setReport(resultSet.getString("report"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(MonthlyReportDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setInt(1, dataObject.getIDProject());
      statement.setString(2, dataObject.getIDStudent());
      statement.setInt(3, dataObject.getMonth());
      statement.setInt(4, dataObject.getYear());
      statement.setInt(5, dataObject.getWorkedHours());
      statement.setString(6, dataObject.getReport());
      statement.executeUpdate();
    }
  }

  @Override
  public List<MonthlyReportDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<MonthlyReportDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public MonthlyReportDTO getOne(FilterMonthlyReport filter) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setInt(1, filter.getIDProject());
      statement.setString(2, filter.getIDStudent());
      statement.setInt(3, filter.getMonth());
      statement.setInt(4, filter.getYear());

      MonthlyReportDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    }
  }

  @Override
  public void updateOne(MonthlyReportDTO dataObject, FilterMonthlyReport filter) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setInt(1, dataObject.getWorkedHours());
      statement.setString(2, dataObject.getReport());
      statement.setInt(3, filter.getIDProject());
      statement.setString(4, filter.getIDStudent());
      statement.setInt(5, filter.getMonth());
      statement.setInt(6, filter.getYear());
      statement.executeUpdate();
    }
  }

  @Override
  public void deleteOne(FilterMonthlyReport filter) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setInt(1, filter.getIDProject());
      statement.setString(2, filter.getIDStudent());
      statement.setInt(3, filter.getMonth());
      statement.setInt(4, filter.getYear());
      statement.executeUpdate();
    }
  }
}