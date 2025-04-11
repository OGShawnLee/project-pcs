package org.example.db;

import org.example.business.ProjectDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO extends DBConnector implements DAO<ProjectDTO> {

    private static final String CREATE_QUERY =
            "INSERT INTO project (id_project, id_organization, name, methodology, state, sector) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY = "SELECT * FROM project";

    private static final String GET_QUERY = "SELECT * FROM project WHERE id_project = ?";

    private static final String UPDATE_QUERY =
            "UPDATE project SET id_organization = ?, name = ?, methodology = ?, state = ?, sector = ? WHERE id_project = ?";

    private static final String DELETE_QUERY = "DELETE FROM project WHERE id_project = ?";

    @Override
    public void create(ProjectDTO element) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(CREATE_QUERY)) {

            statement.setString(1, element.getId());
            statement.setString(2, element.getIdOrganization());
            statement.setString(3, element.getName());
            statement.setString(4, element.getMethodology());
            statement.setString(5, element.getState());
            statement.setString(6, element.getSector());

            statement.executeUpdate();
        }
    }

    @Override
    public List<ProjectDTO> getAll() throws SQLException {
        List<ProjectDTO> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ProjectDTO dto = new ProjectDTO.Builder()
                        .setId(resultSet.getString("id_project"))
                        .setIdOrganization(resultSet.getString("id_organization"))
                        .setName(resultSet.getString("name"))
                        .setMethodology(resultSet.getString("methodology"))
                        .setState(resultSet.getString("state"))
                        .setSector(resultSet.getString("sector"))
                        .build();

                list.add(dto);
            }
        }

        return list;
    }

    @Override
    public ProjectDTO get(int id) throws SQLException {
        return null;
    }

    @Override
    public ProjectDTO get(String id) throws SQLException {
        ProjectDTO dto = null;

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_QUERY)) {

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    dto = new ProjectDTO.Builder()
                            .setId(resultSet.getString("id_project"))
                            .setIdOrganization(resultSet.getString("id_organization"))
                            .setName(resultSet.getString("name"))
                            .setMethodology(resultSet.getString("methodology"))
                            .setState(resultSet.getString("state"))
                            .setSector(resultSet.getString("sector"))
                            .build();
                }
            }
        }

        return dto;
    }

    @Override
    public void update(ProjectDTO element) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, element.getIdOrganization());
            statement.setString(2, element.getName());
            statement.setString(3, element.getMethodology());
            statement.setString(4, element.getState());
            statement.setString(5, element.getSector());
            statement.setString(6, element.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
    }

    public void delete(String idProject) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(DELETE_QUERY)) {

            statement.setString(1, idProject);
            statement.executeUpdate();
        }
    }
}
