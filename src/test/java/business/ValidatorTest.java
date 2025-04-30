package business;

import org.example.business.dto.AcademicDTO;
import org.example.business.validation.Validator;
import org.example.db.dao.AcademicDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ValidatorTest {
  @Test
  public void testGetValidName() {
    assertDoesNotThrow(() -> {
      Validator.getValidName("Manuel", "Nombre", 3, 64);
    });
  }

  @Test
  public void testGetValidNameWithSpanish() {
    assertDoesNotThrow(() -> {
      Validator.getValidName("José María", "Nombre", 3, 64);
    });
  }

  @Test void testGetValidNameThrowsException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      Validator.getValidName("A", "Nombre", 3, 64);
    });
  }

  @Test
  public void testGetValidNameThrowsExceptionWithNumbers() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      Validator.getValidName("José María 123", "Nombre", 3, 64);
    });
  }

  @Test
  public void testGetValidNameThrowsExceptionWithSpecialCharacters() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      String[] specialChars = {"!", "#", "$", "%", "&", "/", "(", ")", "=", "?", "¿", "¡", "@", "+", "*", "-", "_", ".", ",", ";", ":"};

      for (String specialChar : specialChars) {
        Validator.getValidName("José María " + specialChar, "Nombre", 3, 64);
      }
    });
  }

  @Test
  public void testGetValidNameThrowsExceptionWithEmptyString() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      Validator.getValidName("", "Nombre", 3, 64);
    });
  }

  @Test
  public void testGetValidNameThrowsExceptionWithNull() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      Validator.getValidName(null, "Nombre", 3, 64);
    });
  }

  @Test
  public void testGetValidNameThrowsExceptionWithSpaces() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      Validator.getValidName("   ", "Nombre", 3, 64);
    });
  }

  @Test
  public void testGetValidNameThrowsExceptionWithTooLong() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      String longName = "José María José María José María José María José María José María José María José María José María José María";
      Validator.getValidName(longName, "Nombre", 3, 64);
    });
  }

  @Test
  public void testGetValidNameThrowsExceptionWithTooShort() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      Validator.getValidName("A", "Nombre", 3, 64);
    });
  }
}
