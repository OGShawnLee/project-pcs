package db;

import org.example.business.dto.AcademicDTO;
import org.example.business.dao.AcademicDAO;
import org.example.business.dto.AccountDTO;
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
    .setRole(AcademicDTO.Role.ACADEMIC)
    .setPhoneNumber("2281904050")
    .setState("ACTIVE")
    .build();

  public static void createOneTestAcademic() throws SQLException {
    ACADEMIC_DAO.createOne(ACADEMIC_DTO);
  }

  public static void deleteOneTestAcademic() throws SQLException {
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
      Assertions.assertEquals(ACADEMIC_DTO, ACADEMIC_DAO.getOne(ACADEMIC_DTO.getID()));
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
      Assertions.assertEquals(ACADEMIC_DTO, ACADEMIC_DAO.getOne(ACADEMIC_DTO.getID()));
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
        .setPhoneNumber("2281904051")
        .setRole(ACADEMIC_DTO.getRole())
        .build();

      ACADEMIC_DAO.updateOne(updatedAcademic);

      Assertions.assertEquals(updatedAcademic, ACADEMIC_DAO.getOne(updatedAcademic.getID()));
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
        .setRole(AcademicDTO.Role.ACADEMIC_EVALUATOR)
        .setPhoneNumber(ACADEMIC_DTO.getPhoneNumber())
        .setState("ACTIVE")
        .build();

      ACADEMIC_DAO.updateOne(updatedAcademic);
      AccountDTO updatedAccount = AccountDAOTest.ACCOUNT_DAO.getOne(ACADEMIC_DTO.getEmail());
      Assertions.assertEquals(updatedAccount.role().toString(), updatedAcademic.getRole().toString());
      Assertions.assertEquals(updatedAcademic, ACADEMIC_DAO.getOne(updatedAcademic.getID()));

      updatedAcademic = new AcademicDTO.AcademicBuilder()
        .setID(ACADEMIC_DTO.getID())
        .setEmail(ACADEMIC_DTO.getEmail())
        .setName(ACADEMIC_DTO.getName())
        .setPaternalLastName(ACADEMIC_DTO.getPaternalLastName())
        .setMaternalLastName(ACADEMIC_DTO.getMaternalLastName())
        .setRole(AcademicDTO.Role.ACADEMIC_EVALUATOR)
        .setPhoneNumber(ACADEMIC_DTO.getPhoneNumber())
        .setState("ACTIVE")
        .build();

      ACADEMIC_DAO.updateOne(updatedAcademic);
      updatedAccount = AccountDAOTest.ACCOUNT_DAO.getOne(ACADEMIC_DTO.getEmail());
      Assertions.assertEquals(updatedAccount.role().toString(), updatedAcademic.getRole().toString());
      Assertions.assertEquals(updatedAcademic, ACADEMIC_DAO.getOne(updatedAcademic.getID()));
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
