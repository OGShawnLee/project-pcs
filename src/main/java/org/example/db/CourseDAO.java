package org.example.db;

import org.example.business.CourseDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends DAO<CourseDTO, String> {
    private static final String CREATE_QUERY =
            "INSERT INTO course (nrc, id_academic, section, started_at, ended_at) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM course";
    private static final String GET_QUERY = "SELECT * FROM course WHERE nrc = ?";
    private static final String UPDATE_QUERY =
            "UPDATE course SET id_academic = ?, section = ?, started_at = ?, ended_at = ? WHERE nrc = ?";
    private static final String DELETE_QUERY = "DELETE FROM course WHERE nrc = ?";

    @Override
    public CourseDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
        return new CourseDTO.CourseBuilder()
            .setNrc(resultSet.getString("nrc"))
            .setIdAcademic(resultSet.getString("id_academic"))
            .setSection(resultSet.getString("section"))
            .setStartedAt(resultSet.getString("started_at"))
            .setEndedAt(resultSet.getString("ended_at"))
            .build();
    }

    @Override
    public void create(CourseDTO dataObject) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(CREATE_QUERY);

        statement.setString(1, dataObject.getNrc());
        statement.setString(2, dataObject.getIdAcademic());
        statement.setString(3, dataObject.getSection());
        statement.setString(4, dataObject.getStartedAt());
        statement.setString(5, dataObject.getEndedAt());
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
                list.add(createDTOInstanceFromResultSet(resultSet));
            }
        }
        return list;
    }

    @Override
    public CourseDTO get(String nrc) throws SQLException {
        CourseDTO element = null;

        try(Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(GET_QUERY)) {

            statement.setString(1, nrc);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    element = createDTOInstanceFromResultSet(resultSet);
                }
            }
        }

        return element;
    }

    @Override
    public void update(CourseDTO dataObject) throws SQLException {

        try(Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY)) {

            statement.setString(3, dataObject.getIdAcademic());
            statement.setString(4, dataObject.getSection());
            statement.setString(5, dataObject.getStartedAt());
            statement.setString(6, dataObject.getEndedAt());
            statement.setString(1, dataObject.getNrc());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(String nrc) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(DELETE_QUERY);

        try (statement) {
            statement.setString(1, nrc);
            statement.executeUpdate();
        }

        close();
    }
}


