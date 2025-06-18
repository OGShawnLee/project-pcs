package org.example.business.dao;

import org.example.business.dto.AccountDTO;
import org.example.business.dto.StudentDTO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class StudentDAO extends DAOPattern<StudentDTO, String> {
  private static final String CREATE_QUERY =
    "CALL create_student(?, ?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY =
    "SELECT * FROM Student";
  private static final String GET_QUERY =
    "SELECT * FROM Student WHERE id_student = ?";
  private static final String GET_BY_EMAIL_QUERY =
    "SELECT * FROM Student WHERE email = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Student SET name = ?, paternal_last_name = ?, maternal_last_name = ?, phone_number = ?, state = ?, final_grade = ? WHERE id_student = ?";
  private static final String DELETE_QUERY =
    "DELETE FROM Student WHERE id_student = ?";

  @Override
  StudentDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new StudentDTO.StudentBuilder()
      .setID(resultSet.getString("id_student"))
      .setEmail(resultSet.getString("email"))
      .setName(resultSet.getString("name"))
      .setPaternalLastName(resultSet.getString("paternal_last_name"))
      .setMaternalLastName(resultSet.getString("maternal_last_name"))
      .setState(resultSet.getString("state"))
      .setPhoneNumber(resultSet.getString("phone_number"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .setFinalGrade(resultSet.getInt("final_grade"))
      .build();
  }

  @Override
  public void createOne(StudentDTO studentDTO) throws SQLException {
    try (
      Connection connection = getConnection();
      CallableStatement statement = connection.prepareCall(CREATE_QUERY)
    ) {
      statement.setString(1, studentDTO.getID());
      statement.setString(2, studentDTO.getEmail());
      statement.setString(3, studentDTO.getName());
      statement.setString(4, studentDTO.getPaternalLastName());
      statement.setString(5, studentDTO.getMaternalLastName());
      statement.setString(6, AccountDTO.getGeneratedHashedPassword(studentDTO.getID()));
      statement.setString(7, studentDTO.getPhoneNumber());

      statement.executeUpdate();
    }
  }

  @Override
  public List<StudentDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      ArrayList<StudentDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  public List<StudentDTO> getAllWithNoProject() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(
        "SELECT * FROM Student WHERE id_student NOT IN (SELECT id_student FROM Practice) AND state = 'ACTIVE'"
      );
      ResultSet resultSet = statement.executeQuery()
    ) {
      ArrayList<StudentDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public StudentDTO getOne(String id) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, id);

      StudentDTO studentDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          studentDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return studentDTO;
    }
  }

  public StudentDTO getOneByEmail(String email) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_BY_EMAIL_QUERY)
    ) {
      statement.setString(1, email);

      StudentDTO studentDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          studentDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return studentDTO;
    }
  }

  @Override
  public void updateOne(StudentDTO studentDTO) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, studentDTO.getName());
      statement.setString(2, studentDTO.getPaternalLastName());
      statement.setString(3, studentDTO.getMaternalLastName());
      statement.setString(4, studentDTO.getPhoneNumber());
      statement.setString(5, studentDTO.getState());
      statement.setInt(6, studentDTO.getFinalGrade());
      statement.setString(7, studentDTO.getID());

      statement.executeUpdate();
    }
  }

  @Override
  public void deleteOne(String id) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, id);
      statement.executeUpdate();
    }
  }
}