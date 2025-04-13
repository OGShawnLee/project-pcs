package org.example.db;

import org.example.business.EnrollmentDTO;
import org.example.db.filter.FilterEnrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO extends DBConnector implements DAO<EnrollmentDTO, FilterEnrollment> {
    private static final String CREATE_QUERY =
            "INSERT INTO Enrollment (id_course, id_student) VALUES (?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM Enrollment";
    private static final String GET_QUERY = "SELECT * FROM Enrollment WHERE id_student = ? and id_course = ?";
    private static final String UPDATE_QUERY =
            "UPDATE Enrollment SET id_student = ?, created_at = ? WHERE id_student = ? and id_course = ?";
    private static final String DELETE_QUERY = "DELETE FROM Enrollment WHERE id_student = ? and id_course = ?";


    @Override
    public void create(EnrollmentDTO element) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(CREATE_QUERY);

        statement.setString(1, element.getIdCourse());
        statement.setString(2, element.getIdStudent());
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
                EnrollmentDTO dto = new EnrollmentDTO.EnrollmentBuilder() {
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
    public EnrollmentDTO get(FilterEnrollment filter) throws SQLException {
        EnrollmentDTO dto = null;

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_QUERY)) {
            EnrollmentDTO element = null;

            statement.setString(1, filter.getIDStudent());
            statement.setString(2, filter.getIDCourse());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    dto = new EnrollmentDTO.EnrollmentBuilder() {
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
    public void delete(FilterEnrollment filter) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(DELETE_QUERY);

        try (statement) {
            statement.setString(1, filter.getIDStudent());
            statement.setString(2, filter.getIDCourse());
            statement.executeUpdate();
        }

        close();
    }
}
