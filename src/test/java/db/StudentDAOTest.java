package db;

import org.example.business.dto.StudentDTO;
import org.example.business.dao.StudentDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDAOTest {
  public static final StudentDAO STUDENT_DAO = new StudentDAO();
  public static final StudentDTO STUDENT_DTO = new StudentDTO.StudentBuilder()
    .setID("23014115")
    .setEmail(AccountDAOTest.STUDENT_ACCOUNT_DTO.email())
    .setName("Shawn")
    .setPaternalLastName("Doe")
    .setMaternalLastName("Smith")
    .setState("ACTIVE")
    .setPhoneNumber("2281904050")
    .build();

  public static void createOneTestStudent() throws SQLException {
    STUDENT_DAO.createOne(STUDENT_DTO);
  }

  public static void deleteOneTestStudent() throws SQLException {
    STUDENT_DAO.deleteOne(STUDENT_DTO.getID());
  }

  @AfterEach
  public void tearDown() throws SQLException {
    deleteOneTestStudent();
  }

  @Test
  public void testCreateOneStudent() {
    assertDoesNotThrow(() -> {
      createOneTestStudent();
      Assertions.assertEquals(STUDENT_DTO, STUDENT_DAO.getOne(STUDENT_DTO.getID()));
    });
  }

  @Test
  public void testGetAllStudents() {
    assertDoesNotThrow(() -> {
      createOneTestStudent();

      List<StudentDTO> studentList = STUDENT_DAO.getAll();

      Assertions.assertNotNull(studentList);
      Assertions.assertFalse(studentList.isEmpty());
    });
  }

  @Test
  public void testGetOneStudent() {
    assertDoesNotThrow(() -> {
      createOneTestStudent();
      Assertions.assertEquals(STUDENT_DTO, STUDENT_DAO.getOne(STUDENT_DTO.getID()));
    });
  }

  @Test
  public void testUpdateOneStudent() {
    assertDoesNotThrow(() -> {
      createOneTestStudent();

      StudentDTO updatedStudent = new StudentDTO.StudentBuilder()
        .setID(STUDENT_DTO.getID())
        .setEmail(STUDENT_DTO.getEmail())
        .setName("John Halo")
        .setPaternalLastName("Does")
        .setMaternalLastName("Smithson")
        .setState("RETIRED")
        .setFinalGrade(10)
        .setPhoneNumber("2281904050")
        .build();
      STUDENT_DAO.updateOne(updatedStudent);

      Assertions.assertEquals(updatedStudent, STUDENT_DAO.getOne(STUDENT_DTO.getID()));
    });
  }

  @Test
  public void testDeleteOneStudent() {
    assertDoesNotThrow(() -> {
      createOneTestStudent();
      StudentDTO createdStudent = STUDENT_DAO.getOne(STUDENT_DTO.getID());
      Assertions.assertNotNull(createdStudent);

      deleteOneTestStudent();
      StudentDTO deletedStudent = STUDENT_DAO.getOne(STUDENT_DTO.getID());
      Assertions.assertNull(deletedStudent);
    });
  }
}
