package org.example.business.dao;

import org.example.business.dto.ProjectDTO;
import org.example.business.dto.enumeration.ProjectSector;
import org.example.business.dto.WorkPlanDTO;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ProjectDAO extends DAOPattern<ProjectDTO, Integer> {
  private static final String CREATE_QUERY =
    "INSERT INTO Project (id_organization, name, description, department, available_places, methodology, sector) VALUES (?, ?, ?, ?, ?, ?, ?)";
  private static final String CREATE_WITH_WORK_PLAN_QUERY =
    "CALL create_project_and_work_plan(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Project";
  private static final String GET_QUERY = "SELECT * FROM Project WHERE id_project = ?";
  private static final String GET_BY_STUDENT_QUERY =
    """
      SELECT * FROM Project WHERE (SELECT EXISTS(SELECT * FROM Practice WHERE id_student = ? AND id_project = Project.id_project))""";
  private static final String GET_ALL_BY_STATE = "SELECT * FROM Project WHERE state = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Project SET id_organization = ?, name = ?, description = ?, department = ?, available_places = ?, methodology = ?, state = ?, sector = ? WHERE id_project = ?";
  private static final String DELETE_QUERY = "DELETE FROM Project WHERE id_project = ?";

  @Override
  ProjectDTO createDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new ProjectDTO.ProjectBuilder()
      .setID(resultSet.getInt("id_project"))
      .setIDOrganization(resultSet.getString("id_organization"))
      .setName(resultSet.getString("name"))
      .setDescription(resultSet.getString("description"))
      .setDepartment(resultSet.getString("department"))
      .setAvailablePlaces(resultSet.getString("available_places"))
      .setMethodology(resultSet.getString("methodology"))
      .setState(resultSet.getString("state"))
      .setSector(ProjectSector.valueOf(resultSet.getString("sector")))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  @Override
  public void createOne(ProjectDTO projectDTO) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)
    ) {
      statement.setString(1, projectDTO.getIDOrganization());
      statement.setString(2, projectDTO.getName());
      statement.setString(3, projectDTO.getDescription());
      statement.setString(4, projectDTO.getDepartment());
      statement.setInt(5, projectDTO.getAvailablePlaces());
      statement.setString(6, projectDTO.getMethodology());
      statement.setString(7, projectDTO.getSector().toString());
      statement.executeUpdate();

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          projectDTO.setID(generatedKeys.getInt(1));
        } else {
          throw new SQLException("Creating project failed, no ID obtained.");
        }
      }
    }
  }

  public void createOneWithWorkPlan(
    ProjectDTO projectDTO,
    WorkPlanDTO workPlanDTO
  ) throws SQLException {
    try (
      Connection connection = getConnection();
      CallableStatement statement = connection.prepareCall(CREATE_WITH_WORK_PLAN_QUERY)
    ) {
      statement.setString(1, projectDTO.getIDOrganization());
      statement.setString(2, projectDTO.getName());
      statement.setString(3, projectDTO.getDescription());
      statement.setString(4, projectDTO.getDepartment());
      statement.setInt(5, projectDTO.getAvailablePlaces());
      statement.setString(6, projectDTO.getMethodology());
      statement.setString(7, projectDTO.getSector().toString());
      statement.setString(8, workPlanDTO.getProjectGoal());
      statement.setString(9, workPlanDTO.getTheoreticalScope());
      statement.setString(10, workPlanDTO.getFirstMonthActivities());
      statement.setString(11, workPlanDTO.getSecondMonthActivities());
      statement.setString(12, workPlanDTO.getThirdMonthActivities());
      statement.setString(13, workPlanDTO.getFourthMonthActivities());
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

  public List<ProjectDTO> getAllByState(String state) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_STATE)
    ) {
      statement.setString(1, state);
      List<ProjectDTO> list = new ArrayList<>();

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          list.add(createDTOInstanceFromResultSet(resultSet));
        }
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

      ProjectDTO projectDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          projectDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return projectDTO;
    }
  }

  public ProjectDTO getOneByStudent(String idStudent) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_BY_STUDENT_QUERY)
    ) {
      statement.setString(1, idStudent);

      ProjectDTO projectDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          projectDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return projectDTO;
    }
  }

  @Override
  public void updateOne(ProjectDTO projectDTO) throws SQLException {
    try (
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, projectDTO.getIDOrganization());
      statement.setString(2, projectDTO.getName());
      statement.setString(3, projectDTO.getDescription());
      statement.setString(4, projectDTO.getDepartment());
      statement.setInt(5, projectDTO.getAvailablePlaces());
      statement.setString(6, projectDTO.getMethodology());
      statement.setString(7, projectDTO.getState());
      statement.setString(8, projectDTO.getSector().toString());
      statement.setInt(9, projectDTO.getID());
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