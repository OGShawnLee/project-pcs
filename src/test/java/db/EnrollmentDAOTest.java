package db;


import org.example.business.dto.EnrollmentDTO;
import org.example.business.dao.EnrollmentDAO;
import org.example.business.dao.filter.FilterEnrollment;
import org.example.common.UserDisplayableException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class EnrollmentDAOTest {
  public static final EnrollmentDAO ENROLLMENT_DAO = new EnrollmentDAO();
  public static final EnrollmentDTO ENROLLMENT_DTO = new EnrollmentDTO.EnrollmentBuilder()
    .setIDAcademic(AcademicDAOTest.ACADEMIC_DTO.getID())
    .setIDCourse(CourseDAOTest.COURSE_DTO.getNRC())
    .setIDStudent(StudentDAOTest.STUDENT_DTO.getID())
    .build();

  public static void createOneTestEnrollment() throws UserDisplayableException {
    StudentDAOTest.createOneTestStudent();
    CourseDAOTest.createOneTestCourse();
    ENROLLMENT_DAO.createOne(ENROLLMENT_DTO);
  }

  public static void deleteOneTestEnrollment() throws UserDisplayableException {
    StudentDAOTest.deleteOneTestStudent();
    CourseDAOTest.deleteOneTestCourse();
    ENROLLMENT_DAO.deleteOne(
      new FilterEnrollment(
        ENROLLMENT_DTO.getIDStudent(),
        ENROLLMENT_DTO.getIDCourse()
      )
    );
  }

  @AfterEach
  public void tearDown() throws UserDisplayableException {
    deleteOneTestEnrollment();
  }

  @Test
  public void testCreateOneEnrollment() {
    assertDoesNotThrow(() -> {
      createOneTestEnrollment();

      EnrollmentDTO createdEnrollment = ENROLLMENT_DAO.getOne(
        new FilterEnrollment(
          ENROLLMENT_DTO.getIDStudent(),
          ENROLLMENT_DTO.getIDCourse()
        )
      );

      Assertions.assertEquals(ENROLLMENT_DTO, createdEnrollment);
    });
  }

  @Test
  public void testGetAllEnrollments() {
    assertDoesNotThrow(() -> {
      createOneTestEnrollment();

      List<EnrollmentDTO> enrollmentList = ENROLLMENT_DAO.getAll();

      Assertions.assertNotNull(enrollmentList);
      Assertions.assertFalse(enrollmentList.isEmpty());
    });
  }

  @Test
  public void testGetOneEnrollment() {
    assertDoesNotThrow(() -> {
      createOneTestEnrollment();

      EnrollmentDTO createdEnrollment = ENROLLMENT_DAO.getOne(
        new FilterEnrollment(
          ENROLLMENT_DTO.getIDStudent(),
          ENROLLMENT_DTO.getIDCourse()
        )
      );

      Assertions.assertEquals(ENROLLMENT_DTO, createdEnrollment);
    });
  }

  @Test
  public void testDeleteOneEnrollment() {
    assertDoesNotThrow(() -> {
      createOneTestEnrollment();
      EnrollmentDTO createdEnrollment = ENROLLMENT_DAO.getOne(
        new FilterEnrollment(
          ENROLLMENT_DTO.getIDStudent(),
          ENROLLMENT_DTO.getIDCourse()
        )
      );
      Assertions.assertNotNull(createdEnrollment);

      deleteOneTestEnrollment();
      createdEnrollment = ENROLLMENT_DAO.getOne(
        new FilterEnrollment(
          ENROLLMENT_DTO.getIDStudent(),
          ENROLLMENT_DTO.getIDCourse()
        )
      );
      Assertions.assertNull(createdEnrollment);
    });
  }
}
