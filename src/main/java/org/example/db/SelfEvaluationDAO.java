package org.example.db;

import org.example.business.SelfEvaluationDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelfEvaluationDAO extends DBConnector implements DAO<SelfEvaluationDTO> {

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

    private static final String DELETE_QUERY = "DELETE FROM self_evaluation WHERE id_student = ?";

    @Override
    public void create(SelfEvaluationDTO element) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_QUERY)) {

            stmt.setString(1, element.getIdStudent());
            stmt.setInt(2, element.getFollowUpGrade());
            stmt.setInt(3, element.getSafetyGrade());
            stmt.setInt(4, element.getKnowledgeApplicationGrade());
            stmt.setInt(5, element.getInterestingGrade());
            stmt.setInt(6, element.getProductivityGrade());
            stmt.setInt(7, element.getCongruentGrade());
            stmt.setInt(8, element.getInformedByOrganization());
            stmt.setInt(9, element.getRegulatedByOrganization());
            stmt.setInt(10, element.getImportanceForProfessionalDevelopment());
            stmt.setString(11, element.getCreatedAt());

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
                SelfEvaluationDTO dto = new SelfEvaluationDTO.SelfEvaluationBuilder()
                        .setIdStudent(rs.getString("id_student"))
                        .SetFollowUpGrade(rs.getInt("follow_up_grade"))
                        .SetSafetyGrade(rs.getInt("safety_grade"))
                        .setKnowledgeApplicationGrade(rs.getInt("knowledge_application_grade"))
                        .setInterestingGrade(rs.getInt("interesting_grade"))
                        .setProductivityGrade(rs.getInt("productivity_grade"))
                        .setCongruentGrade(rs.getInt("congruent_grade"))
                        .setInformedByOrganization(rs.getInt("informed_by_organization"))
                        .setRegulatedByOrganization(rs.getInt("regulated_by_organization"))
                        .setImportanceForProfessionalDevelopment(rs.getInt("importance_for_professional_development"))
                        .build();

                list.add(dto);
            }
        }

        return list;
    }

    @Override
    public SelfEvaluationDTO get(int id) throws SQLException {
        return null;
    }

    @Override
    public SelfEvaluationDTO get(String id) throws SQLException {
        SelfEvaluationDTO dto = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_QUERY)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dto = new SelfEvaluationDTO.SelfEvaluationBuilder()
                        .setIdStudent(rs.getString("id_student"))
                        .SetFollowUpGrade(rs.getInt("follow_up_grade"))
                        .SetSafetyGrade(rs.getInt("safety_grade"))
                        .setKnowledgeApplicationGrade(rs.getInt("knowledge_application_grade"))
                        .setInterestingGrade(rs.getInt("interesting_grade"))
                        .setProductivityGrade(rs.getInt("productivity_grade"))
                        .setCongruentGrade(rs.getInt("congruent_grade"))
                        .setInformedByOrganization(rs.getInt("informed_by_organization"))
                        .setRegulatedByOrganization(rs.getInt("regulated_by_organization"))
                        .setImportanceForProfessionalDevelopment(rs.getInt("importance_for_professional_development"))
                        .build();
            }
        }

        return dto;
    }

    @Override
    public void update(SelfEvaluationDTO element) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY)) {

            stmt.setInt(1, element.getFollowUpGrade());
            stmt.setInt(2, element.getSafetyGrade());
            stmt.setInt(3, element.getKnowledgeApplicationGrade());
            stmt.setInt(4, element.getInterestingGrade());
            stmt.setInt(5, element.getProductivityGrade());
            stmt.setInt(6, element.getCongruentGrade());
            stmt.setInt(7, element.getInformedByOrganization());
            stmt.setInt(8, element.getRegulatedByOrganization());
            stmt.setInt(9, element.getImportanceForProfessionalDevelopment());
            stmt.setString(10, element.getCreatedAt());
            stmt.setString(11, element.getIdStudent());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
    }

    public void delete(String idStudent) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY)) {

            stmt.setString(1, idStudent);
            stmt.executeUpdate();
        }
    }
}

