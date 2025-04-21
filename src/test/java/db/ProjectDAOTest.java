package db;

import org.example.business.dto.ProjectDTO;
import org.example.db.dao.ProjectDAO;
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
    .setName("Bard AI Reborn")
    .setMethodology("Agile")
    .setSector("PRIVATE")
    .build();

  public static void createOneTestProject() throws SQLException {
    OrganizationDAOTest.createOneTestOrganization();
    PROJECT_DAO.createOne(PROJECT_DTO);
  }

  public static void deleteOneTestProject() throws SQLException {
    OrganizationDAOTest.deleteOneTestOrganization();
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

      ProjectDTO createdProject = PROJECT_DAO.getOne(PROJECT_DTO.getID());

      Assertions.assertNotNull(createdProject);
      Assertions.assertEquals(PROJECT_DTO.getID(), createdProject.getID());
      Assertions.assertEquals(PROJECT_DTO.getIDOrganization(), createdProject.getIDOrganization());
      Assertions.assertEquals(PROJECT_DTO.getName(), createdProject.getName());
      Assertions.assertEquals(PROJECT_DTO.getMethodology(), createdProject.getMethodology());
      Assertions.assertEquals(PROJECT_DTO.getSector(), createdProject.getSector());
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

      ProjectDTO retrievedProject = PROJECT_DAO.getOne(PROJECT_DTO.getID());

      Assertions.assertNotNull(retrievedProject);
      Assertions.assertEquals(PROJECT_DTO.getID(), retrievedProject.getID());
      Assertions.assertEquals(PROJECT_DTO.getIDOrganization(), retrievedProject.getIDOrganization());
      Assertions.assertEquals(PROJECT_DTO.getName(), retrievedProject.getName());
      Assertions.assertEquals(PROJECT_DTO.getMethodology(), retrievedProject.getMethodology());
      Assertions.assertEquals(PROJECT_DTO.getSector(), retrievedProject.getSector());
    });
  }

  @Test
  public void testUpdateOneProject() {
    assertDoesNotThrow(() -> {
      createOneTestProject();

      ProjectDTO updatedProject = new ProjectDTO.ProjectBuilder()
        .setID(PROJECT_DTO.getID())
        .setIDOrganization(OrganizationDAOTest.ORGANIZATION_DTO.getEmail())
        .setName("Bard AI Reborn 2")
        .setMethodology("Waterfall")
        .setSector(PROJECT_DTO.getSector())
        .setState("RETIRED")
        .build();

      PROJECT_DAO.updateOne(updatedProject);

      ProjectDTO retrievedProject = PROJECT_DAO.getOne(PROJECT_DTO.getID());

      Assertions.assertNotNull(retrievedProject);
      Assertions.assertEquals(updatedProject.getID(), retrievedProject.getID());
      Assertions.assertEquals(updatedProject.getMethodology(), retrievedProject.getMethodology());
      Assertions.assertEquals(updatedProject.getSector(), retrievedProject.getSector());
      Assertions.assertEquals(updatedProject.getState(), retrievedProject.getState());
    });
  }

  @Test
  public void testUpdateOneProjectSector() {
    assertDoesNotThrow(() -> {
      createOneTestProject();

      ProjectDTO updatedProject = new ProjectDTO.ProjectBuilder()
        .setID(PROJECT_DTO.getID())
        .setIDOrganization(OrganizationDAOTest.ORGANIZATION_DTO.getEmail())
        .setName(PROJECT_DTO.getName())
        .setMethodology(PROJECT_DTO.getMethodology())
        .setSector("PRIVATE")
        .setState("ACTIVE")
        .build();

      PROJECT_DAO.updateOne(updatedProject);

      ProjectDTO retrievedProject = PROJECT_DAO.getOne(PROJECT_DTO.getID());

      Assertions.assertNotNull(retrievedProject);
      Assertions.assertEquals(updatedProject.getID(), retrievedProject.getID());
      Assertions.assertEquals(updatedProject.getMethodology(), retrievedProject.getMethodology());
      Assertions.assertEquals(updatedProject.getSector(), retrievedProject.getSector());
      Assertions.assertEquals(updatedProject.getState(), retrievedProject.getState());

      updatedProject = new ProjectDTO.ProjectBuilder()
        .setID(PROJECT_DTO.getID())
        .setIDOrganization(OrganizationDAOTest.ORGANIZATION_DTO.getEmail())
        .setName(PROJECT_DTO.getName())
        .setMethodology(PROJECT_DTO.getMethodology())
        .setSector("SOCIAL")
        .setState("ACTIVE")
        .build();

      PROJECT_DAO.updateOne(updatedProject);
      retrievedProject = PROJECT_DAO.getOne(PROJECT_DTO.getID());
      Assertions.assertNotNull(retrievedProject);
      Assertions.assertEquals(updatedProject.getID(), retrievedProject.getID());
      Assertions.assertEquals(updatedProject.getMethodology(), retrievedProject.getMethodology());
      Assertions.assertEquals(updatedProject.getSector(), retrievedProject.getSector());
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
