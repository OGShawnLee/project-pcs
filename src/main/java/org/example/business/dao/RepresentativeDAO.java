package org.example.business.dao;

import org.example.business.dao.shape.CompleteDAOShape;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dto.RepresentativeDTO;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class RepresentativeDAO extends CompleteDAOShape<RepresentativeDTO, String> {
  private static final String CREATE_ONE_QUERY = """
    INSERT INTO Representative (name, paternal_last_name, maternal_last_name, email, phone_number, organization_email, position)
    VALUES (?, ?, ?, ?, ?, ?, ?)
  """;
  private static final String GET_ALL_QUERY =
    "SELECT * FROM Representative";
  private static final String GET_ALL_BY_STATE_QUERY =
    "SELECT * FROM Representative WHERE state = ?";
  private static final String GET_ALL_BY_ORGANIZATION_QUERY =
    "SELECT * FROM Representative WHERE organization_email = ? AND state = ?";
  private static final String GET_ONE_QUERY = "SELECT * FROM Representative WHERE email = ?";
  private static final String UPDATE_ONE_QUERY = """
    UPDATE Representative
    SET name = ?, paternal_last_name = ?, maternal_last_name = ?, state = ?, phone_number = ?, organization_email = ?, position = ?
    WHERE email = ?
  """;
  private static final String DELETE_ONE_QUERY =
    "DELETE FROM Representative WHERE email = ?";

  @Override
  public RepresentativeDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new RepresentativeDTO.RepresentativeBuilder()
      .setEmail(resultSet.getString("email"))
      .setName(resultSet.getString("name"))
      .setPaternalLastName(resultSet.getString("paternal_last_name"))
      .setMaternalLastName(resultSet.getString("maternal_last_name"))
      .setPhoneNumber(resultSet.getString("phone_number"))
      .setState(resultSet.getString("state"))
      .setOrganizationID(resultSet.getString("organization_email"))
      .setPosition(resultSet.getString("position"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(RepresentativeDTO representativeDTO) throws SQLException {
    try (
      Connection connection = DBConnector.getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_ONE_QUERY)
    ) {
      statement.setString(1, representativeDTO.getName());
      statement.setString(2, representativeDTO.getPaternalLastName());
      statement.setString(3, representativeDTO.getMaternalLastName());
      statement.setString(4, representativeDTO.getEmail());
      statement.setString(5, representativeDTO.getPhoneNumber());
      statement.setString(6, representativeDTO.getOrganizationID());
      statement.setString(7, representativeDTO.getPosition());
      statement.executeUpdate();
    }
  }

  @Override
  public List<RepresentativeDTO> getAll() throws SQLException {
    try (
      Connection connection = DBConnector.getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<RepresentativeDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    }
  }

  public List<RepresentativeDTO> getAllByState(String state) throws SQLException {
    try (
      Connection connection = DBConnector.getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_STATE_QUERY)
    ) {
      statement.setString(1, state);

      try (ResultSet resultSet = statement.executeQuery()) {
        List<RepresentativeDTO> list = new ArrayList<>();

        while (resultSet.next()) {
          list.add(createDTOInstanceFromResultSet(resultSet));
        }

        return list;
      }
    }
  }

  public List<RepresentativeDTO> getAllByOrganization(String email, String state) throws SQLException {
    try (
      Connection connection = DBConnector.getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_ORGANIZATION_QUERY)
    ) {
      statement.setString(1, email);
      statement.setString(2, state);

      try (ResultSet resultSet = statement.executeQuery()) {
        List<RepresentativeDTO> list = new ArrayList<>();

        while (resultSet.next()) {
          list.add(createDTOInstanceFromResultSet(resultSet));
        }

        return list;
      }
    }
  }

  public RepresentativeDTO findOne(String email) throws SQLException {
    try (
      Connection connection = DBConnector.getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ONE_QUERY)
    ) {
      statement.setString(1, email);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        return createDTOInstanceFromResultSet(resultSet);
      } else {
        return null;
      }
    }
  }

  @Override
  public RepresentativeDTO getOne(String email) throws SQLException, NotFoundException {
    try (
      Connection connection = DBConnector.getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ONE_QUERY)
    ) {
      statement.setString(1, email);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        return createDTOInstanceFromResultSet(resultSet);
      } else {
        throw new NotFoundException("No se encontró el Representante solicitado.");
      }
    }
  }

  @Override
  public void updateOne(RepresentativeDTO representativeDTO) throws SQLException {
    try (
      Connection connection = DBConnector.getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_ONE_QUERY)
    ) {
      statement.setString(1, representativeDTO.getName());
      statement.setString(2, representativeDTO.getPaternalLastName());
      statement.setString(3, representativeDTO.getMaternalLastName());
      statement.setString(4, representativeDTO.getState());
      statement.setString(5, representativeDTO.getPhoneNumber());
      statement.setString(6, representativeDTO.getOrganizationID());
      statement.setString(7, representativeDTO.getPosition());
      statement.setString(8, representativeDTO.getEmail());
      statement.executeUpdate();
    }
  }

  @Override
  public void deleteOne(String email) throws SQLException {
    try (
      Connection connection = DBConnector.getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_ONE_QUERY)
    ) {
      statement.setString(1, email);
      statement.executeUpdate();
    }
  }
}
