package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.dao.shape.DAOShape;
import org.example.business.dao.shape.GetOneDAOShape;
import org.example.business.dto.WorkPlanDTO;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkPlanDAO extends DAOShape<WorkPlanDTO> implements GetOneDAOShape<WorkPlanDTO, Integer> {
  private static final Logger LOGGER = LogManager.getLogger(WorkPlanDAO.class);
  private static final String GET_ONE_QUERY = "SELECT * From WorkPlan WHERE id_project = ?";

  @Override
  public WorkPlanDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
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
  public WorkPlanDTO getOne(Integer projectID) throws UserDisplayableException, NotFoundException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(GET_ONE_QUERY)
    ) {
      preparedStatement.setInt(1, projectID);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return createDTOInstanceFromResultSet(resultSet);
        } else {
          throw ExceptionHandler.handleNotFoundException(LOGGER, "Cronograma de Actvidades");
        }
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar el Cronograma de Actividades.");
    }
  }
}
