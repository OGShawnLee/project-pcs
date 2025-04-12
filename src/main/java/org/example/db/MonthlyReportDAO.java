package org.example.db;

import org.example.business.MonthlyReportDTO;
import org.example.db.DAO;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MonthlyReportDAO extends DBConnector implements DAO<MonthlyReportDTO> {
    private static final String CREATE_QUERY =
            "INSERT INTO monthly_report (id_project, id_student, month, year, worked_hours, created_at) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM monthly_report";
    private static final String GET_QUERY = "SELECT * FROM monthly_report WHERE id_student = ?";
    private static final String UPDATE_QUERY =
            "UPDATE monthly_report SET month = ?, year = ?, worked_hours = ? WHERE id_student = ? ";
    private static final String DELETE_QUERY = "DELETE FROM monthly_report WHERE id_student = ?";


    @Override
    public void create(MonthlyReportDTO element) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(CREATE_QUERY);

        statement.setString(1, element.getIdProject());
        statement.setString(2, element.getIdStudent());
        statement.setInt(3, element.getMonth());
        statement.setInt(4, element.getYear());
        statement.setInt(5, element.getWorkedHours());
        statement.setString(6, element.getCreatedAt());
        statement.executeUpdate();

        close();
    }

    @Override
    public List<MonthlyReportDTO> getAll() throws SQLException {
        List<MonthlyReportDTO> list = new ArrayList<>();

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()){

            while (resultSet.next()) {
                MonthlyReportDTO dto = new MonthlyReportDTO.MonthlyReportBuilder() {
                }
                        .setIdProject(resultSet.getString("id_project"))
                        .setIdStudent(resultSet.getString("id_student"))
                        .setMonth(resultSet.getInt("month"))
                        .setYear(resultSet.getInt("year"))
                        .setWorkedHours(resultSet.getInt("worked_hours"))
                        .setCreatedAt(resultSet.getString("created_at"))
                        .build();
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public MonthlyReportDTO get(int id) throws SQLException {
        return null;
    }

    @Override
    public MonthlyReportDTO get(String id) throws SQLException {
        MonthlyReportDTO dto = null;

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_QUERY)) {
            MonthlyReportDTO element = null;

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    dto = new MonthlyReportDTO.MonthlyReportBuilder() {
                    }
                            .setIdProject(resultSet.getString("id_project"))
                            .setIdStudent(resultSet.getString("id_student"))
                            .setMonth(resultSet.getInt("month"))
                            .setYear(resultSet.getInt("year"))
                            .setWorkedHours(resultSet.getInt("worked_hours"))
                            .setCreatedAt(resultSet.getString("created_at"))
                            .build();
                }
            }
        }

        return dto;
    }

    @Override
    public void update(MonthlyReportDTO element) throws SQLException {

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, element.getIdProject());
            statement.setString(2, element.getIdStudent());
            statement.setInt(3, element.getMonth());
            statement.setInt(4, element.getYear());
            statement.setInt(5, element.getWorkedHours());
            statement.setString(6, element.getCreatedAt());
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
