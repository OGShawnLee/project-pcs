package db;

import org.example.business.AccountDTO;
import org.example.db.AccountDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountDAOTest {
  private AccountDAO ACCOUNT_DAO;
  private final String ACC_EMAIL = "test@example.com";
  private final String ACC_PASSWORD = "Password123";

  @BeforeEach
  public void setUp() {
    ACCOUNT_DAO = new AccountDAO();
  }

  @Test
  void testCreateAccount() {
    assertDoesNotThrow(() -> {
      AccountDTO account = new AccountDTO(ACC_EMAIL, ACC_PASSWORD);

      ACCOUNT_DAO.create(account);

      AccountDTO createdAccount = ACCOUNT_DAO.get(ACC_EMAIL);

      Assertions.assertNotNull(createdAccount);
      Assertions.assertEquals(ACC_EMAIL, createdAccount.email());
      Assertions.assertEquals(ACC_PASSWORD, createdAccount.password());
    });
  }

  @Test
  void testGetAllAccounts() {
    assertDoesNotThrow(() -> {
      List<AccountDTO> accountList = ACCOUNT_DAO.getAll();

      Assertions.assertNotNull(accountList);
      Assertions.assertFalse(accountList.isEmpty());
    });
  }

  @Test
  void testGetAccount() {
    assertDoesNotThrow(() -> {
      AccountDTO account = ACCOUNT_DAO.get(ACC_EMAIL);

      Assertions.assertNotNull(account);
    });
  }

  @Test
  void testUpdateAccount() {
    assertDoesNotThrow(() -> {
      String updatedPassword = "UpdatedPassword123";
      AccountDTO account = new AccountDTO(ACC_EMAIL, updatedPassword);

      ACCOUNT_DAO.update(account);

      AccountDTO updatedAccount = ACCOUNT_DAO.get(ACC_EMAIL);

      Assertions.assertEquals(updatedPassword, updatedAccount.password());
    });
  }

  @Test
  void testDeleteAccount() {
    assertDoesNotThrow(() -> {
      ACCOUNT_DAO.delete(ACC_EMAIL);

      AccountDTO deletedAccount = ACCOUNT_DAO.get(ACC_EMAIL);

      Assertions.assertNull(deletedAccount);
    });
  }
}
