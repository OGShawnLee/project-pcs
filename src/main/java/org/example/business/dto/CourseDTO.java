package org.example.business.dto;

import java.time.LocalDateTime;

public class CourseDTO {
  private final String nrc;
  private final String idAcademic;
  private final String section;
  private final LocalDateTime startedAt;
  private final LocalDateTime endedAt;

  public CourseDTO(CourseBuilder builder) {
    this.nrc = builder.nrc;
    this.idAcademic = builder.idAcademic;
    this.section = builder.section;
    this.startedAt = builder.startedAt;
    this.endedAt = builder.endedAt;
  }

  public String getNRC() {
    return nrc;
  }

  public String getIDAcademic() {
    return idAcademic;
  }

  public String getSection() {
    return section;
  }

  public LocalDateTime getStartedAt() {
    return startedAt;
  }

  public LocalDateTime getEndedAt() {
    return endedAt;
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    CourseDTO that = (CourseDTO) instance;

    return nrc.equals(that.nrc) && idAcademic.equals(that.idAcademic) && section.equals(that.section);
  }

  public static class CourseBuilder {
    protected String nrc;
    protected String idAcademic;
    protected String section;
    protected LocalDateTime startedAt;
    protected LocalDateTime endedAt;

    public CourseBuilder setNRC(String nrc) {
      this.nrc = nrc;
      return this;
    }

    public CourseBuilder setIDAcademic(String idAcademic) {
      this.idAcademic = idAcademic;
      return this;
    }

    public CourseBuilder setSection(String section) {
      this.section = section;
      return this;
    }

    public CourseBuilder setStartedAt(LocalDateTime startedAt) {
      this.startedAt = startedAt;
      return this;
    }

    public CourseBuilder setEndedAt(LocalDateTime endedAt) {
      this.endedAt = endedAt;
      return this;
    }

    public CourseDTO build() {
      return new CourseDTO(this);
    }
  }
}


