package business.dto;

import org.example.business.dto.AccountDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountDTOTest {
  @Test
  public void testCreate() {
    Assertions.assertDoesNotThrow(() -> {
      new AccountDTO("Valid@gmail.com", "Password1234", AccountDTO.Role.STUDENT, true);
    });
  }

  @Test
  public void testCreateWithInvalidEmail() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new AccountDTO("invalid-email", "Password1234", AccountDTO.Role.STUDENT, true);
    });
  }

  @Test
  public void testCreateWithInvalidPassword() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      new AccountDTO("Valid@gmail.com", "1234", AccountDTO.Role.STUDENT, true);
    });
  }
}
