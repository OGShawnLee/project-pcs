package org.example.db;

import org.example.business.OrganizationDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO extends DAOPattern<OrganizationDTO, String> {
  private static final String CREATE_QUERY =
    "INSERT INTO Organization (email, name, representative_full_name, colony, street) VALUES (?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Organization";
  private static final String GET_QUERY = "SELECT * FROM Organization WHERE email = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Organization SET name = ?, representative_full_name = ?, colony = ?, street = ?, state = ? WHERE email = ?";
  private static final String DELETE_QUERY = "DELETE FROM Organization WHERE email = ?";

  @Override
  protected OrganizationDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new OrganizationDTO.OrganizationBuilder()
      .setEmail(resultSet.getString("email"))
      .setName(resultSet.getString("name"))
      .setRepresentativeFullName(resultSet.getString("representative_full_name"))
      .setColony(resultSet.getString("colony"))
      .setStreet(resultSet.getString("street"))
      .setState(resultSet.getString("state"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(OrganizationDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, dataObject.getEmail());
      statement.setString(2, dataObject.getName());
      statement.setString(3, dataObject.getRepresentativeFullName());
      statement.setString(4, dataObject.getColony());
      statement.setString(5, dataObject.getStreet());
      statement.executeUpdate();
    }
  }

  @Override
  public List<OrganizationDTO> getAll() throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<OrganizationDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  @Override
  public OrganizationDTO getOne(String email) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, email);

      OrganizationDTO dataObject = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          dataObject = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return dataObject;
    }
  }

  @Override
  public void updateOne(OrganizationDTO dataObject) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, dataObject.getName());
      statement.setString(2, dataObject.getRepresentativeFullName());
      statement.setString(3, dataObject.getColony());
      statement.setString(4, dataObject.getStreet());
      statement.setString(5, dataObject.getState());
      statement.setString(6, dataObject.getEmail());
      statement.executeUpdate();
    }
  }

  @Override
  public void deleteOne(String email) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, email);
      statement.executeUpdate();
    }
  }
}