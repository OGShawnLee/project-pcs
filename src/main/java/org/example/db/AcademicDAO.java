package org.example.db;

import org.example.business.AcademicDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcademicDAO extends DBConnector implements DAO<AcademicDTO, String> {
    private static final String CREATE_QUERY =
            "INSERT INTO Academic (id_academic, email, name, paternal_last_name, maternal_last_name, role) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM Academic";
    private static final String GET_QUERY = "SELECT * FROM Academic WHERE id_academic = ?";
    private static final String UPDATE_QUERY =
            "UPDATE Academic SET name = ?, paternal_last_name = ?, maternal_last_name = ? WHERE id_academic = ?";
    private static final String DELETE_QUERY = "DELETE FROM Academic WHERE id_academic = ?";

    @Override
    public void create(AcademicDTO element) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(CREATE_QUERY);

        statement.setString(1, element.getID());
        statement.setString(2, element.getEmail());
        statement.setString(3, element.getName());
        statement.setString(4, element.getPaternalLastName());
        statement.setString(5, element.getMaternalLastName());
        statement.setString(6, element.getRole());
        statement.executeUpdate();

        close();
    }

    @Override
    public List<AcademicDTO> getAll() throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(GET_ALL_QUERY);

        try (ResultSet resultSet = statement.executeQuery()) {
            List<AcademicDTO> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(new AcademicDTO(resultSet));
            }

            close();

            return list;
        }
    }

    @Override
    public AcademicDTO get(String id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(GET_QUERY);
        AcademicDTO element = null;

        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            element = new AcademicDTO(resultSet);
        }

        close();

        return element;
    }

    @Override
    public void update(AcademicDTO element) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY);

        try (statement) {
            statement.setString(1, element.getName());
            statement.setString(2, element.getPaternalLastName());
            statement.setString(3, element.getMaternalLastName());
            statement.setString(4, element.getID());
            statement.executeUpdate();
        }

        close();
    }

    @Override
    public void delete(String id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(DELETE_QUERY);

        try (statement) {
            statement.setString(1, id);
            statement.executeUpdate();
        }

        close();
    }
}


