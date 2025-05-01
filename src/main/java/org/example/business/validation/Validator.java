package org.example.business.validation;

import org.example.business.Result;

public class Validator {
  private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
  private static final String ENROLLMENT_REGEX = "^[0-9]{8}$";
  private static final String NAME_REGEX_SPANISH = "^[A-Za-zÑñÁáÉéÍíÓóÚúÜü\\s]+$";
  private static final String WORKER_ID_REGEX = "^[A-Z0-9]{5}$";
  private static final String FLEXIBLE_NAME_REGEX = "^[A-Za-zÑñÁáÉéÍíÓóÚúÜü0-9\\s\\-_/.:]+$";

  private static boolean isValidEmail(String email) {
    return isValidString(email) && email.trim().matches(EMAIL_REGEX);
  }

  private static boolean isValidName(String name, int minLength, int maxLength) {
    return isValidString(name, minLength, maxLength) && name.matches(NAME_REGEX_SPANISH);
  }

  private static boolean isValidString(String string) {
    return string != null && string.trim().length() > 0;
  }

  private static boolean isValidString(String value, int minLength, int maxLength) {
    if (value == null || value.trim().isEmpty()) {
      return false;
    }

    String trimmedString = value.trim();
    return trimmedString.length() >= minLength && trimmedString.length() <= maxLength;
  }

  public static String getValidAcademicRole(String value) throws IllegalArgumentException   {
    String finalValue = getValidString(value, "Rol de Académico");

    if (
      finalValue.equals("EVALUATOR") ||
      finalValue.equals("EVALUATOR-PROFESSOR") ||
      finalValue.equals("PROFESSOR")
    ) {
      return finalValue;
    }

    if (finalValue.equals("Evaluador")) {
      return "EVALUATOR";
    }

    if (finalValue.equals("Evaluador y Profesor")) {
      return "EVALUATOR-PROFESSOR";
    }

    if (finalValue.equals("Profesor")) {
      return "PROFESSOR";
    }

    throw new IllegalArgumentException("Rol académico debe ser uno de los siguientes: Evaluador, Evaluador-Profesor, Profesor.");
  }

  public static String getValidEmail(String value) throws IllegalArgumentException {
    if (isValidEmail(value)) {
      return value.trim();
    }

    throw new IllegalArgumentException("Correo Electrónico debe ser una cadena de texto con el formato correcto.");
  }


  public static String getValidEnrollment(String value) throws IllegalArgumentException {
    if (isValidString(value) && value.trim().matches(ENROLLMENT_REGEX)) {
      return value.trim();
    }

    throw new IllegalArgumentException("Matrícula debe ser una cadena de texto de 8 dígitos.");
  }

  public static String getValidFlexibleName(String value, String name, int minLength, int maxLength) throws IllegalArgumentException {
    if (isValidString(value, minLength, maxLength) && value.trim().matches(FLEXIBLE_NAME_REGEX)) {
      return value.trim();
    }

    throw new IllegalArgumentException(name + " no puede ser nulo o vacío.");
  }

  private static String getValidName(String value, String name) throws IllegalArgumentException {
    if (isValidName(value, 3, 128)) {
      return value.trim();
    }

    throw new IllegalArgumentException(name + " no puede ser nulo o vacío.");
  }

  public static String getValidName(String value, String name, int minLength, int maxLength) throws IllegalArgumentException {
    if (isValidName(value, minLength, maxLength)) {
      return value.trim();
    }

    throw new IllegalArgumentException(
      name + " debe ser una cadena de texto entre " + minLength + " y " + maxLength + " carácteres."
    );
  }

  public static String getValidProjectSector(String value) throws IllegalArgumentException {
    String finalValue = getValidName(value, "Sector");

    if (
      finalValue.equals("PUBLIC") ||
      finalValue.equals("PRIVATE") ||
      finalValue.equals("SOCIAL")
    ) {
      return finalValue;
    }

    if (finalValue.equals("Público")) {
      return "PUBLIC";
    }

    if (finalValue.equals("Privado")) {
      return "PRIVATE";
    }

    if (finalValue.equals("Social")) {
      return "SOCIAL";
    }

    throw new IllegalArgumentException("Sector debe ser uno de los siguientes: Público, Privado, Social.");
  }

  public static String getValidState(String value) throws IllegalArgumentException {
    String finalValue = getValidName(value, "Estado");

    if (finalValue.equals("ACTIVE") || finalValue.equals("RETIRED")) {
      return finalValue;
    }

    if (finalValue.equals("Activo")) {
      return "ACTIVE";
    }

    if (finalValue.equals("Inactivo")) {
      return "RETIRED";
    }

    throw new IllegalArgumentException("Estado debe ser uno de los siguientes: Activo, Inactivo.");
  }

  private static String getValidString(String value, String name) throws IllegalArgumentException {
    if (isValidString(value, 3, 128)) {
      return value.trim();
    }

    throw new IllegalArgumentException(name + " no puede ser nulo o vacío.");
  }

  public static String getValidWorkerID(String value) throws IllegalArgumentException {
    if (isValidString(value) && value.trim().matches(WORKER_ID_REGEX)) {
      return value.trim();
    }

    throw new IllegalArgumentException("ID de Trabajador debe ser una cadena de texto de 5 carácteres de letras o digitos.");
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
