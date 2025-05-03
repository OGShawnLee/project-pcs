package db;

import org.example.business.dto.PracticeDTO;
import org.example.business.dao.PracticeDAO;
import org.example.business.dao.filter.FilterPractice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PracticeDAOTest {
  public static final PracticeDAO PRACTICE_DAO = new PracticeDAO();

  public static PracticeDTO createOneTestPractice() throws SQLException {
    StudentDAOTest.createOneTestStudent();
    ProjectDAOTest.createOneTestProject();

    PracticeDTO dataObject = new PracticeDTO.PracticeBuilder()
      .setIDStudent(StudentDAOTest.STUDENT_DTO.getID())
      .setIDProject(ProjectDAOTest.PROJECT_DTO.getID())
      .setReasonOfAssignation("Student has a good background working with the required technologies")
      .build();
    PRACTICE_DAO.createOne(dataObject);
    return dataObject;
  }

  public static void deleteOneTestPractice() throws SQLException {
    StudentDAOTest.deleteOneTestStudent();
    ProjectDAOTest.deleteOneTestProject();
    PRACTICE_DAO.deleteOne(new FilterPractice(StudentDAOTest.STUDENT_DTO.getID(), ProjectDAOTest.PROJECT_DTO.getID()));
  }

  @AfterEach
  public void tearDown() throws SQLException {
    deleteOneTestPractice();
  }

  @Test
  public void testCreateOnePractice() {
    assertDoesNotThrow(() -> {
      PracticeDTO practice = createOneTestPractice();

      PracticeDTO createdPractice = PRACTICE_DAO.getOne(
        new FilterPractice(StudentDAOTest.STUDENT_DTO.getID(), ProjectDAOTest.PROJECT_DTO.getID())
      );

      Assertions.assertNotNull(createdPractice);
      Assertions.assertEquals(practice.getIDStudent(), createdPractice.getIDStudent());
      Assertions.assertEquals(practice.getIDProject(), createdPractice.getIDProject());
      Assertions.assertEquals(practice.getReasonOfAssignation(), createdPractice.getReasonOfAssignation());
    });
  }

  @Test
  public void testGetAllPractices() {
    assertDoesNotThrow(() -> {
      createOneTestPractice();

      List<PracticeDTO> practiceList = PRACTICE_DAO.getAll();

      Assertions.assertNotNull(practiceList);
      Assertions.assertFalse(practiceList.isEmpty());
    });
  }

  @Test
  public void testGetOnePractice() {
    assertDoesNotThrow(() -> {
      PracticeDTO practice = createOneTestPractice();

      PracticeDTO createdPractice = PRACTICE_DAO.getOne(
        new FilterPractice(StudentDAOTest.STUDENT_DTO.getID(), ProjectDAOTest.PROJECT_DTO.getID())
      );

      Assertions.assertNotNull(createdPractice);
      Assertions.assertEquals(practice.getIDStudent(), createdPractice.getIDStudent());
      Assertions.assertEquals(practice.getIDProject(), createdPractice.getIDProject());
      Assertions.assertEquals(practice.getReasonOfAssignation(), createdPractice.getReasonOfAssignation());
    });
  }

  @Test
  public void testUpdateOnePractice() {
    assertDoesNotThrow(() -> {
      PracticeDTO createdPractice = createOneTestPractice();

      PracticeDTO updatedPractice = new PracticeDTO.PracticeBuilder()
        .setIDStudent(createdPractice.getIDStudent())
        .setIDProject(createdPractice.getIDProject())
        .setReasonOfAssignation("Updated reason of assignation")
        .build();

      PRACTICE_DAO.updateOne(
        updatedPractice
      );

      PracticeDTO retrievedPractice = PRACTICE_DAO.getOne(
        new FilterPractice(StudentDAOTest.STUDENT_DTO.getID(), ProjectDAOTest.PROJECT_DTO.getID())
      );

      Assertions.assertNotNull(retrievedPractice);
      Assertions.assertEquals(updatedPractice.getIDStudent(), retrievedPractice.getIDStudent());
      Assertions.assertEquals(updatedPractice.getIDProject(), retrievedPractice.getIDProject());
      Assertions.assertEquals(updatedPractice.getReasonOfAssignation(), retrievedPractice.getReasonOfAssignation());
    });
  }

  @Test
  public void testDeleteOnePractice() {
    assertDoesNotThrow(() -> {
      PracticeDTO createdPractice = createOneTestPractice();
      Assertions.assertNotNull(createdPractice);

      deleteOneTestPractice();
      PracticeDTO deletedPractice = PRACTICE_DAO.getOne(
        new FilterPractice(StudentDAOTest.STUDENT_DTO.getID(), ProjectDAOTest.PROJECT_DTO.getID())
      );
      Assertions.assertNull(deletedPractice);
    });
  }
}
