package org.example.business.dto;

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

  public String getIDCourse() {
    return idCourse;
  }

  public String getIDStudent() {
    return idStudent;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    EnrollmentDTO that = (EnrollmentDTO) instance;

    return idCourse.equals(that.idCourse) && idStudent.equals(that.idStudent);
  }

  public static class EnrollmentBuilder {
    protected String idCourse;
    protected String idStudent;
    protected LocalDateTime createdAt;

    public EnrollmentBuilder setIDCourse(String idCourse) {
      this.idCourse = idCourse;
      return this;
    }

    public EnrollmentBuilder setIDStudent(String idStudent) {
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

