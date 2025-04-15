package db;

import org.example.business.AcademicDTO;
import org.example.db.AcademicDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AcademicDAOTest {
  public static final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
  public static final AcademicDTO ACADEMIC_DTO = new AcademicDTO.AcademicBuilder()
    .setID("00010")
    .setEmail(AccountDAOTest.ACADEMIC_ACCOUNT_DTO.email())
    .setName("John")
    .setPaternalLastName("Doe")
    .setMaternalLastName("Smith")
    .setRole("PROFESSOR")
    .build();

  public static void createOneTestAcademic() throws SQLException {
    AccountDAOTest.createOneTestAcademicAccount();
    ACADEMIC_DAO.createOne(ACADEMIC_DTO);
  }

  public static void deleteOneTestAcademic() throws SQLException {
    AccountDAOTest.deleteOneTestAcademicAccount();
    ACADEMIC_DAO.deleteOne(ACADEMIC_DTO.getID());
  }

  @AfterEach
  public void tearDown() throws SQLException {
    deleteOneTestAcademic();
  }

  @Test
  public void testCreateOneAcademic() {
    assertDoesNotThrow(() -> {
      createOneTestAcademic();

      AcademicDTO createdAcademic = ACADEMIC_DAO.getOne(ACADEMIC_DTO.getID());

      Assertions.assertNotNull(createdAcademic);
      Assertions.assertEquals(ACADEMIC_DTO.getID(), createdAcademic.getID());
      Assertions.assertEquals(ACADEMIC_DTO.getEmail(), createdAcademic.getEmail());
      Assertions.assertEquals(ACADEMIC_DTO.getName(), createdAcademic.getName());
      Assertions.assertEquals(ACADEMIC_DTO.getPaternalLastName(), createdAcademic.getPaternalLastName());
      Assertions.assertEquals(ACADEMIC_DTO.getMaternalLastName(), createdAcademic.getMaternalLastName());
      Assertions.assertEquals(ACADEMIC_DTO.getRole(), createdAcademic.getRole());
      Assertions.assertInstanceOf(LocalDateTime.class, createdAcademic.getCreatedAt());
    });
  }

  @Test
  public void testGetAllAcademics() {
    assertDoesNotThrow(() -> {
      createOneTestAcademic();

      List<AcademicDTO> academicList = ACADEMIC_DAO.getAll();

      Assertions.assertNotNull(academicList);
      Assertions.assertFalse(academicList.isEmpty());
    });
  }

  @Test
  public void testGetOneAcademic() {
    assertDoesNotThrow(() -> {
      createOneTestAcademic();

      AcademicDTO academic = ACADEMIC_DAO.getOne(ACADEMIC_DTO.getID());

      Assertions.assertNotNull(academic);
    });
  }

  @Test
  public void testUpdateOneAcademic() {
    assertDoesNotThrow(() -> {
      createOneTestAcademic();

      AcademicDTO updatedAcademic = new AcademicDTO.AcademicBuilder()
        .setID(ACADEMIC_DTO.getID())
        .setEmail(ACADEMIC_DTO.getEmail())
        .setName("John Halo")
        .setPaternalLastName("Does")
        .setMaternalLastName("Smithson")
        .setState("RETIRED")
        .setRole(ACADEMIC_DTO.getRole())
        .build();
      ACADEMIC_DAO.updateOne(updatedAcademic);

      AcademicDTO academic = ACADEMIC_DAO.getOne(updatedAcademic.getID());
      Assertions.assertEquals(updatedAcademic.getName(), academic.getName());
      Assertions.assertEquals(updatedAcademic.getPaternalLastName(), academic.getPaternalLastName());
      Assertions.assertEquals(updatedAcademic.getMaternalLastName(), academic.getMaternalLastName());
      Assertions.assertEquals(updatedAcademic.getState(), academic.getState());
      Assertions.assertEquals(updatedAcademic.getRole(), academic.getRole());
    });
  }

  @Test
  public void testUpdateOneAcademicRole() {
    assertDoesNotThrow(() -> {
      createOneTestAcademic();

      AcademicDTO updatedAcademic = new AcademicDTO.AcademicBuilder()
        .setID(ACADEMIC_DTO.getID())
        .setEmail(ACADEMIC_DTO.getEmail())
        .setName(ACADEMIC_DTO.getName())
        .setPaternalLastName(ACADEMIC_DTO.getPaternalLastName())
        .setMaternalLastName(ACADEMIC_DTO.getMaternalLastName())
        .setRole("EVALUATOR")
        .setState("ACTIVE")
        .build();
      ACADEMIC_DAO.updateOne(updatedAcademic);

      AcademicDTO academic = ACADEMIC_DAO.getOne(updatedAcademic.getID());
      Assertions.assertEquals(updatedAcademic.getRole(), academic.getRole());

      updatedAcademic = new AcademicDTO.AcademicBuilder()
        .setID(ACADEMIC_DTO.getID())
        .setEmail(ACADEMIC_DTO.getEmail())
        .setName(ACADEMIC_DTO.getName())
        .setPaternalLastName(ACADEMIC_DTO.getPaternalLastName())
        .setMaternalLastName(ACADEMIC_DTO.getMaternalLastName())
        .setRole("EVALUATOR-PROFESSOR")
        .setState("ACTIVE")
        .build();

      ACADEMIC_DAO.updateOne(updatedAcademic);
      academic = ACADEMIC_DAO.getOne(updatedAcademic.getID());
      Assertions.assertEquals(updatedAcademic.getRole(), academic.getRole());
    });
  }

  @Test
  public void testDeleteOneAcademic() {
    assertDoesNotThrow(() -> {
      createOneTestAcademic();
      AcademicDTO createdAcademic = ACADEMIC_DAO.getOne(ACADEMIC_DTO.getID());
      Assertions.assertNotNull(createdAcademic);

      deleteOneTestAcademic();
      AcademicDTO deletedStudent = ACADEMIC_DAO.getOne(ACADEMIC_DTO.getID());
      Assertions.assertNull(deletedStudent);
    });
  }
}
