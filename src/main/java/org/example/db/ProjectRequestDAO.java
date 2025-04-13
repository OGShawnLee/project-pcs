package org.example.db;

import org.example.business.ProjectRequestDTO;
import org.example.db.filter.FilterProject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRequestDAO extends DBConnector implements DAO<ProjectRequestDTO, FilterProject> {
    private static final String CREATE_QUERY =
            "INSERT INTO ProjectRequest (id_student, id_project, state, reason_of_state, created_at) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM ProjectRequest";
    private static final String GET_QUERY = "SELECT * FROM ProjectRequest WHERE id_student = ? AND id_project = ?";
    private static final String UPDATE_QUERY =
            "UPDATE ProjectRequest SET state = ?, reason_of_state = ?, created_at = ? WHERE id_student = ? AND id_project = ?";
    private static final String DELETE_QUERY = "DELETE FROM ProjectRequest WHERE id_student = ? AND id_project = ?";

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
                ProjectRequestDTO dto = new ProjectRequestDTO.ProjectRequestBuilder()
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
    public ProjectRequestDTO get(FilterProject filter) throws SQLException {
        ProjectRequestDTO dto = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_QUERY)) {

            stmt.setString(1, filter.getIDStudent());
            stmt.setInt(2, filter.getIDProject());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dto = new ProjectRequestDTO.ProjectRequestBuilder()
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
    public void delete(FilterProject filter) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY)) {

            stmt.setString(1, filter.getIDStudent());
            stmt.setInt(2, filter.getIDProject());
            stmt.executeUpdate();
        }
    }
}
