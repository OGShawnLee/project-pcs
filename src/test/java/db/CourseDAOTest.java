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

  private static void createOneTestData() throws SQLException {
    createAcademic();
    COURSE_DAO.createOne(COURSE_DTO);
  }

  @AfterEach
  public void tearDown() throws SQLException {
    deleteAcademic();
    COURSE_DAO.deleteOne(COURSE_DTO.getNRC());
  }

  @Test
  public void testCreateOneCourse() {
    assertDoesNotThrow(() -> {
      createOneTestData();

      CourseDTO createdCourse = COURSE_DAO.getOne(COURSE_DTO.getNRC());

      Assertions.assertNotNull(createdCourse);
      Assertions.assertEquals(COURSE_DTO.getNRC(), createdCourse.getNRC());
      Assertions.assertEquals(COURSE_DTO.getIDAcademic(), createdCourse.getIDAcademic());
      Assertions.assertEquals(COURSE_DTO.getSection(), createdCourse.getSection());
      Assertions.assertInstanceOf(LocalDateTime.class, createdCourse.getStartedAt());
      Assertions.assertInstanceOf(LocalDateTime.class, createdCourse.getEndedAt());
    });
  }

  @Test
  public void testGetOneAllCourses() {
    assertDoesNotThrow(() -> {
      createOneTestData();

      List<CourseDTO> allCourses = COURSE_DAO.getAll();

      Assertions.assertNotNull(allCourses);
      Assertions.assertFalse(allCourses.isEmpty());
    });
  }

  @Test
  public void testGetOneCourse() {
    assertDoesNotThrow(() -> {
      createOneTestData();

      CourseDTO course = COURSE_DAO.getOne(COURSE_DTO.getNRC());

      Assertions.assertNotNull(course);
      Assertions.assertEquals(COURSE_DTO.getNRC(), course.getNRC());
      Assertions.assertEquals(COURSE_DTO.getIDAcademic(), course.getIDAcademic());
      Assertions.assertEquals(COURSE_DTO.getSection(), course.getSection());
      Assertions.assertInstanceOf(LocalDateTime.class, course.getStartedAt());
      Assertions.assertInstanceOf(LocalDateTime.class, course.getEndedAt());
    });
  }

  @Test
  public void testUpdateOneCourse() {
    assertDoesNotThrow(() -> {
      createOneTestData();

      CourseDTO updatedCourse = new CourseDTO.CourseBuilder()
        .setNRC(COURSE_DTO.getNRC())
        .setIDAcademic(COURSE_DTO.getIDAcademic())
        .setSection("B")
        .setStartedAt(LocalDateTime.now().plusDays(1))
        .setEndedAt(LocalDateTime.now().plusMonths(5))
        .build();

      COURSE_DAO.updateOne(updatedCourse);

      CourseDTO course = COURSE_DAO.getOne(COURSE_DTO.getNRC());

      Assertions.assertEquals(updatedCourse.getSection(), course.getSection());
      Assertions.assertInstanceOf(LocalDateTime.class, course.getStartedAt());
      Assertions.assertInstanceOf(LocalDateTime.class, course.getEndedAt());
    });
  }

  @Test
  public void testDeleteOneCourse() {
    assertDoesNotThrow(() -> {
      createOneTestData();

      COURSE_DAO.deleteOne(COURSE_DTO.getNRC());

      CourseDTO deletedCourse = COURSE_DAO.getOne(COURSE_DTO.getNRC());

      Assertions.assertNull(deletedCourse);
    });
  }
}
