package org.example.db;

import org.example.business.EnrollmentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO extends DBConnector implements DAO<EnrollmentDTO> {
    private static final String CREATE_QUERY =
            "INSERT INTO course (nrc, id_academic, section, started_at, ended_at) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM course";
    private static final String GET_QUERY = "SELECT * FROM course WHERE nrc = ?";
    private static final String UPDATE_QUERY =
            "UPDATE course SET id_academic = ?, section = ?, started_at = ?, ended_at = ? WHERE nrc = ?";
    private static final String DELETE_QUERY = "DELETE FROM course WHERE nrc = ?";


    @Override
    public void create(EnrollmentDTO element) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(CREATE_QUERY);

        statement.setString(1, element.getIdCourse());
        statement.setString(2, element.getIdStudent());
        statement.setString(3, element.getCreatedAt());
        statement.executeUpdate();

        close();
    }

    @Override
    public List<EnrollmentDTO> getAll() throws SQLException {
        List<EnrollmentDTO> list = new ArrayList<>();

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()){

            while (resultSet.next()) {
                EnrollmentDTO dto = new EnrollmentDTO.Builder() {
                }
                        .setIdCourse(resultSet.getString("id_course"))
                        .setIdStudent(resultSet.getString("id_student"))
                        .setCreatedAt(resultSet.getString("created_at"))
                        .build();
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public EnrollmentDTO get(int id) throws SQLException {
        return null;
    }

    @Override
    public EnrollmentDTO get(String id) throws SQLException {
        EnrollmentDTO dto = null;

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_QUERY)) {
            EnrollmentDTO element = null;

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    dto = new EnrollmentDTO.Builder() {
                    }
                            .setIdCourse(resultSet.getString("id_course"))
                            .setIdStudent(resultSet.getString("id_student"))
                            .setCreatedAt(resultSet.getString("created_at"))
                            .build();
                }
            }
        }

        return dto;
    }

    @Override
    public void update(EnrollmentDTO element) throws SQLException {

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, element.getIdStudent());
            statement.setString(2, element.getCreatedAt());
            statement.setString(3, element.getIdCourse());
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
