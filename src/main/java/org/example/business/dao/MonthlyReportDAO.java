package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.dao.shape.CompleteDAOShape;
import org.example.business.dto.MonthlyReportDTO;
import org.example.business.dao.filter.FilterMonthlyReport;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MonthlyReportDAO extends CompleteDAOShape<MonthlyReportDTO, FilterMonthlyReport> {
  private static final Logger LOGGER = LogManager.getLogger(MonthlyReportDAO.class);
  private static final String CREATE_QUERY =
    "INSERT INTO MonthlyReport (id_project, id_student, id_course, month, year, worked_hours, report) VALUES (?, ?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM MonthlyReport";
  private static final String GET_QUERY =
    "SELECT * FROM MonthlyReport WHERE id_project = ? AND id_student = ? AND id_course = ? AND month = ? AND year = ?";
  private static final String UPDATE_QUERY =
    "UPDATE MonthlyReport SET worked_hours = ?, report = ? WHERE id_project = ? AND id_student = ? AND id_course = ? AND month = ? AND year = ?";
  private static final String DELETE_QUERY =
    "DELETE FROM MonthlyReport WHERE id_project = ? AND id_student = ? AND month = ? AND year = ? AND id_course = ?";

  @Override
  public MonthlyReportDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new MonthlyReportDTO.MonthlyReportBuilder()
      .setIDProject(resultSet.getInt("id_project"))
      .setIDStudent(resultSet.getString("id_student"))
      .setIDCourse(resultSet.getString("id_course"))
      .setMonth(resultSet.getInt("month"))
      .setYear(resultSet.getInt("year"))
      .setWorkedHours(resultSet.getInt("worked_hours"))
      .setReport(resultSet.getString("report"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(MonthlyReportDTO dataObject) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setInt(1, dataObject.getIDProject());
      statement.setString(2, dataObject.getIDStudent());
      statement.setString(3, dataObject.getIDCourse());
      statement.setInt(4, dataObject.getMonth());
      statement.setInt(5, dataObject.getYear());
      statement.setInt(6, dataObject.getWorkedHours());
      statement.setString(7, dataObject.getReport());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el informe mensual.");
    }
  }

  @Override
  public List<MonthlyReportDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<MonthlyReportDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los informes mensuales.");
    }
  }

  @Override
  public MonthlyReportDTO getOne(FilterMonthlyReport filter) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setInt(1, filter.getIDProject());
      statement.setString(2, filter.getIDStudent());
      statement.setString(3, filter.getIDCourse());
      statement.setInt(4, filter.getMonth());
      statement.setInt(5, filter.getYear());

      MonthlyReportDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar el informe mensual.");
    }
  }

  @Override
  public void updateOne(MonthlyReportDTO dataObject) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setInt(1, dataObject.getWorkedHours());
      statement.setString(2, dataObject.getReport());
      statement.setInt(3, dataObject.getIDProject());
      statement.setString(4, dataObject.getIDStudent());
      statement.setString(5, dataObject.getIDCourse());
      statement.setInt(6, dataObject.getMonth());
      statement.setInt(7, dataObject.getYear());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el informe mensual.");
    }
  }

  @Override
  public void deleteOne(FilterMonthlyReport filter) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setInt(1, filter.getIDProject());
      statement.setString(2, filter.getIDStudent());
      statement.setInt(3, filter.getMonth());
      statement.setInt(4, filter.getYear());
      statement.setString(5, filter.getIDCourse());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar el informe mensual.");
    }
  }
}