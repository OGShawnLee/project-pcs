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
  void testCreateOneAccount() {
    assertDoesNotThrow(() -> {
      AccountDTO account = new AccountDTO(ACC_EMAIL, ACC_PASSWORD);

      ACCOUNT_DAO.createOne(account);

      AccountDTO createdAccount = ACCOUNT_DAO.getOne(ACC_EMAIL);

      Assertions.assertNotNull(createdAccount);
      Assertions.assertEquals(ACC_EMAIL, createdAccount.email());
      Assertions.assertEquals(ACC_PASSWORD, createdAccount.password());
    });
  }

  @Test
  void testGetOneAllAccounts() {
    assertDoesNotThrow(() -> {
      List<AccountDTO> accountList = ACCOUNT_DAO.getAll();

      Assertions.assertNotNull(accountList);
      Assertions.assertFalse(accountList.isEmpty());
    });
  }

  @Test
  void testGetOneAccount() {
    assertDoesNotThrow(() -> {
      AccountDTO account = ACCOUNT_DAO.getOne(ACC_EMAIL);

      Assertions.assertNotNull(account);
    });
  }

  @Test
  void testUpdateOneAccount() {
    assertDoesNotThrow(() -> {
      String updatedPassword = "UpdatedPassword123";
      AccountDTO account = new AccountDTO(ACC_EMAIL, updatedPassword);

      ACCOUNT_DAO.updateOne(account);

      AccountDTO updatedAccount = ACCOUNT_DAO.getOne(ACC_EMAIL);

      Assertions.assertEquals(updatedPassword, updatedAccount.password());
    });
  }

  @Test
  void testDeleteOneAccount() {
    assertDoesNotThrow(() -> {
      ACCOUNT_DAO.deleteOne(ACC_EMAIL);

      AccountDTO deletedAccount = ACCOUNT_DAO.getOne(ACC_EMAIL);

      Assertions.assertNull(deletedAccount);
    });
  }
}
