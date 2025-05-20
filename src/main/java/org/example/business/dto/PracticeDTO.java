package org.example.business.dto;

import org.example.business.Validator;

public class PracticeDTO {
  private final String idStudent;
  private final int idProject;
  private final String reasonOfAssignation;

  public PracticeDTO(PracticeBuilder builder) {
    this.idStudent = builder.idStudent;
    this.idProject = builder.idProject;
    this.reasonOfAssignation = builder.reasonOfAssignation;
  }

  public String getIDStudent() {
    return idStudent;
  }

  public int getIDProject() {
    return idProject;
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    PracticeDTO that = (PracticeDTO) instance;

    return idProject == that.idProject && idStudent.equals(that.idStudent) && reasonOfAssignation.equals(that.reasonOfAssignation);
  }

  public String getReasonOfAssignation() {
    return reasonOfAssignation;
  }

  public static class PracticeBuilder {
    protected String idStudent;
    protected int idProject;
    protected String reasonOfAssignation;

    public PracticeBuilder setIDStudent(String idStudent) {
      this.idStudent = Validator.getValidEnrollment(idStudent);
      return this;
    }

    public PracticeBuilder setIDProject(int idProject) {
      this.idProject = idProject;
      return this;
    }

    public PracticeBuilder setReasonOfAssignation(String reasonOfAssignation) {
      this.reasonOfAssignation = Validator.getValidText(reasonOfAssignation, "Razon de Asignación");
      return this;
    }

    public PracticeDTO build() {
      return new PracticeDTO(this);
    }
  }
}
