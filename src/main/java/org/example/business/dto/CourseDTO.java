package org.example.business.dto;

import org.example.business.Validator;
import org.example.business.dto.enumeration.CourseState;
import org.example.business.dto.enumeration.Section;
import org.example.business.dto.enumeration.Semester;

import java.time.LocalDateTime;

public class CourseDTO implements Record {
  private final String nrc;
  private final String idAcademic;
  private final Semester semester;
  private final Section section;
  private final CourseState state;
  private final String fullNameAcademic;
  private final int totalStudents;
  private final LocalDateTime createdAt;

  public CourseDTO(CourseBuilder builder) {
    this.nrc = builder.nrc;
    this.idAcademic = builder.idAcademic;
    this.semester = builder.semester;
    this.section = builder.section;
    this.state = builder.state;
    this.fullNameAcademic = builder.fullNameAcademic;
    this.totalStudents = builder.totalStudents;
    this.createdAt = builder.createdAt;
  }

  public String getNRC() {
    return nrc;
  }

  public String getIDAcademic() {
    return idAcademic;
  }

  public Semester getSemester() {
    return semester;
  }

  public String getFormattedSemester() {
    String courseYear = semester == Semester.FEB_JUL
      ? createdAt.getYear() + ""
      : createdAt.getYear() + 1 + "";

    return semester.toString() + " - " + courseYear;
  }

  public Section getSection() {
    return section;
  }

  public CourseState getState() {
    return state;
  }

  public int getTotalStudents() {
    return totalStudents;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getFullNameAcademic() {
    return fullNameAcademic;
  }

  @Override
  public String toString() {
    return String.format(
      "%s - %s (%s) - %s",
      nrc,
      fullNameAcademic,
      getFormattedSemester(),
      section
    );
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    CourseDTO that = (CourseDTO) instance;

    return
      nrc.equals(that.nrc) &&
      idAcademic.equals(that.idAcademic) &&
      section == that.section &&
      totalStudents == that.totalStudents &&
      state == that.state;
  }

  public static class CourseBuilder {
    protected String nrc;
    protected String idAcademic;
    protected Semester semester;
    protected Section section;
    protected CourseState state;
    protected String fullNameAcademic;
    protected int totalStudents;
    protected LocalDateTime createdAt;

    public CourseBuilder setNRC(String nrc) {
      this.nrc = Validator.getValidNRC(nrc);
      return this;
    }

    public CourseBuilder setIDAcademic(String idAcademic) {
      this.idAcademic = Validator.getValidWorkerID(idAcademic);
      return this;
    }

    public CourseBuilder setSemester(Semester semester) {
      this.semester = semester;
      return this;
    }

    public CourseBuilder setSection(Section section) {
      this.section = section;
      return this;
    }

    public CourseBuilder setState(CourseState state) {
      this.state = state;
      return this;
    }

    public CourseBuilder setFullNameAcademic(String fullNameAcademic) {
      this.fullNameAcademic = fullNameAcademic;
      return this;
    }

    public CourseBuilder setTotalStudents(int totalStudents) {
      this.totalStudents = totalStudents;
      return this;
    }

    public CourseBuilder setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public CourseDTO build() {
      return new CourseDTO(this);
    }
  }
}