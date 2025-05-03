package org.example.db.dao;

import org.example.business.dto.PracticeDTO;
import org.example.db.dao.filter.FilterPractice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PracticeDAO extends DAOPattern<PracticeDTO, FilterPractice> {
  private static final String CREATE_QUERY =
    "INSERT INTO Practice (id_student, id_project, reason_of_assignation) VALUES (?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Practice";
  private static final String GET_QUERY = "SELECT * FROM Practice WHERE id_student = ? AND id_project = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Practice SET reason_of_assignation = ? WHERE id_student = ? AND id_project = ?";
  private static final String DELETE_QUERY = "DELETE FROM Practice WHERE id_student = ? AND id_project = ?";

  @Override
  protected PracticeDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new PracticeDTO.PracticeBuilder()
      .setIDStudent(resultSet.getString("id_student"))
      .setIDProject(resultSet.getInt("id_project"))
      .setReasonOfAssignation(resultSet.getString("reason_of_assignation"))
      .build();
  }

  @Override
  public void createOne(PracticeDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getIDStudent());
      statement.setInt(2, dataObject.getIDProject());
      statement.setString(3, dataObject.getReasonOfAssignation());
      statement.executeUpdate();
    }
  }

  @Override
  public List<PracticeDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<PracticeDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public PracticeDTO getOne(FilterPractice filter) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setInt(2, filter.getIDPractice());

      PracticeDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    }
  }

  @Override
  public void updateOne(PracticeDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.getReasonOfAssignation());
      statement.setString(2, dataObject.getIDStudent());
      statement.setInt(3, dataObject.getIDProject());
      statement.executeUpdate();
    }
  }

  @Override
  public void deleteOne(FilterPractice filter) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setInt(2, filter.getIDPractice());
      statement.executeUpdate();
    }
  }
}