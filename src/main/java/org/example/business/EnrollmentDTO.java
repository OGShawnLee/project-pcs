package org.example.business;

public class EnrollmentDTO {
  private final String idCourse;
  private final String idStudent;
  private final String createdAt;

  public EnrollmentDTO(EnrollmentBuilder builder) {
    this.idCourse = builder.idCourse;
    this.idStudent = builder.idStudent;
    this.createdAt = builder.createdAt;
  }

  public String getIdCourse() {
    return idCourse;
  }

  public String getIdStudent() {
    return idStudent;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public static class EnrollmentBuilder {
    protected String idCourse;
    protected String idStudent;
    protected String createdAt;

    public EnrollmentBuilder setIdCourse(String idCourse) {
      this.idCourse = idCourse;
      return this;
    }

    public EnrollmentBuilder setIdStudent(String idStudent) {
      this.idStudent = idStudent;
      return this;
    }

    public EnrollmentBuilder setCreatedAt(String createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public EnrollmentDTO build() {
      return new EnrollmentDTO(this);
    }
  }
}

