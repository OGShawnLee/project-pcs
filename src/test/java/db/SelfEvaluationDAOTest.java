package db;

import org.example.business.dao.SelfEvaluationDAO;
import org.example.business.dto.SelfEvaluationDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SelfEvaluationDAOTest {
  public static final SelfEvaluationDAO SELF_EVALUATION_DAO = new SelfEvaluationDAO();
  public static final SelfEvaluationDTO SELF_EVALUATION_DTO = new SelfEvaluationDTO.SelfEvaluationBuilder()
    .setIDStudent(StudentDAOTest.STUDENT_DTO.getID())
    .setCongruentGrade(5)
    .setFollowUpGrade(5)
    .setInformedByOrganization(5)
    .setInterestingGrade(5)
    .setSafetyGrade(5)
    .setProductivityGrade(5)
    .setKnowledgeApplicationGrade(5)
    .setRegulatedByOrganization(5)
    .setImportanceForProfessionalDevelopment(5)
    .build();

  public static void createOneTestSelfEvaluation() throws SQLException {
    StudentDAOTest.createOneTestStudent();
    SELF_EVALUATION_DAO.createOne(SELF_EVALUATION_DTO);
  }

  public static void deleteOneTestSelfEvaluation() throws SQLException {
    StudentDAOTest.deleteOneTestStudent();
    SELF_EVALUATION_DAO.deleteOne(StudentDAOTest.STUDENT_DTO.getID());
  }

  @AfterEach
  public void tearDown() throws SQLException {
    deleteOneTestSelfEvaluation();
  }

  @Test
  public void testCreateOneSelfEvaluation() {
    assertDoesNotThrow(() -> {
      createOneTestSelfEvaluation();
      SelfEvaluationDTO selfEvaluation = SELF_EVALUATION_DAO.getOne(StudentDAOTest.STUDENT_DTO.getID());
      Assertions.assertEquals(selfEvaluation, SELF_EVALUATION_DTO);
    });
  }

  @Test
  public void testGetAllSelfEvaluations() {
    assertDoesNotThrow(() -> {
      createOneTestSelfEvaluation();

      List<SelfEvaluationDTO> selfEvaluationList = SELF_EVALUATION_DAO.getAll();

      Assertions.assertNotNull(selfEvaluationList);
      Assertions.assertFalse(selfEvaluationList.isEmpty());
    });
  }

  @Test
  public void testGetOneSelfEvaluation() {
    assertDoesNotThrow(() -> {
      createOneTestSelfEvaluation();
      SelfEvaluationDTO selfEvaluation = SELF_EVALUATION_DAO.getOne(StudentDAOTest.STUDENT_DTO.getID());
      Assertions.assertEquals(selfEvaluation, SELF_EVALUATION_DTO);
    });
  }

  @Test
  public void testUpdateOneSelfEvaluation() {
    assertDoesNotThrow(() -> {
      createOneTestSelfEvaluation();

      SelfEvaluationDTO updatedSelfEvaluation = new SelfEvaluationDTO.SelfEvaluationBuilder()
        .setIDStudent(StudentDAOTest.STUDENT_DTO.getID())
        .setCongruentGrade(4)
        .setFollowUpGrade(4)
        .setInformedByOrganization(4)
        .setInterestingGrade(4)
        .setSafetyGrade(4)
        .setProductivityGrade(4)
        .setKnowledgeApplicationGrade(4)
        .setRegulatedByOrganization(4)
        .setImportanceForProfessionalDevelopment(4)
        .build();

      SELF_EVALUATION_DAO.updateOne(updatedSelfEvaluation);

      SelfEvaluationDTO selfEvaluation = SELF_EVALUATION_DAO.getOne(StudentDAOTest.STUDENT_DTO.getID());
      Assertions.assertEquals(selfEvaluation, updatedSelfEvaluation);
    });
  }

  @Test
  public void testDeleteOneSelfEvaluation() {
    assertDoesNotThrow(() -> {
      createOneTestSelfEvaluation();
      SELF_EVALUATION_DAO.deleteOne(StudentDAOTest.STUDENT_DTO.getID());
      Assertions.assertNull(SELF_EVALUATION_DAO.getOne(StudentDAOTest.STUDENT_DTO.getID()));
    });
  }
}
