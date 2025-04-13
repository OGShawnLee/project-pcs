package org.example.db;

import org.example.business.ProjectRequestDTO;
import org.example.db.filter.FilterProject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRequestDAO extends DAO<ProjectRequestDTO, FilterProject> {
    private static final String CREATE_QUERY =
            "INSERT INTO ProjectRequest (id_student, id_project, state, reason_of_state, created_at) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM ProjectRequest";
    private static final String GET_QUERY = "SELECT * FROM ProjectRequest WHERE id_student = ? AND id_project = ?";
    private static final String UPDATE_QUERY =
            "UPDATE ProjectRequest SET state = ?, reason_of_state = ?, created_at = ? WHERE id_student = ? AND id_project = ?";
    private static final String DELETE_QUERY = "DELETE FROM ProjectRequest WHERE id_student = ? AND id_project = ?";

    @Override
    public ProjectRequestDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
        return new ProjectRequestDTO.ProjectRequestBuilder()
            .setIdStudent(resultSet.getString("id_student"))
            .setIdProject(resultSet.getString("id_project"))
            .setState(resultSet.getString("state"))
            .setReasonOfState(resultSet.getString("reason_of_state"))
            .setCreatedAt(resultSet.getString("created_at"))
            .build();
    }

    @Override
    public void create(ProjectRequestDTO dataObject) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_QUERY)) {

            stmt.setString(1, dataObject.getIdStudent());
            stmt.setString(2, dataObject.getIdProject());
            stmt.setString(3, dataObject.getState());
            stmt.setString(4, dataObject.getReasonOfState());
            stmt.setString(5, dataObject.getCreatedAt());

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
                list.add(createDTOInstanceFromResultSet(rs));
            }
        }

        return list;
    }

    @Override
    public ProjectRequestDTO get(FilterProject filter) throws SQLException {
        ProjectRequestDTO element = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_QUERY)) {

            stmt.setString(1, filter.getIDStudent());
            stmt.setInt(2, filter.getIDProject());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                element = createDTOInstanceFromResultSet(rs);
            }
        }

        return element;
    }

    @Override
    public void update(ProjectRequestDTO dataObject) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY)) {

            stmt.setString(1, dataObject.getState());
            stmt.setString(2, dataObject.getReasonOfState());
            stmt.setString(3, dataObject.getCreatedAt());
            stmt.setString(4, dataObject.getIdStudent());
            stmt.setString(5, dataObject.getIdProject());

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
