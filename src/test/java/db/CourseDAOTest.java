package db;

import org.example.business.dto.CourseDTO;
import org.example.business.dao.CourseDAO;
import org.example.business.dto.enumeration.CourseState;
import org.example.business.dto.enumeration.Section;
import org.example.business.dto.enumeration.Semester;
import org.example.common.UserDisplayableException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CourseDAOTest {
  public static final CourseDAO COURSE_DAO = new CourseDAO();
  public static final CourseDTO COURSE_DTO = new CourseDTO.CourseBuilder()
    .setNRC("00010")
    .setIDAcademic(AcademicDAOTest.ACADEMIC_DTO.getID())
    .setSection(Section.S1)
    .setSemester(Semester.FEB_JUL)
    .setState(CourseState.ON_GOING)
    .setTotalStudents(0)
    .build();

  public static void createOneTestCourse() throws UserDisplayableException {
    AcademicDAOTest.createOneTestAcademic();
    COURSE_DAO.createOne(COURSE_DTO);
  }

  public static void deleteOneTestCourse() throws UserDisplayableException {
    AcademicDAOTest.deleteOneTestAcademic();
    COURSE_DAO.deleteOne(COURSE_DTO.getNRC());
  }

  @AfterEach
  public void tearDown() throws UserDisplayableException {
    deleteOneTestCourse();
  }

  @Test
  public void testCreateOneCourse() {
    assertDoesNotThrow(() -> {
      createOneTestCourse();
      Assertions.assertEquals(COURSE_DTO, COURSE_DAO.getOne(COURSE_DTO.getNRC()));
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
      Assertions.assertEquals(COURSE_DTO, COURSE_DAO.getOne(COURSE_DTO.getNRC()));
    });
  }

  @Test
  public void testUpdateOneCourse() {
    assertDoesNotThrow(() -> {
      createOneTestCourse();

      CourseDTO updatedCourse = new CourseDTO.CourseBuilder()
        .setNRC(COURSE_DTO.getNRC())
        .setIDAcademic(COURSE_DTO.getIDAcademic())
        .setSection(Section.S1)
        .setSemester(Semester.FEB_JUL)
        .setState(CourseState.COMPLETED)
        .build();

      COURSE_DAO.updateOne(updatedCourse);

      Assertions.assertEquals(updatedCourse, COURSE_DAO.getOne(updatedCourse.getNRC()));
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
