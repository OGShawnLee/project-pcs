package org.example.db;

import org.example.business.PracticeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PracticeDAO extends DBConnector implements DAO<PracticeDTO> {
    private static final String CREATE_QUERY =
            "INSERT INTO practice (id_student, id_project, reason_of_assignation) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM practice";
    private static final String GET_QUERY = "SELECT * FROM course WHERE id_student = ?";
    private static final String UPDATE_QUERY =
            "UPDATE enrollment SET id_project = ?, reason_of_assignation = ? WHERE id_student = ?";
    private static final String DELETE_QUERY = "DELETE FROM practice WHERE id_student = ?";


    @Override
    public void create(PracticeDTO element) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(CREATE_QUERY);

        statement.setString(1, element.getIdStudent());
        statement.setString(2, element.getIdProject());
        statement.setString(3, element.getReasonOfAssignation());
        statement.executeUpdate();

        close();
    }

    @Override
    public List<PracticeDTO> getAll() throws SQLException {
        List<PracticeDTO> list = new ArrayList<>();

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()){

            while (resultSet.next()) {
                PracticeDTO dto = new PracticeDTO.Builder() {
                }
                        .setIdStudent(resultSet.getString("id_student"))
                        .setIdProject(resultSet.getString("id_project"))
                        .SetReasonOfAssignation(resultSet.getString("reason_of_assignation"))
                        .build();
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public PracticeDTO get(int id) throws SQLException {
        return null;
    }

    @Override
    public PracticeDTO get(String id) throws SQLException {
        PracticeDTO dto = null;

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_QUERY)) {
            PracticeDTO element = null;

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    dto = new PracticeDTO.Builder() {
                    }
                            .setIdStudent(resultSet.getString("id_student"))
                            .setIdProject(resultSet.getString("id_project"))
                            .SetReasonOfAssignation(resultSet.getString("reason_of_assignation"))
                            .build();
                }
            }
        }

        return dto;
    }

    @Override
    public void update(PracticeDTO element) throws SQLException {

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, element.getIdStudent());
            statement.setString(2, element.getIdProject());
            statement.setString(3, element.getReasonOfAssignation());
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
