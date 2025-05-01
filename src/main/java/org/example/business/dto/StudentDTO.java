package org.example.business.dto;

import org.example.business.validation.Validator;

public class StudentDTO extends Person {
  private final String id;
  private final String finalGrade;

  public StudentDTO(StudentBuilder builder) {
    super(builder);
    this.id = builder.id;
    this.finalGrade = builder.finalGrade;
  }

  public String getID() {
    return id;
  }

  public String getFinalGrade() { return finalGrade;}

  public static class StudentBuilder extends PersonBuilder<StudentBuilder> {
    private String id;
    private String finalGrade;

    public StudentBuilder setID(String id) throws IllegalArgumentException {
      this.id = Validator.getValidEnrollment(id);
      return this;
    }

    public StudentBuilder setFinalGrade(String finalGrade) {
      this.finalGrade = finalGrade;
      return this;
    }

    @Override
    public StudentDTO build() {
      return new StudentDTO(this);
    }
  }
}