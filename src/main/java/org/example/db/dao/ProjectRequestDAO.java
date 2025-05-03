package org.example.db.dao;

import org.example.business.dto.ProjectRequestDTO;
import org.example.db.dao.filter.FilterProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectRequestDAO extends DAOPattern<ProjectRequestDTO, FilterProject> {
  private static final String CREATE_QUERY =
    "INSERT INTO ProjectRequest (id_student, id_project, state, reason_of_state) VALUES (?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM ProjectRequest";
  private static final String GET_QUERY = "SELECT * FROM ProjectRequest WHERE id_student = ? AND id_project = ?";
  private static final String UPDATE_QUERY =
    "UPDATE ProjectRequest SET state = ?, reason_of_state = ? WHERE id_student = ? AND id_project = ?";
  private static final String DELETE_QUERY = "DELETE FROM ProjectRequest WHERE id_student = ? AND id_project = ?";

  @Override
  public ProjectRequestDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new ProjectRequestDTO.ProjectRequestBuilder()
      .setIDStudent(resultSet.getString("id_student"))
      .setIDProject(resultSet.getInt("id_project"))
      .setState(resultSet.getString("state"))
      .setReasonOfState(resultSet.getString("reason_of_state"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(ProjectRequestDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getIDStudent());
      statement.setInt(2, dataObject.getIDProject());
      statement.setString(3, dataObject.getState());
      statement.setString(4, dataObject.getReasonOfState());
      statement.executeUpdate();
    }
  }

  @Override
  public List<ProjectRequestDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<ProjectRequestDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public ProjectRequestDTO getOne(FilterProject filter) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setInt(2, filter.getIDProject());

      ProjectRequestDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    }
  }

  @Override
  public void updateOne(ProjectRequestDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.getState());
      statement.setString(2, dataObject.getReasonOfState());
      statement.setString(3, dataObject.getIDStudent());
      statement.setInt(4, dataObject.getIDProject());
      statement.executeUpdate();
    }
  }

  @Override
  public void deleteOne(FilterProject filter) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, filter.getIDStudent());
      statement.setInt(2, filter.getIDProject());
      statement.executeUpdate();
    }
  }
}