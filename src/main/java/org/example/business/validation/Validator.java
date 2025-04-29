package org.example.business.validation;

import org.example.business.Result;

public class Validator {
  private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

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

  public static Result<String> getWords(String value, String message, int maxLength) {
    if (value == null || !value.matches(".*[^a-zA-Z].*")) {
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
