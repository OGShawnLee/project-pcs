package org.example.db;

import org.example.business.PracticeDTO;
import org.example.db.filter.FilterPractice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PracticeDAO extends DAO<PracticeDTO, FilterPractice> {
  private static final String CREATE_QUERY =
    "INSERT INTO Practice (id_student, id_project, reason_of_assignation) VALUES (?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Practice";
  private static final String GET_QUERY = "SELECT * FROM Practice WHERE id_student = ? AND id_project = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Practice SET id_project = ?, reason_of_assignation = ? WHERE id_student = ? AND id_project = ?";
  private static final String DELETE_QUERY = "DELETE FROM Practice WHERE id_student = ? AND id_project = ?";

  @Override
  protected PracticeDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new PracticeDTO.PracticeBuilder()
      .setIdStudent(resultSet.getString("id_student"))
      .setIdProject(resultSet.getString("id_project"))
      .SetReasonOfAssignation(resultSet.getString("reason_of_assignation"))
      .build();
  }

  @Override
  public void create(PracticeDTO dataObject) throws SQLException {
    try (
      Connection conn = getConnection();
      PreparedStatement statement = conn.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getIdStudent());
      statement.setString(2, dataObject.getIdProject());
      statement.setString(3, dataObject.getReasonOfAssignation());
      statement.executeUpdate();
    }
  }

  @Override
  public List<PracticeDTO> getAll() throws SQLException {
    try (
      Connection conn = getConnection();
      PreparedStatement statement = conn.prepareStatement(GET_ALL_QUERY);
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
  public PracticeDTO get(FilterPractice filter) throws SQLException {
    try (
      Connection conn = getConnection();
      PreparedStatement statement = conn.prepareStatement(GET_QUERY)
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
  public void update(PracticeDTO dataObject) throws SQLException {
    try (
      Connection conn = getConnection();
      PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.getIdProject());
      statement.setString(2, dataObject.getReasonOfAssignation());
      statement.setString(3, dataObject.getIdStudent());
      statement.setString(4, dataObject.getIdProject());
      statement.executeUpdate();
    }
  }

  @Override
  public void delete(FilterPractice filter) throws SQLException {
    try (
      Connection conn = getConnection();
      PreparedStatement statement = conn.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setInt(2, filter.getIDPractice());
      statement.executeUpdate();
    }
  }
}