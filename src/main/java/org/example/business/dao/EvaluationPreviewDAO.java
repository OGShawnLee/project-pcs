package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.business.dao.filter.FilterEvaluation;
import org.example.business.dao.shape.DAOShape;
import org.example.business.dao.shape.GetOneDAOShape;
import org.example.business.dto.EvaluationPreviewDTO;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvaluationPreviewDAO
  extends DAOShape<EvaluationPreviewDTO>
  implements GetOneDAOShape<EvaluationPreviewDTO, FilterEvaluation> {
  private static final Logger LOGGER = LogManager.getLogger(EvaluationPreviewDAO.class);
  private static final String GET_QUERY = "SELECT * FROM EvaluationPreview WHERE id_academic = ? AND id_project = ? AND id_student = ? ORDER BY created_at DESC";
  private static final String GET_ALL_BY_STUDENT = "SELECT * FROM EvaluationPreview WHERE id_student = ? ORDER BY created_at DESC";
  private static final String GET_ALL_BY_ACADEMIC =
    """
      SELECT * FROM EvaluationPreview
      WHERE EXISTS (SELECT 1 FROM Enrollment WHERE id_academic = ? AND id_course = ? AND Enrollment.id_student = EvaluationPreview.id_student)
      ORDER BY created_at DESC
    """;
  private static final String GET_ALL_BY_Evaluator = "SELECT * FROM EvaluationPreview WHERE id_academic = ? ORDER BY created_at DESC";

  @Override
  public EvaluationPreviewDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new EvaluationPreviewDTO(
      new EvaluationDAO().getDTOInstanceFromResultSet(resultSet),
      resultSet.getString("full_name_student"),
      resultSet.getString("full_name_academic")
    );
  }

  public List<EvaluationPreviewDTO> getAllByAcademic(String idAcademic, String idCourse) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_ACADEMIC)
    ) {
      statement.setString(1, idAcademic);
      statement.setString(2, idCourse);

      try (ResultSet resultSet = statement.executeQuery()) {
        List<EvaluationPreviewDTO> list = new ArrayList<>();

        while (resultSet.next()) {
          list.add(getDTOInstanceFromResultSet(resultSet));
        }

        return list;
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las evaluaciones del académico.");
    }
  }

  public List<EvaluationPreviewDTO> getAllByStudent(String idStudent) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_STUDENT)
    ) {
      statement.setString(1, idStudent);
      try (ResultSet resultSet = statement.executeQuery()) {
        List<EvaluationPreviewDTO> list = new ArrayList<>();

        while (resultSet.next()) {
          list.add(getDTOInstanceFromResultSet(resultSet));
        }

        return list;
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las evaluaciones del estudiante.");
    }
  }

  public List<EvaluationPreviewDTO> getAllByEvaluator(String idAcademic) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_Evaluator)
    ) {
      statement.setString(1, idAcademic);
      try (ResultSet resultSet = statement.executeQuery()) {
        List<EvaluationPreviewDTO> list = new ArrayList<>();

        while (resultSet.next()) {
          list.add(getDTOInstanceFromResultSet(resultSet));
        }

        return list;
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las evaluaciones del evaluador.");
    }
  }

  @Override
  public EvaluationPreviewDTO getOne(FilterEvaluation filter) throws NotFoundException, UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, filter.getIDAcademic());
      statement.setInt(2, filter.getIDProject());
      statement.setString(3, filter.getIDStudent());

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return createDTOInstanceFromResultSet(resultSet);
        } else {
          throw new UserDisplayableException("No se encontró la evaluación solicitada");
        }
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar la evaluación.");
    }
  }
}
