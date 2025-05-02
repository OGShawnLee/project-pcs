package org.example.db.dao;

import org.example.business.dto.EvaluationDTO;
import org.example.db.Common;

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
    public EvaluationDTO createDTOInstanceFromResultSet(ResultSet rs) throws SQLException {
        return new EvaluationDTO.EvaluationBuilder()
                .setIDStudent(rs.getString("id_student"))
                .setIDProject(rs.getInt("id_project"))
                .setIDAcademic(rs.getString("id_academic"))
                .setSkillGrade(rs.getInt("skill_grade"))
                .setContentGrade(rs.getInt("content_grade"))
                .setWritingGrade(rs.getInt("writing_grade"))
                .setRequirementsGrade(rs.getInt("requirements_grade"))
                .setFeedback(rs.getString("feedback"))
                .setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime())
                .build();
    }

    @Override
    public void createOne(EvaluationDTO dto) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_QUERY)) {
            stmt.setString(1, dto.getIDStudent());
            stmt.setInt(2, dto.getIDProject());
            stmt.setString(3, dto.getIDAcademic());
            stmt.setInt(4, dto.getSkillGrade());
            stmt.setInt(5, dto.getContentGrade());
            stmt.setInt(6, dto.getWritingGrade());
            stmt.setInt(7, dto.getRequirementsGrade());
            stmt.setString(8, dto.getFeedback());
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
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_QUERY)) {
            stmt.setString(1, idStudent);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createDTOInstanceFromResultSet(rs);
                }
            }
            return null;
        }
    }

    @Override
    public void updateOne(EvaluationDTO dto, String unusedKey) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY)) {
            stmt.setString(1, dto.getIDAcademic());
            stmt.setInt(2, dto.getSkillGrade());
            stmt.setInt(3, dto.getContentGrade());
            stmt.setInt(4, dto.getWritingGrade());
            stmt.setInt(5, dto.getRequirementsGrade());
            stmt.setString(6, dto.getFeedback());
            stmt.setString(7, dto.getIDStudent());
            stmt.setInt(8, dto.getIDProject());
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
