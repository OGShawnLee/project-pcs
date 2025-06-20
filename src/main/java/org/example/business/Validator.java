package org.example.business;

public class Validator {
  private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
  private static final String ENROLLMENT_REGEX = "^[0-9]{8}$";
  private static final String NAME_REGEX_SPANISH = "^[A-Za-zÑñÁáÉéÍíÓóÚúÜü\\s]+$";
  private static final String WORKER_ID_REGEX = "^(?!0+$)[0-9]{1,5}$";
  private static final String FLEXIBLE_NAME_REGEX = "^[A-Za-zÑñÁáÉéÍíÓóÚúÜü0-9\\s\\-_/.:]+$";
  private static final String GRADE_REGEX = "^(10|[0-9])$";
  private static final String PHONE_NUMBER_REGEX = "^(\\+?\\d{1,3})?[-.\\s]?\\(?\\d{1,4}\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$";
  private static final String NRC_REGEX = "^(?!0+$)[0-9]{5}$";

  private static boolean isValidEmail(String email) {
    return isValidString(email) && email.trim().matches(EMAIL_REGEX);
  }

  private static boolean isValidName(String name, int minLength, int maxLength) {
    return isValidString(name, minLength, maxLength) && name.matches(NAME_REGEX_SPANISH);
  }

  public static boolean isValidString(String string) {
    return string != null && string.trim().length() > 0;
  }

  private static boolean isValidString(String value, int minLength, int maxLength) {
    if (value == null || value.trim().isEmpty()) {
      return false;
    }

    String trimmedString = value.trim();
    return trimmedString.length() >= minLength && trimmedString.length() <= maxLength;
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

  public static int getValidGrade(String value) throws IllegalArgumentException {
    if (isValidString(value) && value.trim().matches(GRADE_REGEX)) {
      return Integer.parseInt(value.trim());
    }

      System.out.println("Validando calificación: " + value);
    throw new IllegalArgumentException("La calificación debe ser un número entero entre 0 y 10.");
  }

  public static int getValidGrade(int value) throws IllegalArgumentException {
    return getValidGrade(String.valueOf(value));
  }

  public static int getValidInteger(String value, String name, int minValue) throws IllegalArgumentException {
    if (isValidString(value) && value.trim().matches("\\d+")) {
      int integer = Integer.parseInt(value.trim());

      if (integer >= minValue) {
        return integer;
      }

      throw new IllegalArgumentException(name + " debe ser un número entero mayor o igual a " + minValue + ".");
    }

    throw new IllegalArgumentException(name + " debe ser un número entero válido.");
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

  public static String getValidNRC(String value) {
    if (isValidString(value) && value.trim().matches(NRC_REGEX)) {
      return value.trim();
    }

    throw new IllegalArgumentException("NRC debe ser una cadena de texto de 1 a 5 dígitos.");
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

  public static String getValidPhoneNumber(String value) throws IllegalArgumentException {
    if (isValidString(value, 10, 16) && value.trim().matches(PHONE_NUMBER_REGEX)) {
      // Note: Normalize phone number by removing spaces and dashes.
      return value.trim().replaceAll("[\\s\\-]", "");
    }

    throw new IllegalArgumentException("Número de Teléfono debe ser una cadena de texto con el formato correcto.");
  }

  public static String getValidPassword(String password) throws IllegalArgumentException {
    if (isValidString(password, 8, 64)) {
      return password.trim();
    }

    throw new IllegalArgumentException("Contraseña debe ser una cadena de texto entre 8 y 64 carácteres.");

  }

  public static String getValidProjectRequestState(String value) throws IllegalArgumentException {
    String finalValue = getValidName(value, "Estado de Solicitud");

    if (finalValue.equals("PENDING") || finalValue.equals("ACCEPTED") || finalValue.equals("REJECTED")) {
      return finalValue;
    }

    if (finalValue.equals("Pendiente")) {
      return "PENDING";
    }

    if (finalValue.equals("Aceptada")) {
      return "ACCEPTED";
    }

    if (finalValue.equals("Rechazada")) {
      return "REJECTED";
    }

    throw new IllegalArgumentException("Estado de Solicitud debe ser uno de los siguientes: Pendiente, Aceptada, Rechazada.");
  }

  public static String getValidString(String value, String name, int minLength, int maxLength) throws IllegalArgumentException {
    if (isValidString(value, minLength, maxLength)) {
      return value.trim();
    }

    throw new IllegalArgumentException(name + " no puede ser nulo o vacío.");
  }

  public static String getValidText(String value, String name) throws IllegalArgumentException {
    if (isValidString(value, 3, 512)) {
      return value.trim();
    }

    throw new IllegalArgumentException(name + " no puede ser nulo o vacío.");
  }

  public static String getValidWorkerID(String value) throws IllegalArgumentException {
    if (isValidString(value) && value.trim().matches(WORKER_ID_REGEX)) {
      return value.replaceFirst("^0+(?!$)", "").trim();
    }

    throw new IllegalArgumentException("ID de Trabajador debe ser una cadena de texto de 5 carácteres de letras o digitos.");
  }
}
