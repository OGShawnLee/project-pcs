package db;

import jdk.jshell.Snippet;
import org.example.business.dto.EvaluationDTO;
import org.example.business.dao.EvaluationDAO;
import org.example.business.dao.filter.FilterEvaluation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EvaluationDAOTest {
  public static final EvaluationDAO EVALUATION_DAO = new EvaluationDAO();
  public static final EvaluationDTO EVALUATION_DTO = new EvaluationDTO.EvaluationBuilder()
    .setIDAcademic(AcademicDAOTest.ACADEMIC_DTO.getID())
    .setIDStudent(StudentDAOTest.STUDENT_DTO.getID())
    .setContentCongruenceGrade("0")
    .setMethodologicalRigorGrade("1")
    .setAdequateUseGrade("2")
    .setWritingGrade("3")
    .setKind(EvaluationDTO.Kind.SECOND_PERIOD)
    .setFeedback("I have never seen a project like this before, it is amazing!")
    .build();

  public static void createOneTestEvaluation() throws SQLException {
    AcademicDAOTest.createOneTestAcademic();
    ProjectDAOTest.createOneTestProject();
    StudentDAOTest.createOneTestStudent();
    EVALUATION_DTO.setIDProject(ProjectDAOTest.PROJECT_DTO.getID());
    EVALUATION_DAO.createOne(EVALUATION_DTO);
  }

  public static void deleteOneTestEvaluation() throws SQLException {
    StudentDAOTest.deleteOneTestStudent();
    ProjectDAOTest.deleteOneTestProject();
    AcademicDAOTest.deleteOneTestAcademic();
    EVALUATION_DAO.deleteOne(
      new FilterEvaluation(
        EVALUATION_DTO.getIDProject(),
        EVALUATION_DTO.getIDStudent(),
        EVALUATION_DTO.getIDAcademic()
      )
    );
  }

  @AfterEach
  public void tearDown() throws SQLException {
    deleteOneTestEvaluation();
  }

  @Test
  public void testCreateOneEvaluation() {
    assertDoesNotThrow(() -> {
      createOneTestEvaluation();

      EvaluationDTO createdEvaluation = EVALUATION_DAO.getOne(
        new FilterEvaluation(
          EVALUATION_DTO.getIDProject(),
          EVALUATION_DTO.getIDStudent(),
          EVALUATION_DTO.getIDAcademic()
        )
      );

      assertEquals(EVALUATION_DTO, createdEvaluation);
    });
  }

  @Test
  public void testGetAllEvaluations() {
    assertDoesNotThrow(() -> {
      createOneTestEvaluation();

      List<EvaluationDTO> evaluationList = EVALUATION_DAO.getAll();

      assertNotNull(evaluationList);
      assertFalse(evaluationList.isEmpty());
    });
  }

  @Test
  public void testGetOneEvaluation() {
    assertDoesNotThrow(() -> {
      createOneTestEvaluation();

      EvaluationDTO retrievedEvaluation = EVALUATION_DAO.getOne(
        new FilterEvaluation(
          EVALUATION_DTO.getIDProject(),
          EVALUATION_DTO.getIDStudent(),
          EVALUATION_DTO.getIDAcademic()
        )
      );

      assertEquals(EVALUATION_DTO, retrievedEvaluation);
    });
  }

  @Test
  public void testUpdateOneEvaluation() {
    assertDoesNotThrow(() -> {
      createOneTestEvaluation();

      EvaluationDTO updatedEvaluation = new EvaluationDTO.EvaluationBuilder()
        .setIDProject(EVALUATION_DTO.getIDProject())
        .setIDStudent(EVALUATION_DTO.getIDStudent())
        .setIDAcademic(EVALUATION_DTO.getIDAcademic())
        .setContentCongruenceGrade("4")
        .setMethodologicalRigorGrade("5")
        .setAdequateUseGrade("6")
        .setWritingGrade("7")
        .setFeedback("This project is good, but it can be improved.")
        .setKind(EvaluationDTO.Kind.FIRST_PERIOD)
        .build();

      EVALUATION_DAO.updateOne(updatedEvaluation);

      EvaluationDTO retrievedEvaluation = EVALUATION_DAO.getOne(
        new FilterEvaluation(
          updatedEvaluation.getIDProject(),
          updatedEvaluation.getIDStudent(),
          updatedEvaluation.getIDAcademic()
        )
      );

      assertEquals(updatedEvaluation, retrievedEvaluation);
    });
  }

  @Test
  public void testDeleteOneEvaluation() {
    assertDoesNotThrow(() -> {
      createOneTestEvaluation();
      EvaluationDTO createdEvaluation = EVALUATION_DAO.getOne(
        new FilterEvaluation(
          EVALUATION_DTO.getIDProject(),
          EVALUATION_DTO.getIDStudent(),
          EVALUATION_DTO.getIDAcademic()
        )
      );
      assertNotNull(createdEvaluation);

      deleteOneTestEvaluation();
      EvaluationDTO deletedEvaluation = EVALUATION_DAO.getOne(
        new FilterEvaluation(
          EVALUATION_DTO.getIDProject(),
          EVALUATION_DTO.getIDStudent(),
          EVALUATION_DTO.getIDAcademic()
        )
      );
      assertNull(deletedEvaluation);
    });
  }
}
