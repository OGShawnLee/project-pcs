package org.example.business.dto;

import java.time.LocalDateTime;

public class ProjectRequestDTO {
  private final String idStudent;
  private final String idProject;
  private final String state;
  private final String reasonOfState;
  private final LocalDateTime createdAt;

  public ProjectRequestDTO(ProjectRequestBuilder builder) {
    this.idStudent = builder.idStudent;
    this.idProject = builder.idProject;
    this.state = builder.state;
    this.reasonOfState = builder.reasonOfState;
    this.createdAt = builder.createdAt;
  }

  public String getIDStudent() {
    return idStudent;
  }

  public String getIDProject() {
    return idProject;
  }

  public String getState() {
    return state;
  }

  public String getReasonOfState() {
    return reasonOfState;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public static class ProjectRequestBuilder {
    protected String idStudent;
    protected String idProject;
    protected String state;
    protected String reasonOfState;
    protected LocalDateTime createdAt;

    public ProjectRequestBuilder setIDStudent(String idStudent) {
      this.idStudent = idStudent;
      return this;
    }

    public ProjectRequestBuilder setIDProject(String idProject) {
      this.idProject = idProject;
      return this;
    }

    public ProjectRequestBuilder setState(String state) {
      this.state = state;
      return this;
    }

    public ProjectRequestBuilder setReasonOfState(String reasonOfState) {
      this.reasonOfState = reasonOfState;
      return this;
    }

    public ProjectRequestBuilder setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public ProjectRequestDTO build() {
      return new ProjectRequestDTO(this);
    }
  }
}


