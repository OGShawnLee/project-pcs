package org.example.db;

import org.example.business.EnrollmentDTO;
import org.example.db.filter.FilterEnrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO extends DAO<EnrollmentDTO, FilterEnrollment> {
    private static final String CREATE_QUERY =
            "INSERT INTO Enrollment (id_course, id_student) VALUES (?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM Enrollment";
    private static final String GET_QUERY = "SELECT * FROM Enrollment WHERE id_student = ? and id_course = ?";
    private static final String UPDATE_QUERY =
            "UPDATE Enrollment SET id_student = ?, created_at = ? WHERE id_student = ? and id_course = ?";
    private static final String DELETE_QUERY = "DELETE FROM Enrollment WHERE id_student = ? and id_course = ?";

    @Override
    protected EnrollmentDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
        return new EnrollmentDTO.EnrollmentBuilder()
            .setIdCourse(resultSet.getString("id_course"))
            .setIdStudent(resultSet.getString("id_student"))
            .setCreatedAt(resultSet.getString("created_at"))
            .build();
    }

    @Override
    public void create(EnrollmentDTO dataObject) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(CREATE_QUERY);

        statement.setString(1, dataObject.getIdCourse());
        statement.setString(2, dataObject.getIdStudent());
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
                list.add(createDTOInstanceFromResultSet(resultSet));
            }
        }

        return list;
    }

    @Override
    public EnrollmentDTO get(FilterEnrollment filter) throws SQLException {
        EnrollmentDTO element = null;

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_QUERY)) {

            statement.setString(1, filter.getIDStudent());
            statement.setString(2, filter.getIDCourse());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    element = createDTOInstanceFromResultSet(resultSet);
                }
            }
        }

        return element;
    }

    @Override
    public void update(EnrollmentDTO dataObject) throws SQLException {

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, dataObject.getIdStudent());
            statement.setString(2, dataObject.getCreatedAt());
            statement.setString(3, dataObject.getIdCourse());
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
