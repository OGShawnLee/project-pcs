package db;

import org.example.business.dto.enumeration.ProjectSector;
import org.example.business.dto.ProjectDTO;
import org.example.business.dao.ProjectDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ProjectDAOTest {
  public static final ProjectDAO PROJECT_DAO = new ProjectDAO();
  public static final ProjectDTO PROJECT_DTO = new ProjectDTO.ProjectBuilder()
    .setIDOrganization(OrganizationDAOTest.ORGANIZATION_DTO.getEmail())
    .setRepresentativeEmail(RepresentativeDAOTest.REPRESENTATIVE_DTO.getEmail())
    .setName("Bard AI Reborn")
    .setDescription("An advanced AI project to enhance Bard's capabilities.")
    .setDepartment("AI Research")
    .setAvailablePlaces("5")
    .setMethodology("Agile")
    .setState("ACTIVE")
    .setSector(ProjectSector.PRIVATE)
    .build();

  public static void createOneTestProject() throws SQLException {
    RepresentativeDAOTest.createOneTestRepresentative();
    PROJECT_DAO.createOne(PROJECT_DTO);
  }

  public static void deleteOneTestProject() throws SQLException {
    RepresentativeDAOTest.deleteOneTestRepresentative();
    PROJECT_DAO.deleteOne(PROJECT_DTO.getID());
  }

  @AfterEach
  public void tearDown() throws SQLException {
    deleteOneTestProject();
  }

  @Test
  public void testCreateOneProject() {
    assertDoesNotThrow(() -> {
      createOneTestProject();
      Assertions.assertEquals(PROJECT_DTO, PROJECT_DAO.getOne(PROJECT_DTO.getID()));
    });
  }

  @Test
  public void testGetAllProjects() {
    assertDoesNotThrow(() -> {
      createOneTestProject();

      List<ProjectDTO> projectList = PROJECT_DAO.getAll();

      Assertions.assertNotNull(projectList);
      Assertions.assertFalse(projectList.isEmpty());
    });
  }

  @Test
  public void testGetOneProject() {
    assertDoesNotThrow(() -> {
      createOneTestProject();
      Assertions.assertEquals(PROJECT_DTO, PROJECT_DAO.getOne(PROJECT_DTO.getID()));
    });
  }

  @Test
  public void testUpdateOneProject() {
    assertDoesNotThrow(() -> {
      createOneTestProject();

      ProjectDTO updatedProject = new ProjectDTO.ProjectBuilder()
        .setID(PROJECT_DTO.getID())
        .setIDOrganization(OrganizationDAOTest.ORGANIZATION_DTO.getEmail())
        .setRepresentativeEmail(RepresentativeDAOTest.REPRESENTATIVE_DTO.getEmail())
        .setName("Bard AI Reborn 2")
        .setDescription("An advanced AI project to enhance Bard's capabilities with new features.")
        .setDepartment("AI Research and Development")
        .setAvailablePlaces("10")
        .setMethodology("Waterfall")
        .setSector(PROJECT_DTO.getSector())
        .setState("RETIRED")
        .build();

      PROJECT_DAO.updateOne(updatedProject);

      Assertions.assertEquals(updatedProject, PROJECT_DAO.getOne(PROJECT_DTO.getID()));
    });
  }

  @Test
  public void testUpdateOneProjectSector() {
    assertDoesNotThrow(() -> {
      createOneTestProject();

      ProjectDTO updatedProject = new ProjectDTO.ProjectBuilder()
        .setID(PROJECT_DTO.getID())
        .setIDOrganization(OrganizationDAOTest.ORGANIZATION_DTO.getEmail())
        .setRepresentativeEmail(RepresentativeDAOTest.REPRESENTATIVE_DTO.getEmail())
        .setName(PROJECT_DTO.getName())
        .setDescription(PROJECT_DTO.getDescription())
        .setDepartment(PROJECT_DTO.getDepartment())
        .setAvailablePlaces(Integer.toString(PROJECT_DTO.getAvailablePlaces()))
        .setMethodology(PROJECT_DTO.getMethodology())
        .setSector(ProjectSector.PRIVATE)
        .setState("ACTIVE")
        .build();

      PROJECT_DAO.updateOne(updatedProject);

      Assertions.assertEquals(updatedProject, PROJECT_DAO.getOne(PROJECT_DTO.getID()));

      updatedProject = new ProjectDTO.ProjectBuilder()
        .setID(PROJECT_DTO.getID())
        .setIDOrganization(OrganizationDAOTest.ORGANIZATION_DTO.getEmail())
        .setRepresentativeEmail(RepresentativeDAOTest.REPRESENTATIVE_DTO.getEmail())
        .setName(PROJECT_DTO.getName())
        .setDescription(PROJECT_DTO.getDescription())
        .setDepartment(PROJECT_DTO.getDepartment())
        .setAvailablePlaces(Integer.toString(PROJECT_DTO.getAvailablePlaces()))
        .setMethodology(PROJECT_DTO.getMethodology())
        .setSector(ProjectSector.SOCIAL)
        .setState("ACTIVE")
        .build();

      PROJECT_DAO.updateOne(updatedProject);

      Assertions.assertEquals(updatedProject, PROJECT_DAO.getOne(PROJECT_DTO.getID()));
    });
  }

  @Test
  public void testDeleteOneProject() {
    assertDoesNotThrow(() -> {
      createOneTestProject();
      ProjectDTO createdProject = PROJECT_DAO.getOne(PROJECT_DTO.getID());
      Assertions.assertNotNull(createdProject);

      deleteOneTestProject();
      ProjectDTO deletedProject = PROJECT_DAO.getOne(PROJECT_DTO.getID());
      Assertions.assertNull(deletedProject);
    });
  }
}
