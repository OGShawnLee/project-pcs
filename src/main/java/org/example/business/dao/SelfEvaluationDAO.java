package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.dao.filter.FilterSelfEvaluation;
import org.example.business.dao.shape.CompleteDAOShape;
import org.example.business.dto.SelfEvaluationDTO;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelfEvaluationDAO extends CompleteDAOShape<SelfEvaluationDTO, FilterSelfEvaluation> {
  private static final Logger LOGGER = LogManager.getLogger(SelfEvaluationDAO.class);
  private static final String CREATE_QUERY =
    "INSERT INTO SelfEvaluation (" +
      "id_student, id_course, follow_up_grade, safety_grade, knowledge_application_grade, interesting_grade, " +
      "productivity_grade, congruent_grade, informed_by_organization, regulated_by_organization, " +
      "importance_for_professional_development) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM SelfEvaluation";
  private static final String GET_QUERY = "SELECT * FROM SelfEvaluation WHERE id_student = ? AND id_course = ?";
  private static final String UPDATE_QUERY =
    "UPDATE SelfEvaluation SET follow_up_grade = ?, safety_grade = ?, knowledge_application_grade = ?, " +
      "interesting_grade = ?, productivity_grade = ?, congruent_grade = ?, informed_by_organization = ?, " +
      "regulated_by_organization = ?, importance_for_professional_development = ? " +
      "WHERE id_student = ? AND id_course = ?";
  private static final String DELETE_QUERY = "DELETE FROM SelfEvaluation WHERE id_student = ? AND id_course = ?";

  @Override
  public SelfEvaluationDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new SelfEvaluationDTO.SelfEvaluationBuilder()
      .setIDStudent(resultSet.getString("id_student"))
      .setIDCourse(resultSet.getString("id_course"))
      .setFollowUpGrade(resultSet.getInt("follow_up_grade"))
      .setSafetyGrade(resultSet.getInt("safety_grade"))
      .setKnowledgeApplicationGrade(resultSet.getInt("knowledge_application_grade"))
      .setInterestingGrade(resultSet.getInt("interesting_grade"))
      .setProductivityGrade(resultSet.getInt("productivity_grade"))
      .setCongruentGrade(resultSet.getInt("congruent_grade"))
      .setInformedByOrganization(resultSet.getInt("informed_by_organization"))
      .setRegulatedByOrganization(resultSet.getInt("regulated_by_organization"))
      .setImportanceForProfessionalDevelopment(resultSet.getInt("importance_for_professional_development"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(SelfEvaluationDTO selfEvaluationDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, selfEvaluationDTO.getIDStudent());
      statement.setString(2, selfEvaluationDTO.getIDCourse());
      statement.setInt(3, selfEvaluationDTO.getFollowUpGrade());
      statement.setInt(4, selfEvaluationDTO.getSafetyGrade());
      statement.setInt(5, selfEvaluationDTO.getKnowledgeApplicationGrade());
      statement.setInt(6, selfEvaluationDTO.getInterestingGrade());
      statement.setInt(7, selfEvaluationDTO.getProductivityGrade());
      statement.setInt(8, selfEvaluationDTO.getCongruentGrade());
      statement.setInt(9, selfEvaluationDTO.getInformedByOrganization());
      statement.setInt(10, selfEvaluationDTO.getRegulatedByOrganization());
      statement.setInt(11, selfEvaluationDTO.getImportanceForProfessionalDevelopment());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear la autoevaluación.");
    }
  }

  @Override
  public List<SelfEvaluationDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<SelfEvaluationDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las autoevaluaciones.");
    }
  }

  @Override
  public SelfEvaluationDTO getOne(FilterSelfEvaluation filter) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setString(2, filter.getIDCourse());

      SelfEvaluationDTO selfEvaluationDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          selfEvaluationDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return selfEvaluationDTO;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar la autoevaluación.");
    }
  }

  @Override
  public void updateOne(SelfEvaluationDTO selfEvaluationDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setInt(1, selfEvaluationDTO.getFollowUpGrade());
      statement.setInt(2, selfEvaluationDTO.getSafetyGrade());
      statement.setInt(3, selfEvaluationDTO.getKnowledgeApplicationGrade());
      statement.setInt(4, selfEvaluationDTO.getInterestingGrade());
      statement.setInt(5, selfEvaluationDTO.getProductivityGrade());
      statement.setInt(6, selfEvaluationDTO.getCongruentGrade());
      statement.setInt(7, selfEvaluationDTO.getInformedByOrganization());
      statement.setInt(8, selfEvaluationDTO.getRegulatedByOrganization());
      statement.setInt(9, selfEvaluationDTO.getImportanceForProfessionalDevelopment());
      statement.setString(10, selfEvaluationDTO.getIDStudent());
      statement.setString(11, selfEvaluationDTO.getIDCourse());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar la autoevaluación.");
    }
  }

  @Override
  public void deleteOne(FilterSelfEvaluation filter) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setString(2, filter.getIDCourse());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar la autoevaluación.");
    }
  }
}