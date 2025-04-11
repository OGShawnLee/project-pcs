package org.example.db;

import org.example.business.OrganizationDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO extends DBConnector implements DAO<OrganizationDTO> {
    private static final String CREATE_QUERY =
            "INSERT INTO organization (email, name, representative_fullname, colony, street, state) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM organization";
    private static final String GET_QUERY = "SELECT * FROM organization WHERE name = ?";
    private static final String UPDATE_QUERY =
            "UPDATE organization SET email = ?, representative_fullname = ?, colony = ?, street = ?, state = ? WHERE name = ?";
    private static final String DELETE_QUERY = "DELETE FROM organization WHERE name = ?";


    @Override
    public void create(OrganizationDTO element) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(CREATE_QUERY);

        statement.setString(1, element.getEmail());
        statement.setString(2, element.getName());
        statement.setString(3, element.getRepresentativeFullName());
        statement.setString(4, element.getColony());
        statement.setString(5, element.getStreet());
        statement.setString(6, element.getState());
        statement.setString(7, element.getCreatedAt());
        statement.executeUpdate();

        close();
    }

    @Override
    public List<OrganizationDTO> getAll() throws SQLException {
        List<OrganizationDTO> list = new ArrayList<>();

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()){

            while (resultSet.next()) {
                OrganizationDTO dto = new OrganizationDTO.Builder() {
                }
                        .setEmail(resultSet.getString("email"))
                        .setName(resultSet.getString("name"))
                        .setRepresentativeFullName(resultSet.getString("representative_fullname"))
                        .setColony(resultSet.getString("colony"))
                        .setStreet(resultSet.getString("street"))
                        .setState(resultSet.getString("state"))
                        .setCreatedAt(resultSet.getString("created_at"))
                        .build();
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public OrganizationDTO get(int id) throws SQLException {
        return null;
    }

    @Override
    public OrganizationDTO get(String id) throws SQLException {
        OrganizationDTO dto = null;

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_QUERY)) {
            OrganizationDTO element = null;

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    dto = new OrganizationDTO.Builder() {
                    }
                            .setEmail(resultSet.getString("email"))
                            .setName(resultSet.getString("name"))
                            .setRepresentativeFullName(resultSet.getString("representative_fullname"))
                            .setColony(resultSet.getString("colony"))
                            .setStreet(resultSet.getString("street"))
                            .setState(resultSet.getString("state"))
                            .setCreatedAt(resultSet.getString("created_at"))
                            .build();
                }
            }
        }

        return dto;
    }

    @Override
    public void update(OrganizationDTO element) throws SQLException {

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, element.getEmail());
            statement.setString(2, element.getRepresentativeFullName());
            statement.setString(3, element.getColony());
            statement.setString(4, element.getStreet());
            statement.setString(5, element.getState());
            statement.setString(6, element.getCreatedAt());
            statement.setString(7, element.getName());
            statement.executeUpdate();
        }
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
