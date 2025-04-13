package org.example.business;

import java.time.LocalDateTime;

public class EnrollmentDTO {
  private final String idCourse;
  private final String idStudent;
  private final LocalDateTime createdAt;

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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public static class EnrollmentBuilder {
    protected String idCourse;
    protected String idStudent;
    protected LocalDateTime createdAt;

    public EnrollmentBuilder setIdCourse(String idCourse) {
      this.idCourse = idCourse;
      return this;
    }

    public EnrollmentBuilder setIdStudent(String idStudent) {
      this.idStudent = idStudent;
      return this;
    }

    public EnrollmentBuilder setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public EnrollmentDTO build() {
      return new EnrollmentDTO(this);
    }
  }
}

