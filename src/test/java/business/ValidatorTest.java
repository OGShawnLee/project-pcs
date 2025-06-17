package business;

import org.example.business.Validator;
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
  public void testGetValidFlexibleName() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "John Doe",
          Validator.getValidFlexibleName("John Doe", "Name", 3, 64),
          "Valid flexible name should be returned"
        );
      }
    );
  }

  @Test
  public void testGetValidFlexibleNameWithSpaces() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "John Doe",
          Validator.getValidFlexibleName("   John Doe   ", "Name", 3, 64),
          "Valid flexible name should be returned with spaces trimmed"
        );
      }
    );
  }

  @Test
  public void testGetValidFlexibleNameWithSpanishCharacters() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "José María Nuñez Dominguéz",
          Validator.getValidFlexibleName("José María Nuñez Dominguéz", "Name", 3, 64),
          "Valid flexible name should allow Spanish characters"
        );
      }
    );
  }

  @Test
  public void testGetValidFlexibleNameWithNull() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidFlexibleName(null, "Name", 6, 64),
      "Flexible name cannot be null"
    );
  }

  @Test
  public void testGetValidFlexibleNameWithEmpty() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidFlexibleName("", "Name", 6, 64),
      "Flexible name cannot be empty"
    );
  }

  @Test
  public void testGetValidFlexibleNameWithShortInvalidLength() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidFlexibleName("A", "Name", 6, 64),
      "Flexible name must be between 6 and 64 characters"
    );
  }

  @Test
  public void testGetValidFlexibleNameWithLongInvalidLength() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidFlexibleName("A very long flexible name that exceeds the maximum length", "Name", 6, 16),
      "Flexible name must be between 6 and 16 characters"
    );
  }

  @Test
  public void testGetValidFlexibleNameWithValidCharacters() {
    String[] validCharacters = {"-", "_", "/", ".", ":", "1234567890"};
    for (String validCharacter : validCharacters) {
      assertDoesNotThrow(
        () -> {
          Assertions.assertEquals(
            "John Doe" + validCharacter,
            Validator.getValidFlexibleName("John Doe" + validCharacter, "Name", 3, 64),
            "Flexible name should allow special characters"
          );
        }
      );
    }
  }

  @Test
  public void testGetValidFlexibleNameWithInvalidCharacters() {
    String[] invalidCharacters = {"!", "#", "$", "%", "^", "&", "*", "(", ")", "=", "+", "{", "}", "[", "]", "|", "\\", ";", "\"", "'", "<", ">", ",", "?", "~"};
    for (String invalidCharacter : invalidCharacters) {
      Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> Validator.getValidFlexibleName("John Doe" + invalidCharacter, "Name", 3, 64),
        "Flexible name cannot contain special characters"
      );
    }
  }

  @Test
  public void testGetValidProjectRequestState() {
    assertDoesNotThrow(
      () -> {
        String[] states = {"PENDING", "ACCEPTED", "REJECTED"};
        for (String state : states) {
          Assertions.assertEquals(
            state,
            Validator.getValidProjectRequestState(state),
            "Valid project request state should be returned"
          );
        }
      }
    );
  }

  @Test
  public void testGetValidProjectRequestStateWithSpaces() {
    assertDoesNotThrow(
      () -> {
        String[] states = {"PENDING", "ACCEPTED", "REJECTED"};
        for (String state : states) {
          Assertions.assertEquals(
            state,
            Validator.getValidProjectRequestState("   " + state + "   "),
            "Valid project request state should be returned with spaces trimmed"
          );
        }
      }
    );
  }

  @Test
  public void testGetValidProjectRequestStateWithSpanishLabel() {
    assertDoesNotThrow(
      () -> {
        String[] states = {"PENDING", "ACCEPTED", "REJECTED"};
        String[] spanishLabels = {"Pendiente", "Aceptada", "Rechazada"};
        for (int i = 0; i < states.length; i++) {
          Assertions.assertEquals(
            states[i],
            Validator.getValidProjectRequestState(spanishLabels[i]),
            "Valid project request state should be returned with Spanish label"
          );
        }
      }
    );
  }

  @Test
  public void testGetValidProjectRequestStateWithSpanishLabelAndSpaces() {
    assertDoesNotThrow(
      () -> {
        String[] states = {"PENDING", "ACCEPTED", "REJECTED"};
        String[] spanishLabels = {"   Pendiente   ", "   Aceptada   ", "   Rechazada   "};
        for (int i = 0; i < states.length; i++) {
          Assertions.assertEquals(
            states[i],
            Validator.getValidProjectRequestState(spanishLabels[i]),
            "Valid project request state should be returned with Spanish label and spaces trimmed"
          );
        }
      }
    );
  }

  @Test
  public void testGetValidProjectRequestStateWithNull() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidProjectRequestState(null),
      "Project request state cannot be null"
    );
  }

  @Test
  public void testGetValidProjectRequestStateWithEmpty() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidProjectRequestState(""),
      "Project request state cannot be empty"
    );
  }

  @Test
  public void testGetValidProjectRequestStateWithInvalidValue() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidProjectRequestState("InvalidState"),
      "Project request state must be one of the following: Pendiente, Aceptada, Rechazada"
    );
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
  public void testGetValidText() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "Valid text",
          Validator.getValidText("Valid text", "Text"),
          "Valid text should be returned"
        );
      }
    );
  }

  @Test
  public void testGetValidTextWithSpaces() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          "Valid text",
          Validator.getValidText("   Valid text   ", "Text"),
          "Valid text should be returned with spaces trimmed"
        );
      }
    );
  }

  @Test
  public void testGetValidTextWithNull() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidText(null, "Text"),
      "Text cannot be null"
    );
  }

  @Test
  public void testGetValidTextWithEmpty() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidText("", "Text"),
      "Text cannot be empty"
    );
  }

  @Test
  public void testGetValidTextWithInvalidShortLength() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidText("A", "Text"),
      "Text must be between 3 and 512 characters"
    );
  }

  @Test
  public void testGetValidTextWithInvalidLongLength() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 514; i++) {
          builder.append("A");
        }

        Validator.getValidText(builder.toString(), "Text");
      },
      "Text must be between 3 and 512 characters"
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

  @Test
  public void testGetValidGrade() {
    assertDoesNotThrow(
      () -> {
        for (int i = 0; i <= 10; i++) {
          Assertions.assertEquals(
            i,
            Validator.getValidGrade(String.valueOf(i)),
            "Valid grade should be returned"
          );
        }
      }
    );
  }

  @Test
  public void testGetValidGradeWithSpaces() {
    assertDoesNotThrow(
      () -> {
        Assertions.assertEquals(
          3,
          Validator.getValidGrade("   3   "),
          "Valid grade should be returned with spaces trimmed"
        );
      }
    );
  }

  @Test
  public void testGetValidGradeWithNull() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidGrade(null),
      "Grade cannot be null"
    );
  }

  @Test
  public void testGetValidGradeWithEmpty() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidGrade(""),
      "Grade cannot be empty"
    );
  }

  @Test
  public void testGetValidGradeWithInvalidFormat() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidGrade("invalid-grade"),
      "Grade must be a string of digits"
    );
  }

  @Test
  public void testGetValidGradeWithInvalidLength() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidGrade("1234567890"),
      "Grade must be a string of 1 or 2 digits"
    );
  }

  @Test
  public void testGetValidGradeWithInvalidCharacters() {
    String[] invalidCharacters = {"!", "#", "$", "%", "^", "&", "*", "(", ")", "=", "+", "{", "}", "[", "]", "|", "\\", ":", ";", "\"", "'", "<", ">", ",", "?", "/", "~"};
    for (String invalidCharacter : invalidCharacters) {
      Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> Validator.getValidGrade("3" + invalidCharacter),
        "Grade cannot contain special characters"
      );
    }
  }

  @Test
  public void testGetValidGradeWithInvalidValue() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidGrade("11"),
      "Grade must be between 0 and 10"
    );
  }

  @Test
  public void testGetValidGradeWithNegativeValue() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidGrade("-1"),
      "Grade must be between 0 and 10"
    );
  }

  @Test
  public void testGetValidGradeWithNonIntegerValue() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidGrade("3.0"),
      "Grade must be an integer between 0 and 10"
    );
  }

  @Test
  public void testGetValidGradeWithInteger() {
    assertDoesNotThrow(
      () -> {
        for (int i = 0; i <= 10; i++) {
          Assertions.assertEquals(
            i,
            Validator.getValidGrade(i),
            "Valid grade should be returned as an integer"
          );
        }
      }
    );
  }

  @Test
  public void testGetValidGradeWithNegativeInteger() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidGrade(-1),
      "Grade must be between 0 and 10"
    );
  }

  @Test
  public void testGetValidGradeWithOutOfRangeInteger() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> Validator.getValidGrade(11),
      "Grade must be between 0 and 10"
    );
  }
}