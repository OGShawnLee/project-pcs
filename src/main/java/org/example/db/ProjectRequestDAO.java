package org.example.db;

import org.example.business.ProjectRequestDTO;
import org.example.db.filter.FilterProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectRequestDAO extends DAO<ProjectRequestDTO, FilterProject> {
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
      .setIdStudent(resultSet.getString("id_student"))
      .setIdProject(resultSet.getString("id_project"))
      .setState(resultSet.getString("state"))
      .setReasonOfState(resultSet.getString("reason_of_state"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void create(ProjectRequestDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getIdStudent());
      statement.setString(2, dataObject.getIdProject());
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
  public ProjectRequestDTO get(FilterProject filter) throws SQLException {
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
  public void update(ProjectRequestDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.getState());
      statement.setString(2, dataObject.getReasonOfState());
      statement.setString(3, dataObject.getIdStudent());
      statement.setString(4, dataObject.getIdProject());
      statement.executeUpdate();
    }
  }

  @Override
  public void delete(FilterProject filter) throws SQLException {
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