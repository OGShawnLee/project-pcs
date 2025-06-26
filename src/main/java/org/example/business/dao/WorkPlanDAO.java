package org.example.business.dao;

import org.example.business.dto.WorkPlanDTO;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkPlanDAO implements GetOneDAOShape<WorkPlanDTO, Integer> {
  private static final String GET_ONE_QUERY = "SELECT * From WorkPlan WHERE id_project = ?";

  @Override
  public WorkPlanDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new WorkPlanDTO.WorkPlanBuilder()
      .setIDProject(resultSet.getInt("id_project"))
      .setProjectGoal(resultSet.getString("project_goal"))
      .setTheoreticalScope(resultSet.getString("theoretical_scope"))
      .setFirstMonthActivities(resultSet.getString("first_month_plan"))
      .setSecondMonthActivities(resultSet.getString("second_month_plan"))
      .setThirdMonthActivities(resultSet.getString("third_month_plan"))
      .setFourthMonthActivities(resultSet.getString("fourth_month_plan"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public WorkPlanDTO getOne(Integer projectID) throws SQLException, NotFoundException {
    try (
      Connection connection = DBConnector.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(GET_ONE_QUERY)
    ) {
      preparedStatement.setInt(1, projectID);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return createDTOInstanceFromResultSet(resultSet);
        } else {
          throw new NotFoundException("No se encontró el Cronograma de Actividades para el proyecto solicitado");
        }
      }
    }
  }
}
