package org.example.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.dao.shape.CompleteDAOShape;
import org.example.business.dto.ProjectDTO;
import org.example.business.dto.enumeration.ProjectSector;
import org.example.business.dto.WorkPlanDTO;
import org.example.common.ExceptionHandler;
import org.example.common.UserDisplayableException;
import org.example.db.DBConnector;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ProjectDAO extends CompleteDAOShape<ProjectDTO, Integer> {
  private static final Logger LOGGER = LogManager.getLogger(ProjectDAO.class);
  private static final String CREATE_QUERY =
    "INSERT INTO Project (id_organization, representative_email, name, description, department, available_places, methodology, sector) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String CREATE_WITH_WORK_PLAN_QUERY =
    "CALL create_project_and_work_plan(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Project";
  private static final String GET_QUERY = "SELECT * FROM Project WHERE id_project = ?";
  private static final String GET_BY_STUDENT_QUERY = "CALL get_current_student_project(?)";
  private static final String GET_ALL_BY_STATE = "SELECT * FROM Project WHERE state = ?";
  private static final String UPDATE_QUERY =
    "UPDATE Project SET id_organization = ?, representative_email = ?, name = ?, description = ?, department = ?, available_places = ?, methodology = ?, state = ?, sector = ? WHERE id_project = ?";
  private static final String DELETE_QUERY = "DELETE FROM Project WHERE id_project = ?";

  @Override
  public ProjectDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
    return new ProjectDTO.ProjectBuilder()
      .setID(resultSet.getInt("id_project"))
      .setIDOrganization(resultSet.getString("id_organization"))
      .setRepresentativeEmail(resultSet.getString("representative_email"))
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
  public void createOne(ProjectDTO projectDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)
    ) {
      statement.setString(1, projectDTO.getIDOrganization());
      statement.setString(2, projectDTO.getRepresentativeEmail());
      statement.setString(3, projectDTO.getName());
      statement.setString(4, projectDTO.getDescription());
      statement.setString(5, projectDTO.getDepartment());
      statement.setInt(6, projectDTO.getAvailablePlaces());
      statement.setString(7, projectDTO.getMethodology());
      statement.setString(8, projectDTO.getSector().toDBString());
      statement.executeUpdate();

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          projectDTO.setID(generatedKeys.getInt(1));
        } else {
          throw new SQLException("Creating project failed, no ID obtained.");
        }
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el proyecto.");
    }
  }

  public void createOneWithWorkPlan(
    ProjectDTO projectDTO,
    WorkPlanDTO workPlanDTO
  ) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      CallableStatement statement = connection.prepareCall(CREATE_WITH_WORK_PLAN_QUERY)
    ) {
      statement.setString(1, projectDTO.getIDOrganization());
      statement.setString(2, projectDTO.getRepresentativeEmail());
      statement.setString(3, projectDTO.getName());
      statement.setString(4, projectDTO.getDescription());
      statement.setString(5, projectDTO.getDepartment());
      statement.setInt(6, projectDTO.getAvailablePlaces());
      statement.setString(7, projectDTO.getMethodology());
      statement.setString(8, projectDTO.getSector().toDBString());
      statement.setString(9, workPlanDTO.getProjectGoal());
      statement.setString(10, workPlanDTO.getTheoreticalScope());
      statement.setString(11, workPlanDTO.getFirstMonthActivities());
      statement.setString(12, workPlanDTO.getSecondMonthActivities());
      statement.setString(13, workPlanDTO.getThirdMonthActivities());
      statement.setString(14, workPlanDTO.getFourthMonthActivities());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el proyecto con plan de trabajo.");
    }
  }

  @Override
  public List<ProjectDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<ProjectDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los proyectos.");
    }
  }

  public List<ProjectDTO> getAllByState(String state) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
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
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los proyectos por estado.");
    }
  }

  @Override
  public ProjectDTO getOne(Integer id) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
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
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar el proyecto.");
    }
  }

  public ProjectDTO getOneByStudent(String idStudent) throws NotFoundException, UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      CallableStatement statement = connection.prepareCall(GET_BY_STUDENT_QUERY)
    ) {
      statement.setString(1, idStudent);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return createDTOInstanceFromResultSet(resultSet);
        } else {
          throw new NotFoundException("No se ha encontrado un proyecto asignado al estudiante.");
        }
      }
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar el proyecto del estudiante.");
    }
  }

  @Override
  public void updateOne(ProjectDTO projectDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, projectDTO.getIDOrganization());
      statement.setString(2, projectDTO.getRepresentativeEmail());
      statement.setString(3, projectDTO.getName());
      statement.setString(4, projectDTO.getDescription());
      statement.setString(5, projectDTO.getDepartment());
      statement.setInt(6, projectDTO.getAvailablePlaces());
      statement.setString(7, projectDTO.getMethodology());
      statement.setString(8, projectDTO.getState());
      statement.setString(9, projectDTO.getSector().toDBString());
      statement.setInt(10, projectDTO.getID());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el proyecto.");
    }
  }

  @Override
  public void deleteOne(Integer id) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setInt(1, id);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar el proyecto.");
    }
  }
}