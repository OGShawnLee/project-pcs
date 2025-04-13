package db;

import org.example.business.AccountDTO;
import org.example.business.StudentDTO;
import org.example.db.AccountDAO;
import org.example.db.StudentDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDAOTest {
  private final StudentDAO STUDENT_DAO = new StudentDAO();
  private final AccountDAO ACCOUNT_DAO = new AccountDAO();
  private final String ID = "18014115";
  private final String EMAIL = "zS18014115@estudiantes.mx";
  private final String PASSWORD = "Password123";
  private final String NAME = "Shawn";
  private final String PATERNAL_LAST_NAME = "Doe";
  private final String MATERNAL_LAST_NAME = "Lee";
  private final StudentDTO BASE_STUDENT_DTO = new StudentDTO.StudentBuilder()
    .setID(ID)
    .setEmail(EMAIL)
    .setName(NAME)
    .setPaternalLastName(PATERNAL_LAST_NAME)
    .setMaternalLastName(MATERNAL_LAST_NAME)
    .build();
  private final AccountDTO BASE_ACCOUNT_DTO = new AccountDTO(EMAIL, PASSWORD);

  private void createTestData() throws SQLException {
    ACCOUNT_DAO.create(BASE_ACCOUNT_DTO);
    STUDENT_DAO.create(BASE_STUDENT_DTO);
  }

  @AfterEach
  public void tearDown() throws SQLException {
    STUDENT_DAO.delete(ID);
    ACCOUNT_DAO.delete(EMAIL);
  }

  @Test
  public void testCreateStudent() {
    assertDoesNotThrow(() -> {
      createTestData();

      StudentDTO createdStudent = STUDENT_DAO.get(ID);

      Assertions.assertNotNull(createdStudent);
      Assertions.assertEquals(ID, createdStudent.getID());
      Assertions.assertEquals(EMAIL, createdStudent.getEmail());
      Assertions.assertEquals(NAME, createdStudent.getName());
      Assertions.assertEquals(PATERNAL_LAST_NAME, createdStudent.getPaternalLastName());
      Assertions.assertEquals(MATERNAL_LAST_NAME, createdStudent.getMaternalLastName());
      Assertions.assertInstanceOf(LocalDateTime.class, createdStudent.getCreatedAt());
    });
  }

  @Test
  public void testGetAllStudents() {
    assertDoesNotThrow(() -> {
      createTestData();

      List<StudentDTO> studentList = STUDENT_DAO.getAll();

      Assertions.assertNotNull(studentList);
      Assertions.assertFalse(studentList.isEmpty());
    });
  }

  @Test
  public void testGetStudent() {
    assertDoesNotThrow(() -> {
      ACCOUNT_DAO.create(BASE_ACCOUNT_DTO);
      STUDENT_DAO.create(BASE_STUDENT_DTO);

      StudentDTO student = STUDENT_DAO.get(ID);

      Assertions.assertNotNull(student);
    });
  }

  @Test
  public void testUpdateAcademic() {
    assertDoesNotThrow(() -> {
      createTestData();

      String updatedName = "John Halo";
      String updatedMaternalLastName = "Smithson";
      String updatedPaternalLastName = "Does";

      StudentDTO updatedStudent = new StudentDTO.StudentBuilder()
        .setID(ID)
        .setEmail(EMAIL)
        .setName(updatedName)
        .setPaternalLastName(updatedPaternalLastName)
        .setMaternalLastName(updatedMaternalLastName)
        .build();

      STUDENT_DAO.update(updatedStudent);

      StudentDTO retrievedStudent = STUDENT_DAO.get(ID);

      Assertions.assertEquals(updatedMaternalLastName, retrievedStudent.getMaternalLastName());
      Assertions.assertEquals(updatedPaternalLastName, retrievedStudent.getPaternalLastName());
      Assertions.assertEquals(updatedName, retrievedStudent.getName());
    });
  }

  @Test
  public void testDeleteAcademic() {
    assertDoesNotThrow(() -> {
      createTestData();

      ACCOUNT_DAO.delete(EMAIL);
      STUDENT_DAO.delete(ID);

      StudentDTO deleteStudent = STUDENT_DAO.get(ID);

      Assertions.assertNull(deleteStudent);
    });
  }
}
