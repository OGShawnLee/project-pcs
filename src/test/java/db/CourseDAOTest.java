package db;

import org.example.business.dto.CourseDTO;
import org.example.business.dao.CourseDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CourseDAOTest {
  public static final CourseDAO COURSE_DAO = new CourseDAO();
  public static final CourseDTO COURSE_DTO = new CourseDTO.CourseBuilder()
    .setNRC("00010")
    .setIDAcademic(AcademicDAOTest.ACADEMIC_DTO.getID())
    .setSection("A")
    .setStartedAt(LocalDateTime.now())
    .setEndedAt(LocalDateTime.now().plusMonths(4))
    .build();

  public static void createOneTestCourse() throws SQLException {
    AcademicDAOTest.createOneTestAcademic();
    COURSE_DAO.createOne(COURSE_DTO);
  }

  public static void deleteOneTestCourse() throws SQLException {
    AcademicDAOTest.deleteOneTestAcademic();
    COURSE_DAO.deleteOne(COURSE_DTO.getNRC());
  }

  @AfterEach
  public void tearDown() throws SQLException {
    deleteOneTestCourse();
  }

  @Test
  public void testCreateOneCourse() {
    assertDoesNotThrow(() -> {
      createOneTestCourse();

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
  public void testGetAllCourses() {
    assertDoesNotThrow(() -> {
      createOneTestCourse();

      List<CourseDTO> courseList = COURSE_DAO.getAll();

      Assertions.assertNotNull(courseList);
      Assertions.assertFalse(courseList.isEmpty());
    });
  }

  @Test
  public void testGetOneCourse() {
    assertDoesNotThrow(() -> {
      createOneTestCourse();

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
  public void testUpdateOneCourse() {
    assertDoesNotThrow(() -> {
      createOneTestCourse();

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
      createOneTestCourse();
      CourseDTO createdCourse = COURSE_DAO.getOne(COURSE_DTO.getNRC());
      Assertions.assertNotNull(createdCourse);

      deleteOneTestCourse();
      CourseDTO deletedCourse = COURSE_DAO.getOne(COURSE_DTO.getNRC());
      Assertions.assertNull(deletedCourse);
    });
  }
}
