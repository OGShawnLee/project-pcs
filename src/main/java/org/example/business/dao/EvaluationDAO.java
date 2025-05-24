package org.example.business.dao;

import org.example.business.dto.EvaluationDTO;
import org.example.business.dao.filter.FilterEvaluation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvaluationDAO extends DAOPattern<EvaluationDTO, FilterEvaluation> {
  private static final String CREATE_QUERY =
    "INSERT INTO Evaluation (id_academic, id_project, id_student, content_grade, feedback, requirements_grade, skill_grade, writing_grade) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Evaluation";
  private static final String GET_QUERY = "SELECT * FROM Evaluation WHERE id_academic = ? AND id_project = ? AND id_student = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Evaluation SET content_grade = ?, feedback = ?, requirements_grade = ?, skill_grade = ?, writing_grade = ? WHERE id_academic = ? AND id_project = ? AND id_student = ?";
  private static final String DELETE_QUERY = "DELETE FROM Evaluation WHERE id_academic = ? AND id_project = ? AND id_student = ?";

  @Override
  EvaluationDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new EvaluationDTO.EvaluationBuilder()
      .setIDAcademic(resultSet.getString("id_academic"))
      .setIDProject(resultSet.getInt("id_project"))
      .setIDStudent(resultSet.getString("id_student"))
      .setContentGrade(resultSet.getString("content_grade"))
      .setFeedback(resultSet.getString("feedback"))
      .setSkillGrade(resultSet.getString("skill_grade"))
      .setRequirementsGrade(resultSet.getString("requirements_grade"))
      .setWritingGrade(resultSet.getString("writing_grade"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(EvaluationDTO evaluationDTO) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, evaluationDTO.getIDAcademic());
      statement.setInt(2, evaluationDTO.getIDProject());
      statement.setString(3, evaluationDTO.getIDStudent());
      statement.setInt(4, evaluationDTO.getContentGrade());
      statement.setString(5, evaluationDTO.getFeedback());
      statement.setInt(6, evaluationDTO.getRequirementsGrade());
      statement.setInt(7, evaluationDTO.getSkillGrade());
      statement.setInt(8, evaluationDTO.getWritingGrade());
      statement.executeUpdate();
    }
  }

  @Override
  public List<EvaluationDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<EvaluationDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public EvaluationDTO getOne(FilterEvaluation filter) throws SQLException {
    try (
      Connection connection = getConnection();
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
    }
  }

  @Override
  public void updateOne(EvaluationDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setInt(1, dataObject.getContentGrade());
      statement.setString(2, dataObject.getFeedback());
      statement.setInt(3, dataObject.getRequirementsGrade());
      statement.setInt(4, dataObject.getSkillGrade());
      statement.setInt(5, dataObject.getWritingGrade());
      statement.setString(6, dataObject.getIDAcademic());
      statement.setInt(7, dataObject.getIDProject());
      statement.setString(8, dataObject.getIDStudent());
      statement.executeUpdate();
    }
  }

  @Override
  public void deleteOne(FilterEvaluation filter) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, filter.getIDAcademic());
      statement.setInt(2, filter.getIDProject());
      statement.setString(3, filter.getIDStudent());
      statement.executeUpdate();
    }
  }
}
