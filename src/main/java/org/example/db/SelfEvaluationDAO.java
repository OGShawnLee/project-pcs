package org.example.db;

import org.example.business.SelfEvaluationDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelfEvaluationDAO extends DAO<SelfEvaluationDTO, String> {
  private static final String CREATE_QUERY =
    "INSERT INTO SelfEvaluation (" +
      "id_student, follow_up_grade, safety_grade, knowledge_application_grade, interesting_grade, " +
      "productivity_grade, congruent_grade, informed_by_organization, regulated_by_organization, " +
      "importance_for_professional_development, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM SelfEvaluation";
  private static final String GET_QUERY = "SELECT * FROM SelfEvaluation WHERE id_student = ?";
  private static final String UPDATE_QUERY =
    "UPDATE SelfEvaluation SET follow_up_grade = ?, safety_grade = ?, knowledge_application_grade = ?, " +
      "interesting_grade = ?, productivity_grade = ?, congruent_grade = ?, informed_by_organization = ?, " +
      "regulated_by_organization = ?, importance_for_professional_development = ?, created_at = ? " +
      "WHERE id_student = ?";
  private static final String DELETE_QUERY = "DELETE FROM SelfEvaluation WHERE id_student = ?";

  @Override
  protected SelfEvaluationDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new SelfEvaluationDTO.SelfEvaluationBuilder()
      .setIdStudent(resultSet.getString("id_student"))
      .SetFollowUpGrade(resultSet.getInt("follow_up_grade"))
      .SetSafetyGrade(resultSet.getInt("safety_grade"))
      .setKnowledgeApplicationGrade(resultSet.getInt("knowledge_application_grade"))
      .setInterestingGrade(resultSet.getInt("interesting_grade"))
      .setProductivityGrade(resultSet.getInt("productivity_grade"))
      .setCongruentGrade(resultSet.getInt("congruent_grade"))
      .setInformedByOrganization(resultSet.getInt("informed_by_organization"))
      .setRegulatedByOrganization(resultSet.getInt("regulated_by_organization"))
      .setImportanceForProfessionalDevelopment(resultSet.getInt("importance_for_professional_development"))
      .build();
  }

  @Override
  public void create(SelfEvaluationDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getIdStudent());
      statement.setInt(2, dataObject.getFollowUpGrade());
      statement.setInt(3, dataObject.getSafetyGrade());
      statement.setInt(4, dataObject.getKnowledgeApplicationGrade());
      statement.setInt(5, dataObject.getInterestingGrade());
      statement.setInt(6, dataObject.getProductivityGrade());
      statement.setInt(7, dataObject.getCongruentGrade());
      statement.setInt(8, dataObject.getInformedByOrganization());
      statement.setInt(9, dataObject.getRegulatedByOrganization());
      statement.setInt(10, dataObject.getImportanceForProfessionalDevelopment());
      statement.setString(11, dataObject.getCreatedAt());
      statement.executeUpdate();
    }
  }

  @Override
  public List<SelfEvaluationDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<SelfEvaluationDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public SelfEvaluationDTO get(String id) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, id);

      SelfEvaluationDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    }
  }

  @Override
  public void update(SelfEvaluationDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setInt(1, dataObject.getFollowUpGrade());
      statement.setInt(2, dataObject.getSafetyGrade());
      statement.setInt(3, dataObject.getKnowledgeApplicationGrade());
      statement.setInt(4, dataObject.getInterestingGrade());
      statement.setInt(5, dataObject.getProductivityGrade());
      statement.setInt(6, dataObject.getCongruentGrade());
      statement.setInt(7, dataObject.getInformedByOrganization());
      statement.setInt(8, dataObject.getRegulatedByOrganization());
      statement.setInt(9, dataObject.getImportanceForProfessionalDevelopment());
      statement.setString(10, dataObject.getCreatedAt());
      statement.setString(11, dataObject.getIdStudent());
      statement.executeUpdate();
    }
  }

  @Override
  public void delete(String idStudent) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, idStudent);
      statement.executeUpdate();
    }
  }
}