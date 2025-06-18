package org.example.business.dao;

import org.example.business.dto.OrganizationDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO extends DAOPattern<OrganizationDTO, String> {
  private static final String CREATE_QUERY =
    "INSERT INTO Organization (email, name, representative_full_name, colony, street, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Organization";
  private static final String GET_ALL_BY_STATE_QUERY = "SELECT * FROM Organization WHERE state = ?";
  private static final String GET_QUERY = "SELECT * FROM Organization WHERE email = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Organization SET name = ?, representative_full_name = ?, colony = ?, street = ?, phone_number = ?, state = ? WHERE email = ?";
  private static final String DELETE_QUERY = "DELETE FROM Organization WHERE email = ?";

  @Override
  OrganizationDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new OrganizationDTO.OrganizationBuilder()
      .setEmail(resultSet.getString("email"))
      .setName(resultSet.getString("name"))
      .setRepresentativeFullName(resultSet.getString("representative_full_name"))
      .setColony(resultSet.getString("colony"))
      .setStreet(resultSet.getString("street"))
      .setState(resultSet.getString("state"))
      .setPhoneNumber(resultSet.getString("phone_number"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(OrganizationDTO organizationDTO) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, organizationDTO.getEmail());
      statement.setString(2, organizationDTO.getName());
      statement.setString(3, organizationDTO.getRepresentativeFullName());
      statement.setString(4, organizationDTO.getColony());
      statement.setString(5, organizationDTO.getStreet());
      statement.setString(6, organizationDTO.getPhoneNumber());
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

  public List<OrganizationDTO> getAllByState(String state) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_STATE_QUERY)
    ) {
      statement.setString(1, state);
      List<OrganizationDTO> list = new ArrayList<>();

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          list.add(createDTOInstanceFromResultSet(resultSet));
        }
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

      OrganizationDTO organizationDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          organizationDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return organizationDTO;
    }
  }

  @Override
  public void updateOne(OrganizationDTO organizationDTO) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, organizationDTO.getName());
      statement.setString(2, organizationDTO.getRepresentativeFullName());
      statement.setString(3, organizationDTO.getColony());
      statement.setString(4, organizationDTO.getStreet());
      statement.setString(5, organizationDTO.getPhoneNumber());
      statement.setString(6, organizationDTO.getState());
      statement.setString(7, organizationDTO.getEmail());
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