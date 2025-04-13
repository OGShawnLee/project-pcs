package db;

import org.example.business.CourseDTO;
import org.example.db.CourseDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CourseDAOTest extends TestContext {
  private static final CourseDAO COURSE_DAO = new CourseDAO();
  private static final CourseDTO COURSE_DTO = new CourseDTO.CourseBuilder()
    .setNRC("00010")
    .setIDAcademic(ACADEMIC_DTO.getID())
    .setSection("A")
    .setStartedAt(LocalDateTime.now())
    .setEndedAt(LocalDateTime.now().plusMonths(4))
    .build();

  private static void createTestData() throws SQLException {
    createAcademic();
    COURSE_DAO.create(COURSE_DTO);
  }

  @AfterEach
  public void tearDown() throws SQLException {
    deleteAcademic();
    COURSE_DAO.delete(COURSE_DTO.getNRC());
  }

  @Test
  public void testCreateCourse() {
    assertDoesNotThrow(() -> {
      createTestData();

      CourseDTO createdCourse = COURSE_DAO.get(COURSE_DTO.getNRC());

      Assertions.assertNotNull(createdCourse);
      Assertions.assertEquals(COURSE_DTO.getNRC(), createdCourse.getNRC());
      Assertions.assertEquals(COURSE_DTO.getIDAcademic(), createdCourse.getIDAcademic());
      Assertions.assertEquals(COURSE_DTO.getSection(), createdCourse.getSection());
      Assertions.assertInstanceOf(LocalDateTime.class, createdCourse.getStartedAt());
      Assertions.assertInstanceOf(LocalDateTime.class, createdCourse.getEndedAt());
    });
  }

  @Test
  public void testGetAllCourses() {
    assertDoesNotThrow(() -> {
      createTestData();

      List<CourseDTO> allCourses = COURSE_DAO.getAll();

      Assertions.assertNotNull(allCourses);
      Assertions.assertFalse(allCourses.isEmpty());
    });
  }

  @Test
  public void testGetCourse() {
    assertDoesNotThrow(() -> {
      createTestData();

      CourseDTO course = COURSE_DAO.get(COURSE_DTO.getNRC());

      Assertions.assertNotNull(course);
      Assertions.assertEquals(COURSE_DTO.getNRC(), course.getNRC());
      Assertions.assertEquals(COURSE_DTO.getIDAcademic(), course.getIDAcademic());
      Assertions.assertEquals(COURSE_DTO.getSection(), course.getSection());
      Assertions.assertInstanceOf(LocalDateTime.class, course.getStartedAt());
      Assertions.assertInstanceOf(LocalDateTime.class, course.getEndedAt());
    });
  }

  @Test
  public void testUpdateCourse() {
    assertDoesNotThrow(() -> {
      createTestData();

      CourseDTO updatedCourse = new CourseDTO.CourseBuilder()
        .setNRC(COURSE_DTO.getNRC())
        .setIDAcademic(COURSE_DTO.getIDAcademic())
        .setSection("B")
        .setStartedAt(LocalDateTime.now().plusDays(1))
        .setEndedAt(LocalDateTime.now().plusMonths(5))
        .build();

      COURSE_DAO.update(updatedCourse);

      CourseDTO course = COURSE_DAO.get(COURSE_DTO.getNRC());

      Assertions.assertEquals(updatedCourse.getSection(), course.getSection());
      Assertions.assertInstanceOf(LocalDateTime.class, course.getStartedAt());
      Assertions.assertInstanceOf(LocalDateTime.class, course.getEndedAt());
    });
  }

  @Test
  public void testDeleteCourse() {
    assertDoesNotThrow(() -> {
      createTestData();

      COURSE_DAO.delete(COURSE_DTO.getNRC());

      CourseDTO deletedCourse = COURSE_DAO.get(COURSE_DTO.getNRC());

      Assertions.assertNull(deletedCourse);
    });
  }
}
