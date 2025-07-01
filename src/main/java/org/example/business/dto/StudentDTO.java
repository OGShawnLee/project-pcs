package org.example.business.dto;

import org.example.business.Validator;

public class StudentDTO extends Person {
  private final String id;
  private final int finalGrade;

  public StudentDTO(StudentBuilder builder) {
    super(builder);
    this.id = builder.id;
    this.finalGrade = builder.finalGrade;
  }

  public String getID() {
    return id;
  }

  public int getFinalGrade() {
    return finalGrade;
  }

  @Override
  public String toString() {
    return getFullName() + " (S" + id + ")";
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    StudentDTO that = (StudentDTO) instance;

    return id.equals(that.id) && finalGrade == that.finalGrade && super.equals(that);
  }

  public static class StudentBuilder extends PersonBuilder<StudentBuilder> {
    private String id;
    private int finalGrade;

    public StudentBuilder setID(String id) throws IllegalArgumentException {
      this.id = Validator.getValidEnrollment(id);
      return this;
    }

    public StudentBuilder setFinalGrade(int finalGrade) {
      this.finalGrade = Validator.getValidGrade(finalGrade);
      return this;
    }

    @Override
    public StudentDTO build() {
      return new StudentDTO(this);
    }
  }
}