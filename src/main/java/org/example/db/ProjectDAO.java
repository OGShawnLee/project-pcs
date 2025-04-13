package org.example.db;

import org.example.business.ProjectDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO extends DAO<ProjectDTO, Integer> {
    private static final String CREATE_QUERY =
            "INSERT INTO Project (id_project, id_organization, name, methodology, state, sector) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM Project";
    private static final String GET_QUERY = "SELECT * FROM Project WHERE id_project = ?";
    private static final String UPDATE_QUERY =
            "UPDATE Project SET id_organization = ?, name = ?, methodology = ?, state = ?, sector = ? WHERE id_project = ?";
    private static final String DELETE_QUERY = "DELETE FROM Project WHERE id_project = ?";

    @Override
    protected ProjectDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
        return new ProjectDTO.PracticeBuilder()
            .setId(resultSet.getString("id_project"))
            .setIdOrganization(resultSet.getString("id_organization"))
            .setName(resultSet.getString("name"))
            .setMethodology(resultSet.getString("methodology"))
            .setState(resultSet.getString("state"))
            .setSector(resultSet.getString("sector"))
            .build();
    }

    @Override
    public void create(ProjectDTO dataObject) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(CREATE_QUERY)) {

            statement.setString(1, dataObject.getId());
            statement.setString(2, dataObject.getIdOrganization());
            statement.setString(3, dataObject.getName());
            statement.setString(4, dataObject.getMethodology());
            statement.setString(5, dataObject.getState());
            statement.setString(6, dataObject.getSector());

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
                list.add(createDTOInstanceFromResultSet(resultSet));
            }
        }

        return list;
    }

    @Override
    public ProjectDTO get(Integer id) throws SQLException {
        ProjectDTO element = null;

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_QUERY)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    element = createDTOInstanceFromResultSet(resultSet);
                }
            }
        }

        return element;
    }

    @Override
    public void update(ProjectDTO dataObject) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, dataObject.getIdOrganization());
            statement.setString(2, dataObject.getName());
            statement.setString(3, dataObject.getMethodology());
            statement.setString(4, dataObject.getState());
            statement.setString(5, dataObject.getSector());
            statement.setString(6, dataObject.getId());

            statement.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(DELETE_QUERY)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
