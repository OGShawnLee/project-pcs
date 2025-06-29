package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.business.dto.StatsDTO;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsDAO {
  private static final Logger LOGGER = LogManager.getLogger(StatsDAO.class);
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

  public StatsDTO getStats() throws UserDisplayableException, NotFoundException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_STATS_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      if (resultSet.next()) {
        return createDTOInstanceFromResultSet(resultSet);
      } else {
        throw ExceptionHandler.handleNotFoundException(LOGGER, "Estadísticas");
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las estadísticas.");
    }
  }
}
