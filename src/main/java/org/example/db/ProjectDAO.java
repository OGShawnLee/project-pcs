package org.example.db;

import org.example.business.ProjectDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO extends DAOPattern<ProjectDTO, Integer> {
  private static final String CREATE_QUERY =
    "INSERT INTO Project (id_organization, name, methodology, sector) VALUES (?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Project";
  private static final String GET_QUERY = "SELECT * FROM Project WHERE id_project = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Project SET id_organization = ?, name = ?, methodology = ?, state = ?, sector = ? WHERE id_project = ?";
  private static final String DELETE_QUERY = "DELETE FROM Project WHERE id_project = ?";

  @Override
  protected ProjectDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new ProjectDTO.ProjectBuilder()
      .setID(resultSet.getInt("id_project"))
      .setIDOrganization(resultSet.getString("id_organization"))
      .setName(resultSet.getString("name"))
      .setMethodology(resultSet.getString("methodology"))
      .setState(resultSet.getString("state"))
      .setSector(resultSet.getString("sector"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(ProjectDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)
    ) {
      statement.setString(1, dataObject.getIDOrganization());
      statement.setString(2, dataObject.getName());
      statement.setString(3, dataObject.getMethodology());
      statement.setString(4, dataObject.getSector());
      statement.executeUpdate();

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          dataObject.setID(generatedKeys.getInt(1));
        } else {
          throw new SQLException("Creating project failed, no ID obtained.");
        }
      }
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
  public ProjectDTO getOne(Integer id) throws SQLException {
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
  public void updateOne(ProjectDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.getIDOrganization());
      statement.setString(2, dataObject.getName());
      statement.setString(3, dataObject.getMethodology());
      statement.setString(4, dataObject.getState());
      statement.setString(5, dataObject.getSector());
      statement.setInt(6, dataObject.getID());
      statement.executeUpdate();
    }
  }

  @Override
  public void deleteOne(Integer id) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setInt(1, id);
      statement.executeUpdate();
    }
  }
}