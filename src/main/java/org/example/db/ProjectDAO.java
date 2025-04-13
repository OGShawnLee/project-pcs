package org.example.db;

import org.example.business.ProjectDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO extends DAO<ProjectDTO, Integer> {
  private static final String CREATE_QUERY =
    "INSERT INTO Project (id_project, id_organization, name, methodology, state, sector) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Project";
  private static final String GET_QUERY = "SELECT * FROM Project WHERE id_project = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Project SET id_organization = ?, name = ?, methodology = ?, state = ?, sector = ? WHERE id_project = ?";
  private static final String DELETE_QUERY = "DELETE FROM Project WHERE id_project = ?";

  @Override
  protected ProjectDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new ProjectDTO.ProjectBuilder()
      .setId(resultSet.getString("id_project"))
      .setIdOrganization(resultSet.getString("id_organization"))
      .setName(resultSet.getString("name"))
      .setMethodology(resultSet.getString("methodology"))
      .setState(resultSet.getString("state"))
      .setSector(resultSet.getString("sector"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void create(ProjectDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getId());
      statement.setString(2, dataObject.getIdOrganization());
      statement.setString(3, dataObject.getName());
      statement.setString(4, dataObject.getMethodology());
      statement.setString(5, dataObject.getState());
      statement.setString(6, dataObject.getSector());
      statement.executeUpdate();
    }
  }

  @Override
  public List<ProjectDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<ProjectDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public ProjectDTO get(Integer id) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setInt(1, id);

      ProjectDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    }
  }

  @Override
  public void update(ProjectDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.getIdOrganization());
      statement.setString(2, dataObject.getName());
      statement.setString(3, dataObject.getMethodology());
      statement.setString(4, dataObject.getState());
      statement.setString(5, dataObject.getSector());
      statement.setString(6, dataObject.getId());
      statement.executeUpdate();
    }
  }

  @Override
  public void delete(Integer id) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setInt(1, id);
      statement.executeUpdate();
    }
  }
}