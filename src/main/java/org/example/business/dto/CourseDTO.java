package org.example.business.dto;

import org.example.business.Validator;

import java.time.LocalDateTime;

public class CourseDTO {
  public enum Section {S1, S2}

  public enum Semester {
    FEB_JUL("FEB-JUL"),
    AGO_JAN("AGO-JAN");

    private final String displayName;

    Semester(String displayName) {
      this.displayName = displayName;
    }

    @Override
    public String toString() {
      return displayName;
    }

    public String toDBString() {
      return displayName.replace("-", "_").toUpperCase();
    }
  }

  public enum State {ON_GOING, COMPLETED}

  private final String nrc;
  private final String idAcademic;
  private final Semester semester;
  private final Section section;
  private final State state;
  private final LocalDateTime createdAt;

  public CourseDTO(CourseBuilder builder) {
    this.nrc = builder.nrc;
    this.idAcademic = builder.idAcademic;
    this.semester = builder.semester;
    this.section = builder.section;
    this.state = builder.state;
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

  public Section getSection() {
    return section;
  }

  public State getState() {
    return state;
  }

  public LocalDateTime getEndedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    CourseDTO that = (CourseDTO) instance;

    return nrc.equals(that.nrc) && idAcademic.equals(that.idAcademic) && section == that.section && state == that.state;
  }

  public static class CourseBuilder {
    protected String nrc;
    protected String idAcademic;
    protected Semester semester;
    protected Section section;
    protected State state;
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

    public CourseBuilder setState(State state) {
      this.state = state;
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