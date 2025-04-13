package org.example.business;

public class PracticeDTO {
  private final String idStudent;
  private final String idProject;
  private final String reasonOfAssignation;

  public PracticeDTO(PracticeBuilder builder) {
    this.idStudent = builder.idStudent;
    this.idProject = builder.idProject;
    this.reasonOfAssignation = builder.reasonOfAssignation;
  }

  public String getIDStudent() {
    return idStudent;
  }

  public String getIDProject() {
    return idProject;
  }

  public String getReasonOfAssignation() {
    return reasonOfAssignation;
  }

  public static class PracticeBuilder {
    protected String idStudent;
    protected String idProject;
    protected String reasonOfAssignation;

    public PracticeBuilder setIDStudent(String idStudent) {
      this.idStudent = idStudent;
      return this;
    }

    public PracticeBuilder setIDProject(String idProject) {
      this.idProject = idProject;
      return this;
    }

    public PracticeBuilder SetReasonOfAssignation(String reasonOfAssignation) {
      this.reasonOfAssignation = reasonOfAssignation;
      return this;
    }

    public PracticeDTO build() {
      return new PracticeDTO(this);
    }
  }
}
