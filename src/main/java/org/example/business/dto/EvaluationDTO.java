package org.example.business.dto;

import org.example.business.Validator;
import org.example.business.dto.enumeration.EvaluationKind;

import java.time.LocalDateTime;

public class EvaluationDTO {
  private int idProject;
  private final String idStudent;
  private final String idAcademic;
  private final int adequateUseGrade;
  private final int contentCongruenceGrade;
  private final int writingGrade;
  private final int methodologicalRigorGrade;
  private final String feedback;
  private final EvaluationKind kind;
  private final LocalDateTime createdAt;

  public EvaluationDTO(EvaluationBuilder builder) {
    this.idProject = builder.idProject;
    this.idStudent = builder.idStudent;
    this.idAcademic = builder.idAcademic;
    this.adequateUseGrade = builder.adequateUseGrade;
    this.contentCongruenceGrade = builder.contentCongruenceGrade;
    this.writingGrade = builder.writingGrade;
    this.methodologicalRigorGrade = builder.methodologicalRigorGrade;
    this.feedback = builder.feedback;
    this.kind = builder.kind;
    this.createdAt = builder.createdAt;
  }

  public int getIDProject() {
    return idProject;
  }

  public void setIDProject(int id) {
    this.idProject = id;
  }

  public String getIDStudent() {
    return idStudent;
  }

  public String getIDAcademic() {
    return idAcademic;
  }

  public int getAdequateUseGrade() {
    return adequateUseGrade;
  }

  public int getContentCongruenceGrade() {
    return contentCongruenceGrade;
  }

  public int getWritingGrade() {
    return writingGrade;
  }

  public int getMethodologicalRigorGrade() {
    return methodologicalRigorGrade;
  }

  public String getFeedback() {
    return feedback;
  }

  public EvaluationKind getKind() {
    return kind;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    EvaluationDTO that = (EvaluationDTO) instance;

    return
      idStudent.equals(that.idStudent) &&
        idProject == that.idProject &&
        adequateUseGrade == that.adequateUseGrade &&
        contentCongruenceGrade == that.contentCongruenceGrade &&
        writingGrade == that.writingGrade &&
        methodologicalRigorGrade == that.methodologicalRigorGrade &&
        feedback.equals(that.feedback) &&
        kind.equals(that.kind);
  }

  public static class EvaluationBuilder {
    private int idProject;
    private String idStudent;
    private String idAcademic;
    private int adequateUseGrade;
    private int contentCongruenceGrade;
    private int writingGrade;
    private int methodologicalRigorGrade;
    private String feedback;
    private EvaluationKind kind;
    private LocalDateTime createdAt;

    public EvaluationBuilder setIDProject(int idProject) {
      this.idProject = idProject;
      return this;
    }

    public EvaluationBuilder setIDStudent(String idStudent) {
      this.idStudent = Validator.getValidEnrollment(idStudent);
      return this;
    }

    public EvaluationBuilder setIDAcademic(String idAcademic) {
      this.idAcademic = Validator.getValidWorkerID(idAcademic);
      return this;
    }

    public EvaluationBuilder setAdequateUseGrade(String adequateUseGrade) {
      this.adequateUseGrade = Validator.getValidGrade(adequateUseGrade);
      return this;
    }

    public EvaluationBuilder setContentCongruenceGrade(String contentCongruenceGrade) {
      this.contentCongruenceGrade = Validator.getValidGrade(contentCongruenceGrade);
      return this;
    }

    public EvaluationBuilder setWritingGrade(String writingGrade) {
      this.writingGrade = Validator.getValidGrade(writingGrade);
      return this;
    }

    public EvaluationBuilder setMethodologicalRigorGrade(String methodologicalRigorGrade) {
      this.methodologicalRigorGrade = Validator.getValidGrade(methodologicalRigorGrade);
      return this;
    }

    public EvaluationBuilder setFeedback(String feedback) {
      this.feedback = Validator.getValidText(feedback, "Retroalimentación");
      return this;
    }

    public EvaluationBuilder setKind(EvaluationKind kind) {
      this.kind = kind;
      return this;
    }

    public EvaluationBuilder setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public EvaluationDTO build() {
      return new EvaluationDTO(this);
    }
  }
}