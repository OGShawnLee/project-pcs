package db;

import org.example.business.StudentDTO;
import org.example.db.StudentDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
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
    .build();

  public static void createOneTestStudent() throws SQLException {
    AccountDAOTest.createOneTestStudentAccount();
    STUDENT_DAO.createOne(STUDENT_DTO);
  }

  public static void deleteOneTestStudent() throws SQLException {
    AccountDAOTest.deleteOneTestStudentAccount();
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

      StudentDTO createdStudent = STUDENT_DAO.getOne(STUDENT_DTO.getID());

      Assertions.assertNotNull(createdStudent);
      Assertions.assertEquals(STUDENT_DTO.getID(), createdStudent.getID());
      Assertions.assertEquals(STUDENT_DTO.getEmail(), createdStudent.getEmail());
      Assertions.assertEquals(STUDENT_DTO.getName(), createdStudent.getName());
      Assertions.assertEquals(STUDENT_DTO.getPaternalLastName(), createdStudent.getPaternalLastName());
      Assertions.assertEquals(STUDENT_DTO.getMaternalLastName(), createdStudent.getMaternalLastName());
      Assertions.assertInstanceOf(LocalDateTime.class, createdStudent.getCreatedAt());
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

      StudentDTO student = STUDENT_DAO.getOne(STUDENT_DTO.getID());

      Assertions.assertNotNull(student);
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
        .build();
      STUDENT_DAO.updateOne(updatedStudent);

      StudentDTO student = STUDENT_DAO.getOne(updatedStudent.getID());
      Assertions.assertEquals(updatedStudent.getName(), student.getName());
      Assertions.assertEquals(updatedStudent.getPaternalLastName(), student.getPaternalLastName());
      Assertions.assertEquals(updatedStudent.getMaternalLastName(), student.getMaternalLastName());
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
