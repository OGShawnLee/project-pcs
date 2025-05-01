package business.validation;

import org.example.business.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ValidatorTest {
  @Test
  public void testGetAcademicRole() {
    assertDoesNotThrow(
      () -> {
        String[] roles = {"EVALUATOR", "EVALUATOR-PROFESSOR", "PROFESSOR"};
        for (String role : roles) {
          Assertions.assertEquals(
            role,
            Validator.getValidAcademicRole(role),
            "Valid academic role should be returned"
          );
        }
      }
    );
  }

  @Test
  public void testGetAcademicRoleWithSpaces() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "EVALUATOR",
          Validator.getValidAcademicRole("   Evaluador   "),
          "Valid academic role should be returned with spaces trimmed"
        );
      }
    );
  }

  @Test
  public void testGetAcademicRoleWithNull() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidAcademicRole(null),
      "Academic role cannot be null"
    );
  }

  @Test
  public void testGetAcademicRoleWithEmpty() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidAcademicRole(""),
      "Academic role cannot be empty"
    );
  }

  @Test
  public void testGetAcademicRoleWithInvalidValue() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidAcademicRole("InvalidRole"),
      "Academic role must be one of the following: Evaluador, Evaluador-Profesor, Profesor"
    );
  }

  @Test
  public void testGetAcademicRoleWithSpanishLabel() {
    assertDoesNotThrow(
      () -> {
        String[] roles = {"EVALUATOR", "EVALUATOR-PROFESSOR", "PROFESSOR"};
        String[] spanishLabels = {"Evaluador", "Evaluador y Profesor", "Profesor"};
        for (int i = 0; i < roles.length; i++) {
          Assertions.assertEquals(
            roles[i],
            Validator.getValidAcademicRole(spanishLabels[i]),
            "Valid academic role should be returned with Spanish label"
          );
        }
      }
    );
  }

  @Test
  public void testGetEmail() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "OGJohnDoe@gmail.com",
          Validator.getValidEmail("OGJohnDoe@gmail.com"),
          "Valid email should be returned"
        );
      }
    );
  }

  @Test
  public void testGetValidEmail() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "OGJohnDoe@gmail.com",
          Validator.getValidEmail("   OGJohnDoe@gmail.com   "),
          "Valid email should be returned with spaces trimmed"
        );
      }
    );
  }

  @Test
  public void testGetValidEmailWithNull() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidEmail(null),
      "Email cannot be null"
    );
  }

  @Test
  public void testGetValidEmailWithEmpty() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidEmail(""),
      "Email cannot be empty"
    );
  }

  @Test
  public void testGetValidEmailWithInvalidFormat() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidEmail("invalid-email"),
      "Email must be in a valid format"
    );
  }

  @Test
  public void testGetValidEmailWithInvalidDomain() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidEmail("user@invalid-domain"),
      "Email must be in a valid format"
    );
  }

  @Test
  public void testGetValidEmailWithInvalidLength() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidEmail("a@b.c"),
      "Email must be between 6 and 64 characters"
    );
  }

  @Test
  public void testGetValidEmailWithInvalidCharacters() {
    String[] invalidCharacters = {"!", "#", "$", "%", "^", "&", "*", "(", ")", "=", "+", "{", "}", "[", "]", "|", "\\", ":", ";", "\"", "'", "<", ">", ",", "?", "/", "~"};
    for (String invalidCharacter : invalidCharacters) {
      Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> Validator.getValidEmail("JohnDoe" + invalidCharacter + "@gmail.com"),
        "Email cannot contain special characters"
      );
    }
  }

  @Test
  public void testGetValidEnrollment() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "12345678",
          Validator.getValidEnrollment("12345678"),
          "Valid enrollment should be returned"
        );
      }
    );
  }

  @Test
  public void testGetValidEnrollmentWithSpaces() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "12345678",
          Validator.getValidEnrollment("   12345678   "),
          "Valid enrollment should be returned with spaces trimmed"
        );
      }
    );
  }

  @Test
  public void testGetValidEnrollmentWithNull() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidEnrollment(null),
      "Enrollment cannot be null"
    );
  }

  @Test
  public void testGetValidEnrollmentWithEmpty() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidEnrollment(""),
      "Enrollment cannot be empty"
    );
  }

  @Test
  public void testGetValidEnrollmentWithInvalidFormat() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidEnrollment("invalid-enrollment"),
      "Enrollment must be a string of 8 digits"
    );
  }

  @Test
  public void testGetValidEnrollmentWithInvalidLength() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidEnrollment("1234567"),
      "Enrollment must be a string of 8 digits"
    );
  }

  @Test
  public void testGetValidEnrollmentWithInvalidCharacters() {
    String[] invalidCharacters = {"!", "#", "$", "%", "^", "&", "*", "(", ")", "=", "+", "{", "}", "[", "]", "|", "\\", ":", ";", "\"", "'", "<", ">", ",", "?", "/", "~"};
    for (String invalidCharacter : invalidCharacters) {
      Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> Validator.getValidEnrollment("12345678" + invalidCharacter),
        "Enrollment cannot contain special characters"
      );
    }
  }

  @Test
  public void testGetValidName() {
    assertDoesNotThrow(() -> {
      Assertions.assertEquals(
        "John Doe",
        Validator.getValidName("John Doe", "Name", 3, 64),
        "Valid name should be returned"
      );
    });
  }

  @Test
  public void testGetValidNameWithSpaces() {
    assertDoesNotThrow(() -> {
      Assertions.assertEquals(
        "John Doe",
        Validator.getValidName("   John Doe   ", "Name", 3, 64),
        "Valid name should be returned with spaces trimmed"
      );
    });
  }

  @Test
  public void testGetValidNameWithSpanishCharacters() {
    Assertions.assertDoesNotThrow(
      () -> Validator.getValidName("José María Nuñez Dominguéz", "Name", 3, 64),
      "Name should allow Spanish characters"
    );
  }

  @Test
  public void testGetValidNameWithNull() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidName(null, "Name", 6, 64),
      "Name cannot be null"
    );
  }

  @Test
  public void testGetValidNameWithEmpty() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidName("", "Name", 6, 64),
      "Name cannot be empty"
    );
  }

  @Test
  public void testGetValidNameWithShortInvalidLength() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidName("A", "Name", 6, 64),
      "Name must be between 6 and 64 characters"
    );
  }

  @Test
  public void testGetValidNameWithLongInvalidLength() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidName("A very long name that exceeds the maximum length", "Name", 6, 16),
      "Name must be between 6 and 16 characters"
    );
  }

  @Test
  public void testGetValidNameWithInvalidCharacters() {
    String[] invalidCharacters = {"123", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "+", "{", "}", "[", "]", "|", "\\", ":", ";", "\"", "'", "<", ">", ",", ".", "?", "/", "~"};
    for (String invalidCharacter : invalidCharacters) {
      Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> Validator.getValidName("John Doe" + invalidCharacter, "Name", 3, 64),
        "Name cannot contain special characters"
      );
    }
  }

  @Test
  public void testGetValidState() {
    assertDoesNotThrow(
      () -> {
        for (String state : new String[]{"ACTIVE", "RETIRED"}) {
          Assertions.assertEquals(
            state,
            Validator.getValidState(state),
            "Valid state should be returned"
          );
        }
      }
    );
  }

  @Test
  public void testGetValidStateWithSpaces() {
    assertDoesNotThrow(
      () -> {
        for (String state : new String[]{"ACTIVE", "RETIRED"}) {
          Assertions.assertEquals(
            state,
            Validator.getValidState("   " + state + "   "),
            "Valid state should be returned with spaces trimmed"
          );
        }
      }
    );
  }

  @Test
  public void testGetValidStateWithSpanishLabel() {
    assertDoesNotThrow(
      () -> {
        for (String state : new String[]{"ACTIVE", "RETIRED"}) {
          Assertions.assertEquals(
            state,
            Validator.getValidState(state.equals("ACTIVE") ? "Activo" : "Inactivo"),
            "Valid state should be returned with Spanish label"
          );
        }
      }
    );
  }

  @Test
  public void testGetValidStateWithNull() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidState(null),
      "State cannot be null"
    );
  }

  @Test
  public void testGetValidStateWithEmpty() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidState(""),
      "State cannot be empty"
    );
  }

  @Test
  public void testGetValidWorkerID() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "12345",
          Validator.getValidWorkerID("12345"),
          "Valid worker ID should be returned"
        );
      }
    );
  }

  @Test
  public void testGetValidWorkerIDWithSpaces() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "12345",
          Validator.getValidWorkerID("   12345   "),
          "Valid worker ID should be returned with spaces trimmed"
        );
      }
    );
  }

  @Test
  public void testGetValidWorkerIDWithNull() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidWorkerID(null),
      "Worker ID cannot be null"
    );
  }

  @Test
  public void testGetValidWorkerIDWithEmpty() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidWorkerID(""),
      "Worker ID cannot be empty"
    );
  }

  @Test
  public void testGetValidWorkerIDWithLetters() {
    Assertions.assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "ABCDE",
          Validator.getValidWorkerID("ABCDE"),
          "Valid worker ID should be returned"
        );
      }
    );
  }

  @Test
  public void testGetValidWorkerIDWithInvalidLength() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidWorkerID("1234"),
      "Worker ID must be a string of 5 digits"
    );
  }

  @Test
  public void testGetValidWorkerIDWithInvalidCharacters() {
    String[] invalidCharacters = {"!", "#", "$", "%", "^", "&", "*", "(", ")", "=", "+", "{", "}", "[", "]", "|", "\\", ":", ";", "\"", "'", "<", ">", ",", "?", "/", "~"};
    for (String invalidCharacter : invalidCharacters) {
      Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> Validator.getValidWorkerID("12345" + invalidCharacter),
        "Worker ID cannot contain special characters"
      );
    }
  }
}