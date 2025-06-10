package org.example.business.dao;

import org.example.business.dto.StatsDTO;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsDAO {
  private static final String GET_STATS_QUERY = "SELECT * FROM Stats";

  StatsDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new StatsDTO.Builder()
      .setTotalAcademics(resultSet.getInt("total_academics"))
      .setTotalEvaluators(resultSet.getInt("total_evaluators"))
      .setTotalOrganizations(resultSet.getInt("total_organizations"))
      .setTotalProjectRequests(resultSet.getInt("total_project_requests"))
      .setTotalEvaluations(resultSet.getInt("total_evaluations"))
      .setTotalSelfEvaluations(resultSet.getInt("total_self_evaluations"))
      .setTotalMonthlyReports(resultSet.getInt("total_monthly_reports"))
      .setTotalProjects(resultSet.getInt("total_projects"))
      .setTotalCourses(resultSet.getInt("total_courses"))
      .setTotalStudents(resultSet.getInt("total_students"))
      .build();
  }

  public StatsDTO getStats() throws SQLException {
    try (
      Connection connection = DBConnector.getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_STATS_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      if (resultSet.next()) {
        return createDTOInstanceFromResultSet(resultSet);
      } else {
        throw new SQLException("No stats found in the database.");
      }
    }
  }
}
