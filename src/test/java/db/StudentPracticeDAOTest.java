package db;

import org.example.business.dao.StudentPracticeDAO;
import org.example.business.dto.StudentPracticeDTO;
import org.example.common.UserDisplayableException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class StudentPracticeDAOTest {
  private static final StudentPracticeDAO STUDENT_PRACTICE_DAO = new StudentPracticeDAO();
  @AfterEach
  public void tearDown() throws UserDisplayableException {
    PracticeDAOTest.deleteOneTestPractice();
  }

  @Test
  public void testGetAllByProjectID() {
    assertDoesNotThrow(() -> {
      PracticeDAOTest.createOneTestPractice();
      List<StudentPracticeDTO> studentPracticeList = STUDENT_PRACTICE_DAO.getAllByProjectID(
        ProjectDAOTest.PROJECT_DTO.getID()
      );
      Assertions.assertNotNull(studentPracticeList);
      Assertions.assertFalse(studentPracticeList.isEmpty());
    });
  }
}
