package org.example.db.dao;

import org.example.business.dto.EvaluationDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvaluationDAO extends DAOPattern<EvaluationDTO, String> {
    private static final String CREATE_QUERY =
            "INSERT INTO Evaluation (id_student, id_project, id_academic, skill_grade, content_grade, writing_grade, requirements_grade, feedback, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
    private static final String GET_ALL_QUERY = "SELECT * FROM Evaluation";
    private static final String GET_QUERY = "SELECT * FROM Evaluation WHERE id_student = ?";
    private static final String UPDATE_QUERY =
            "UPDATE Evaluation SET id_academic = ?, skill_grade = ?, content_grade = ?, writing_grade = ?, requirements_grade = ?, feedback = ? WHERE id_student = ? AND id_project = ?";
    private static final String DELETE_QUERY = "DELETE FROM Evaluation WHERE id_student = ? AND id_project = ?";

    @Override
    public EvaluationDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
        return new EvaluationDTO.EvaluationBuilder()
                .setIDStudent(resultSet.getString("id_student"))
                .setIDProject(resultSet.getInt("id_project"))
                .setIDAcademic(resultSet.getString("id_academic"))
                .setSkillGrade(resultSet.getString("skill_grade"))
                .setContentGrade(resultSet.getString("content_grade"))
                .setWritingGrade(resultSet.getString("writing_grade"))
                .setRequirementsGrade(resultSet.getString("requirements_grade"))
                .setFeedback(resultSet.getString("feedback"))
                .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .build();
    }

    @Override
    public void createOne(EvaluationDTO evaluationDTO) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_QUERY)) {
            stmt.setString(1, evaluationDTO.getIDStudent());
            stmt.setInt(2, evaluationDTO.getIDProject());
            stmt.setString(3, evaluationDTO.getIDAcademic());
            stmt.setInt(4, evaluationDTO.getSkillGrade());
            stmt.setInt(5, evaluationDTO.getContentGrade());
            stmt.setInt(6, evaluationDTO.getWritingGrade());
            stmt.setInt(7, evaluationDTO.getRequirementsGrade());
            stmt.setString(8, evaluationDTO.getFeedback());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<EvaluationDTO> getAll() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_QUERY);
             ResultSet rs = stmt.executeQuery()) {
            List<EvaluationDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(createDTOInstanceFromResultSet(rs));
            }
            return list;
        }
    }

    @Override
    public EvaluationDTO getOne(String idStudent) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_QUERY)) {
            stmt.setString(1, idStudent);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return createDTOInstanceFromResultSet(resultSet);
                }
            }
            return null;
        }
    }

    @Override
    public void updateOne(EvaluationDTO evaluationDTO, String unusedKey) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_QUERY)) {
            stmt.setString(1, evaluationDTO.getIDAcademic());
            stmt.setInt(2, evaluationDTO.getSkillGrade());
            stmt.setInt(3, evaluationDTO.getContentGrade());
            stmt.setInt(4, evaluationDTO.getWritingGrade());
            stmt.setInt(5, evaluationDTO.getRequirementsGrade());
            stmt.setString(6, evaluationDTO.getFeedback());
            stmt.setString(7, evaluationDTO.getIDStudent());
            stmt.setInt(8, evaluationDTO.getIDProject());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteOne(String compositeKey) throws SQLException {
        String[] keys = compositeKey.split("-");
        if (keys.length != 2) {
            throw new IllegalArgumentException("Expected composite key in format 'id_student-id_project'");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY)) {
            stmt.setString(1, keys[0]);
            stmt.setInt(2, Integer.parseInt(keys[1]));
            stmt.executeUpdate();
        }
    }
}
