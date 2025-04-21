package org.example.business;

public class Result<T> {
  private T data;
  private String error;

  private Result(T data, String error) {
    this.data = data;
    this.error = error;
  }

  public static <T> Result<T> createSuccessResult(T data) {
    return new Result<>(data, null);
  }

  public static <T> Result<T> createFailureResult(String error) {
    return new Result<>(null, error);
  }

  public T getData() {
    return data;
  }

  public String getError() {
    return error;
  }

  public boolean isSuccess() {
    return error == null;
  }

  public boolean isFailure() {
    return error != null;
  }

}