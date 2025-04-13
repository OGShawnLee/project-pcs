package org.example.db;

import org.example.business.SelfEvaluationDTO;

import java.sql.*;
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
    try (Connection connection = getConnection();
         PreparedStatement stmt = connection.prepareStatement(CREATE_QUERY)) {

      stmt.setString(1, dataObject.getIdStudent());
      stmt.setInt(2, dataObject.getFollowUpGrade());
      stmt.setInt(3, dataObject.getSafetyGrade());
      stmt.setInt(4, dataObject.getKnowledgeApplicationGrade());
      stmt.setInt(5, dataObject.getInterestingGrade());
      stmt.setInt(6, dataObject.getProductivityGrade());
      stmt.setInt(7, dataObject.getCongruentGrade());
      stmt.setInt(8, dataObject.getInformedByOrganization());
      stmt.setInt(9, dataObject.getRegulatedByOrganization());
      stmt.setInt(10, dataObject.getImportanceForProfessionalDevelopment());
      stmt.setString(11, dataObject.getCreatedAt());

      stmt.executeUpdate();
    }
  }

  @Override
  public List<SelfEvaluationDTO> getAll() throws SQLException {
    List<SelfEvaluationDTO> list = new ArrayList<>();

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(GET_ALL_QUERY);
         ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        list.add(createDTOInstanceFromResultSet(rs));
      }
    }

    return list;
  }

  @Override
  public SelfEvaluationDTO get(String id) throws SQLException {
    SelfEvaluationDTO element = null;

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(GET_QUERY)) {

      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        element = createDTOInstanceFromResultSet(rs);
      }
    }

    return element;
  }

  @Override
  public void update(SelfEvaluationDTO dataObject) throws SQLException {
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY)) {

      stmt.setInt(1, dataObject.getFollowUpGrade());
      stmt.setInt(2, dataObject.getSafetyGrade());
      stmt.setInt(3, dataObject.getKnowledgeApplicationGrade());
      stmt.setInt(4, dataObject.getInterestingGrade());
      stmt.setInt(5, dataObject.getProductivityGrade());
      stmt.setInt(6, dataObject.getCongruentGrade());
      stmt.setInt(7, dataObject.getInformedByOrganization());
      stmt.setInt(8, dataObject.getRegulatedByOrganization());
      stmt.setInt(9, dataObject.getImportanceForProfessionalDevelopment());
      stmt.setString(10, dataObject.getCreatedAt());
      stmt.setString(11, dataObject.getIdStudent());

      stmt.executeUpdate();
    }
  }

  @Override
  public void delete(String idStudent) throws SQLException {
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY)) {

      stmt.setString(1, idStudent);
      stmt.executeUpdate();
    }
  }
}

