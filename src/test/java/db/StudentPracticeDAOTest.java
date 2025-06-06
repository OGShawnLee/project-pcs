package db;

import org.example.business.dao.StudentPracticeDAO;
import org.example.business.dto.StudentPracticeDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class StudentPracticeDAOTest {
  @AfterEach
  public void tearDown() throws SQLException {
    PracticeDAOTest.deleteOneTestPractice();
  }

  @Test
  public void testGetAllByProjectID() {
    assertDoesNotThrow(() -> {
      PracticeDAOTest.createOneTestPractice();
      List<StudentPracticeDTO> studentPracticeList = StudentPracticeDAO.getAllByProjectID(
        ProjectDAOTest.PROJECT_DTO.getID()
      );
      Assertions.assertNotNull(studentPracticeList);
      Assertions.assertFalse(studentPracticeList.isEmpty());
    });
  }
}
