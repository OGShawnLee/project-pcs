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
            "INSERT INTO Project (id_project, id_organization, name, methodology, sector) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM project";
    private static final String GET_QUERY = "SELECT * FROM Project WHERE id_project = ?";
    private static final String UPDATE_QUERY =
            "UPDATE project SET name = ?, id_organization = ?, methodology = ? WHERE id_project = ?";
    private static final String DELETE_QUERY = "DELETE FROM project WHERE id_project = ?";

    @Override
    public void create(ProjectDTO element) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(CREATE_QUERY);

        statement.setString(1, element.getId());
        statement.setString(3, element.getName());
        statement.setString(4, element.getMethodology());
        statement.setString(5, element.getState());
        statement.setString(6, element.getSector());
        statement.executeUpdate();

        close();
    }

    @Override
    public List<ProjectDTO> getAll() throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(GET_ALL_QUERY);

        try (ResultSet resultSet = statement.executeQuery()) {
            List<ProjectDTO> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(new ProjectDTO((ProjectDTO.Builder)resultSet));
            }

            close();

            return list;
        }
    }

    @Override
    public ProjectDTO get(int id) throws SQLException {
        return null;
    }

    @Override
    public ProjectDTO get(String id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(GET_QUERY);
        ProjectDTO element = null;

        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            element = new ProjectDTO((ProjectDTO.Builder)resultSet);
        }

        close();

        return element;
    }

    @Override
    public void update(ProjectDTO element) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY);

        statement.setString(1, element.getId());
        statement.setString(3, element.getName());
        statement.setString(4, element.getMethodology());
        statement.setString(5, element.getState());
        statement.setString(6, element.getSector());

        close();
    }

    @Override
    public void delete(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(DELETE_QUERY);

        try (statement) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }

        close();
    }
}

