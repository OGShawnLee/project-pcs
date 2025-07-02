package db;

import org.example.business.dao.ProjectRequestDAO;
import org.example.business.dao.filter.FilterProject;
import org.example.business.dto.ProjectRequestDTO;
import org.example.common.UserDisplayableException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ProjectRequestDAOTest {
  private final static ProjectRequestDAO PROJECT_REQUEST_DAO = new ProjectRequestDAO();

  public static ProjectRequestDTO createOnTestProjectRequest() throws UserDisplayableException {
    ProjectDAOTest.createOneTestProject();
    StudentDAOTest.createOneTestStudent();

    ProjectRequestDTO dataObject = new ProjectRequestDTO.ProjectRequestBuilder()
      .setIDStudent(StudentDAOTest.STUDENT_DTO.getID())
      .setIDProject(ProjectDAOTest.PROJECT_DTO.getID())
      .setState("PENDING")
      .setReasonOfState("Student has a good background working with the required technologies but needs to be evaluated")
      .build();

    PROJECT_REQUEST_DAO.createOne(dataObject);

    return dataObject;
  }

  public static void deleteOneTestProjectRequest() throws UserDisplayableException {
    ProjectDAOTest.deleteOneTestProject();
    StudentDAOTest.deleteOneTestStudent();

    PROJECT_REQUEST_DAO.deleteOne(
      new FilterProject(ProjectDAOTest.PROJECT_DTO.getID(), StudentDAOTest.STUDENT_DTO.getID())
    );
  }

  @AfterEach
  public void tearDown() throws UserDisplayableException {
    deleteOneTestProjectRequest();
  }

  @Test
  public void testCreateOneProjectRequest() {
    assertDoesNotThrow(() -> {
      ProjectRequestDTO projectRequest = createOnTestProjectRequest();

      ProjectRequestDTO createdProjectRequest = PROJECT_REQUEST_DAO.getOne(
        new FilterProject(ProjectDAOTest.PROJECT_DTO.getID(), StudentDAOTest.STUDENT_DTO.getID())
      );

      Assertions.assertEquals(projectRequest, createdProjectRequest);
    });
  }

  @Test
  public void testGetAllProjectRequests() {
    assertDoesNotThrow(() -> {
      createOnTestProjectRequest();

      List<ProjectRequestDTO> projectRequestList = PROJECT_REQUEST_DAO.getAll();

      Assertions.assertNotNull(projectRequestList);
      Assertions.assertFalse(projectRequestList.isEmpty());
    });
  }

  @Test
  public void testGetOneProjectRequest() {
    assertDoesNotThrow(() -> {
      ProjectRequestDTO projectRequest = createOnTestProjectRequest();

      ProjectRequestDTO createdProjectRequest = PROJECT_REQUEST_DAO.getOne(
        new FilterProject(ProjectDAOTest.PROJECT_DTO.getID(), StudentDAOTest.STUDENT_DTO.getID())
      );

      Assertions.assertEquals(projectRequest, createdProjectRequest);
    });
  }

  @Test
  public void testUpdateOneProjectRequest() {
    assertDoesNotThrow(() -> {
      ProjectRequestDTO createdProjectRequest = createOnTestProjectRequest();

      ProjectRequestDTO updatedProjectRequest = new ProjectRequestDTO.ProjectRequestBuilder()
        .setIDStudent(createdProjectRequest.getIDStudent())
        .setIDProject(createdProjectRequest.getIDProject())
        .setState("ACCEPTED")
        .setReasonOfState("Student has a good background working with the required technologies")
        .build();

      PROJECT_REQUEST_DAO.updateOne(updatedProjectRequest);

      Assertions.assertEquals(updatedProjectRequest, PROJECT_REQUEST_DAO.getOne(
        new FilterProject(ProjectDAOTest.PROJECT_DTO.getID(), StudentDAOTest.STUDENT_DTO.getID())
      ));
    });
  }

  @Test
  public void testDeleteOneProjectRequest() {
    assertDoesNotThrow(() -> {
      ProjectRequestDTO createdProjectRequest = createOnTestProjectRequest();

      Assertions.assertEquals(createdProjectRequest, PROJECT_REQUEST_DAO.getOne(
        new FilterProject(ProjectDAOTest.PROJECT_DTO.getID(), StudentDAOTest.STUDENT_DTO.getID())
      ));

      deleteOneTestProjectRequest();

      ProjectRequestDTO deletedProjectRequest = PROJECT_REQUEST_DAO.getOne(
        new FilterProject(ProjectDAOTest.PROJECT_DTO.getID(), StudentDAOTest.STUDENT_DTO.getID())
      );

      Assertions.assertNull(deletedProjectRequest);
    });
  }
}
