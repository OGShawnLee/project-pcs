package db;

import org.example.business.dto.AccountDTO;
import org.example.business.dao.AccountDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountDAOTest {
  public static final AccountDAO ACCOUNT_DAO = new AccountDAO();
  public static final AccountDTO ACADEMIC_ACCOUNT_DTO = new AccountDTO("smith@uv.mx", "123Password");
  public static final AccountDTO STUDENT_ACCOUNT_DTO = new AccountDTO("zS18014115@estudiantes.uv.mx", "Password123");

  public static void createOneTestAcademicAccount() throws SQLException {
    ACCOUNT_DAO.createOne(ACADEMIC_ACCOUNT_DTO);
  }

  public static void createOneTestStudentAccount() throws SQLException {
    ACCOUNT_DAO.createOne(STUDENT_ACCOUNT_DTO);
  }

  public static void deleteOneTestAcademicAccount() throws SQLException {
    ACCOUNT_DAO.deleteOne(ACADEMIC_ACCOUNT_DTO.email());
  }

  public static void deleteOneTestStudentAccount() throws SQLException {
    ACCOUNT_DAO.deleteOne(STUDENT_ACCOUNT_DTO.email());
  }

  @AfterEach
  public void tearDown() throws SQLException {
    deleteOneTestAcademicAccount();
  }

  @Test
  void testCreateOneAccount() {
    assertDoesNotThrow(() -> {
      createOneTestAcademicAccount();
      Assertions.assertEquals(ACADEMIC_ACCOUNT_DTO, ACCOUNT_DAO.getOne(ACADEMIC_ACCOUNT_DTO.email()));
    });
  }

  @Test
  void testGetAllAccounts() {
    assertDoesNotThrow(() -> {
      createOneTestAcademicAccount();

      List<AccountDTO> accountList = ACCOUNT_DAO.getAll();

      Assertions.assertNotNull(accountList);
      Assertions.assertFalse(accountList.isEmpty());
    });
  }

  @Test
  void testGetOneAccount() {
    assertDoesNotThrow(() -> {
      createOneTestAcademicAccount();
      Assertions.assertEquals(ACADEMIC_ACCOUNT_DTO, ACCOUNT_DAO.getOne(ACADEMIC_ACCOUNT_DTO.email()));
    });
  }

  @Test
  void testUpdateOneAccount() {
    assertDoesNotThrow(() -> {
      createOneTestAcademicAccount();

      AccountDTO updatedAccount = new AccountDTO(ACADEMIC_ACCOUNT_DTO.email(), "SecurePassword");
      ACCOUNT_DAO.updateOne(updatedAccount);

      Assertions.assertEquals(updatedAccount, ACCOUNT_DAO.getOne(ACADEMIC_ACCOUNT_DTO.email()));
    });
  }

  @Test
  void testDeleteOneAccount() {
    assertDoesNotThrow(() -> {
      createOneTestAcademicAccount();
      AccountDTO createdAccount = ACCOUNT_DAO.getOne(ACADEMIC_ACCOUNT_DTO.email());
      Assertions.assertNotNull(createdAccount);

      deleteOneTestAcademicAccount();
      AccountDTO deletedAccount = ACCOUNT_DAO.getOne(ACADEMIC_ACCOUNT_DTO.email());
      Assertions.assertNull(deletedAccount);
    });
  }
}
