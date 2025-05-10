package org.example.business.dao;

import org.example.business.dto.PracticeDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dto.StudentPracticeDTO;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentPracticeDAO {
  private static final String GET_ALL_BY_PROJECT_ID = "SELECT * FROM StudentPractice WHERE id_project = ?";

  private static StudentPracticeDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new StudentPracticeDTO(
      new StudentDTO.StudentBuilder()
        .setID(resultSet.getString("id_student"))
        .setEmail(resultSet.getString("email"))
        .setName(resultSet.getString("name"))
        .setPaternalLastName(resultSet.getString("paternal_last_name"))
        .setMaternalLastName(resultSet.getString("maternal_last_name"))
        .setState(resultSet.getString("state"))
        .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
        .setFinalGrade(resultSet.getInt("final_grade"))
        .build(),
      new PracticeDTO.PracticeBuilder()
        .setIDStudent(resultSet.getString("id_student"))
        .setIDProject(resultSet.getInt("id_project"))
        .setReasonOfAssignation(resultSet.getString("reason_of_assignation"))
        .build()
    );
  }

  public static List<StudentPracticeDTO> getAllByProjectID(int idProject) throws SQLException {
    try (
      Connection connection = DBConnector.getConnection();
      PreparedStatement statement = connection.prepareStatement(
        GET_ALL_BY_PROJECT_ID);
    ) {
      statement.setInt(1, idProject);

      try (ResultSet resultSet = statement.executeQuery()) {
        List<StudentPracticeDTO> list = new ArrayList<>();

        while (resultSet.next()) {
          list.add(createDTOInstanceFromResultSet(resultSet));
        }

        return list;
      }
    }
  }
}
