package org.example.db;

import org.example.business.CourseDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends DBConnector implements DAO<CourseDTO> {
    private static final String CREATE_QUERY =
            "INSERT INTO course (nrc, id_academic, section, started_at, ended_at) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM course";
    private static final String GET_QUERY = "SELECT * FROM course WHERE nrc = ?";
    private static final String UPDATE_QUERY =
            "UPDATE course SET id_academic = ?, section = ?, started_at = ?, ended_at = ? WHERE nrc = ?";
    private static final String DELETE_QUERY = "DELETE FROM course WHERE nrc = ?";


    @Override
    public void create(CourseDTO element) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(CREATE_QUERY);

        statement.setString(1, element.getNrc());
        statement.setString(2, element.getIdAcademic());
        statement.setString(3, element.getSection());
        statement.setString(4, element.getStartedAt());
        statement.setString(5, element.getEndedAt());
        statement.executeUpdate();

        close();
    }

    @Override
    public List<CourseDTO> getAll() throws SQLException {
        List<CourseDTO> list = new ArrayList<>();

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()){

            while (resultSet.next()) {
                CourseDTO dto = new CourseDTO.Builder() {
                }
                .setNrc(resultSet.getString("nrc"))
                .setIdAcademic(resultSet.getString("id_academic"))
                .setSection(resultSet.getString("section"))
                .setStartedAt(resultSet.getString("started_at"))
                .setEndedAt(resultSet.getString("ended_at"))
                .build();
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public CourseDTO get(int id) throws SQLException {
        return null;
    }

    @Override
    public CourseDTO get(String id) throws SQLException {
        CourseDTO dto = null;

        try(Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(GET_QUERY)) {
            CourseDTO element = null;

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    dto = new CourseDTO.Builder() {
                    }
                    .setNrc(resultSet.getString("nrc"))
                    .setIdAcademic(resultSet.getString("id_academic"))
                    .setSection(resultSet.getString("section"))
                    .setStartedAt(resultSet.getString("started_at"))
                    .setEndedAt(resultSet.getString("ended_at"))
                    .build();
                }
            }
        }

        return dto;
    }

    @Override
    public void update(CourseDTO element) throws SQLException {

        try(Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY)) {

            statement.setString(3, element.getIdAcademic());
            statement.setString(4, element.getSection());
            statement.setString(5, element.getStartedAt());
            statement.setString(6, element.getEndedAt());
            statement.setString(1, element.getNrc());
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


