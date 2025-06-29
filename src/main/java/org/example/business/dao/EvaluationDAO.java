package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.dao.shape.CompleteDAOShape;
import org.example.business.dto.EvaluationDTO;
import org.example.business.dao.filter.FilterEvaluation;
import org.example.business.dto.enumeration.EvaluationKind;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class EvaluationDAO extends CompleteDAOShape<EvaluationDTO, FilterEvaluation> {
  private static final Logger LOGGER = LogManager.getLogger(EvaluationDAO.class);
  private static final String CREATE_QUERY =
    "INSERT INTO Evaluation (id_academic, id_project, id_student, adequate_use_grade, feedback, content_congruence_grade, writing_grade, methodological_rigor_grade, kind) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Evaluation";
  private static final String GET_QUERY = "SELECT * FROM Evaluation WHERE id_academic = ? AND id_project = ? AND id_student = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Evaluation SET adequate_use_grade = ?, feedback = ?, content_congruence_grade = ?, writing_grade = ?, methodological_rigor_grade = ?, kind = ? WHERE id_academic = ? AND id_project = ? AND id_student = ?";
  private static final String DELETE_QUERY = "DELETE FROM Evaluation WHERE id_academic = ? AND id_project = ? AND id_student = ?";

  @Override
  public EvaluationDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new EvaluationDTO.EvaluationBuilder()
      .setIDAcademic(resultSet.getString("id_academic"))
      .setIDProject(resultSet.getInt("id_project"))
      .setIDStudent(resultSet.getString("id_student"))
      .setAdequateUseGrade(resultSet.getString("adequate_use_grade"))
      .setFeedback(resultSet.getString("feedback"))
      .setContentCongruenceGrade(resultSet.getString("content_congruence_grade"))
      .setWritingGrade(resultSet.getString("writing_grade"))
      .setMethodologicalRigorGrade(resultSet.getString("methodological_rigor_grade"))
      .setKind(EvaluationKind.valueOf(resultSet.getString("kind")))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(EvaluationDTO evaluationDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, evaluationDTO.getIDAcademic());
      statement.setInt(2, evaluationDTO.getIDProject());
      statement.setString(3, evaluationDTO.getIDStudent());
      statement.setInt(4, evaluationDTO.getAdequateUseGrade());
      statement.setString(5, evaluationDTO.getFeedback());
      statement.setInt(6, evaluationDTO.getContentCongruenceGrade());
      statement.setInt(7, evaluationDTO.getWritingGrade());
      statement.setInt(8, evaluationDTO.getMethodologicalRigorGrade());
      statement.setString(9, evaluationDTO.getKind().toString());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear la evaluación.");
    }
  }

  @Override
  public List<EvaluationDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<EvaluationDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las evaluaciones.");
    }
  }

  @Override
  public EvaluationDTO getOne(FilterEvaluation filter) throws UserDisplayableException {
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
        }
      }

      return null;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar la evaluación.");
    }
  }

  @Override
  public void updateOne(EvaluationDTO dataObject) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setInt(1, dataObject.getAdequateUseGrade());
      statement.setString(2, dataObject.getFeedback());
      statement.setInt(3, dataObject.getContentCongruenceGrade());
      statement.setInt(4, dataObject.getWritingGrade());
      statement.setInt(5, dataObject.getMethodologicalRigorGrade());
      statement.setString(6, dataObject.getKind().toString());
      statement.setString(7, dataObject.getIDAcademic());
      statement.setInt(8, dataObject.getIDProject());
      statement.setString(9, dataObject.getIDStudent());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar la evaluación.");
    }
  }

  @Override
  public void deleteOne(FilterEvaluation filter) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, filter.getIDAcademic());
      statement.setInt(2, filter.getIDProject());
      statement.setString(3, filter.getIDStudent());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar la evaluación.");
    }
  }
}
