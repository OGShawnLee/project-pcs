package org.example.db;

import org.example.business.ProjectRequestDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRequestDAO extends DBConnector implements DAO<ProjectRequestDTO> {

    private static final String CREATE_QUERY =
            "INSERT INTO project_request (id_student, id_project, state, reason_of_state, created_at) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY = "SELECT * FROM project_request";

    private static final String GET_QUERY = "SELECT * FROM project_request WHERE id_student = ? AND id_project = ?";

    private static final String UPDATE_QUERY =
            "UPDATE project_request SET state = ?, reason_of_state = ?, created_at = ? WHERE id_student = ? AND id_project = ?";

    private static final String DELETE_QUERY = "DELETE FROM project_request WHERE id_student = ? AND id_project = ?";

    @Override
    public void create(ProjectRequestDTO element) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_QUERY)) {

            stmt.setString(1, element.getIdStudent());
            stmt.setString(2, element.getIdProject());
            stmt.setString(3, element.getState());
            stmt.setString(4, element.getReasonOfState());
            stmt.setString(5, element.getCreatedAt());

            stmt.executeUpdate();
        }
    }

    @Override
    public List<ProjectRequestDTO> getAll() throws SQLException {
        List<ProjectRequestDTO> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ProjectRequestDTO dto = new ProjectRequestDTO.Builder()
                        .setIdStudent(rs.getString("id_student"))
                        .setIdProject(rs.getString("id_project"))
                        .setState(rs.getString("state"))
                        .setReasonOfState(rs.getString("reason_of_state"))
                        .setCreatedAt(rs.getString("created_at"))
                        .build();

                list.add(dto);
            }
        }

        return list;
    }

    @Override
    public ProjectRequestDTO get(int id) throws SQLException {
        return null;
    }

    public ProjectRequestDTO get(String idStudent, String idProject) throws SQLException {
        ProjectRequestDTO dto = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_QUERY)) {

            stmt.setString(1, idStudent);
            stmt.setString(2, idProject);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dto = new ProjectRequestDTO.Builder()
                        .setIdStudent(rs.getString("id_student"))
                        .setIdProject(rs.getString("id_project"))
                        .setState(rs.getString("state"))
                        .setReasonOfState(rs.getString("reason_of_state"))
                        .setCreatedAt(rs.getString("created_at"))
                        .build();
            }
        }

        return dto;
    }

    @Override
    public ProjectRequestDTO get(String id) throws SQLException {
        return null;
    }

    @Override
    public void update(ProjectRequestDTO element) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY)) {

            stmt.setString(1, element.getState());
            stmt.setString(2, element.getReasonOfState());
            stmt.setString(3, element.getCreatedAt());
            stmt.setString(4, element.getIdStudent());
            stmt.setString(5, element.getIdProject());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
    }

    public void delete(String idStudent, String idProject) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY)) {

            stmt.setString(1, idStudent);
            stmt.setString(2, idProject);
            stmt.executeUpdate();
        }
    }
}
