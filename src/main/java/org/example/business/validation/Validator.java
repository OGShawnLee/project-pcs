package org.example.business.validation;

import org.example.business.Result;
import org.example.business.dto.AcademicRole;

public class Validator {
  private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
  private static final String NAME_REGEX_SPANISH = "^[A-Za-zÑñÁáÉéÍíÓóÚúÜü\\s]+$";

  private static boolean isInvalidAcademicRole(String role) {
    return AcademicRole.getAcademicRoleFromSpanishLabel(role) == null;
  }

  private static boolean isInvalidEmail(String email) {
    return isInvalidString(email) || !email.matches(EMAIL_REGEX);
  }

  private static boolean isInvalidName(String name, int minLength, int maxLength) {
    return isInvalidString(name, minLength, maxLength) || !name.matches(NAME_REGEX_SPANISH);
  }

  private static boolean isInvalidString(String string) {
    return string == null || string.trim().isEmpty();
  }

  private static boolean isInvalidString(String string, int minLength, int maxLength) {
    string = string == null ? null : string.trim();
    return string == null || string.isEmpty() || string.length() < minLength || string.length() > maxLength;
  }

  public static AcademicRole getValidAcademicRoleFromSpanishLabel(String role) throws IllegalArgumentException {
    if (isInvalidAcademicRole(role)) {
      throw new IllegalArgumentException(
        "Rol de Académico debe ser uno de los siguientes valores: Evaluador, Profesor, Profesor-Evaluador."
      );
    }

    return AcademicRole.getAcademicRoleFromSpanishLabel(role);
  }

  public static String getValidName(String value, String name, int minLength, int maxLength) throws IllegalArgumentException {
    if (isInvalidName(value, minLength, maxLength)) {
      throw new IllegalArgumentException(
        name + "debe ser una cadena de texto entre " + minLength + " y " + maxLength + " carácteres."
      );
    }

    return value.trim();
  }

  public static String getValidString(String value, String message) throws IllegalArgumentException {
    if (isInvalidString(value)) {
      throw new IllegalArgumentException(message);
    }

    return value.trim();
  }

  public static String getValidStringWithLength(String value, String message, int minLength, int maxLength) {
    if (isInvalidString(value, minLength, maxLength)) {
      throw new IllegalArgumentException(message);
    }

    return value.trim();
  }

  public static String getValidEmail(String value) {
    if (isInvalidEmail(value)) {
      throw new IllegalArgumentException("Correo electrónico debe ser una cadena de texto con el formato correcto.");
    }

    return value.trim();
  }

  public static Result<String> getFilledString(String value, String message) {
    if (value == null ||value.trim().isEmpty()) {
      return Result.createFailureResult(message);
    }
    return Result.createSuccessResult(value);
  }

  public static Result<String> getMaxLenght(String value, String message, int maxLength) {
    if (value.trim().length() > maxLength) {
      return Result.createFailureResult(message);
    }
    return Result.createSuccessResult(value);
  }

  public static Result<String> getWords(String value, String message) {
    if (!value.matches(".*[^a-zA-ZáéíóúÁÉÍÓÚñÑ]*.")) {
      return Result.createFailureResult(message);
    }
    return Result.createSuccessResult(value);
  }

  public static Result<String> getIDStudent(String value, String message, int maxLength) {
    if (!value.matches("[0-9]+") || value.trim().length() != maxLength) {
      return Result.createFailureResult(message);
    }
    return Result.createSuccessResult(value);
  }


  public static Result<String> getEmail(String value, String message) {
    Result<String> emailResult = getFilledString(value, message);

    if (emailResult.isFailure()) {
      return emailResult;
    }

    if (value.matches(EMAIL_REGEX)) {
      return Result.createSuccessResult(value);
    }

    return Result.createFailureResult(message);
  }
}
