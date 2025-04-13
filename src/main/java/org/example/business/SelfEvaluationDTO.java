package org.example.business;

import java.time.LocalDateTime;

public class SelfEvaluationDTO {
  private final String idStudent;
  private final int followUpGrade;
  private final int safetyGrade;
  private final int knowledgeApplicationGrade;
  private final int interestingGrade;
  private final int productivityGrade;
  private final int congruentGrade;
  private final int informedByOrganization;
  private final int regulatedByOrganization;
  private final int importanceForProfessionalDevelopment;
  private final LocalDateTime createdAt;

  public SelfEvaluationDTO(SelfEvaluationBuilder builder) {
    this.idStudent = builder.idStudent;
    this.followUpGrade = builder.followUpGrade;
    this.safetyGrade = builder.safetyGrade;
    this.knowledgeApplicationGrade = builder.knowledgeApplicationGrade;
    this.interestingGrade = builder.interestingGrade;
    this.productivityGrade = builder.productivityGrade;
    this.congruentGrade = builder.congruentGrade;
    this.informedByOrganization = builder.informedByOrganization;
    this.regulatedByOrganization = builder.regulatedByOrganization;
    this.importanceForProfessionalDevelopment = builder.importanceForProfessionalDevelopment;
    this.createdAt = builder.createdAt;
  }

  public String getIdStudent() {
    return idStudent;
  }

  public int getFollowUpGrade() {
    return followUpGrade;
  }

  public int getSafetyGrade() {
    return safetyGrade;
  }

  public int getKnowledgeApplicationGrade() {
    return knowledgeApplicationGrade;
  }

  public int getInterestingGrade() {
    return interestingGrade;
  }

  public int getProductivityGrade() {
    return productivityGrade;
  }

  public int getCongruentGrade() {
    return congruentGrade;
  }

  public int getInformedByOrganization() {
    return informedByOrganization;
  }

  public int getRegulatedByOrganization() {
    return regulatedByOrganization;
  }

  public int getImportanceForProfessionalDevelopment() {
    return importanceForProfessionalDevelopment;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public static class SelfEvaluationBuilder {
    protected String idStudent;
    protected int followUpGrade;
    protected int safetyGrade;
    protected int knowledgeApplicationGrade;
    protected int interestingGrade;
    protected int productivityGrade;
    protected int congruentGrade;
    protected int informedByOrganization;
    protected int regulatedByOrganization;
    protected int importanceForProfessionalDevelopment;
    protected LocalDateTime createdAt;

    public SelfEvaluationBuilder setIdStudent(String idStudent) {
      this.idStudent = idStudent;
      return this;
    }

    public SelfEvaluationBuilder SetFollowUpGrade(int followUpGrade) {
      this.followUpGrade = followUpGrade;
      return this;
    }

    public SelfEvaluationBuilder SetSafetyGrade(int safetyGrade) {
      this.safetyGrade = safetyGrade;
      return this;
    }

    public SelfEvaluationBuilder setKnowledgeApplicationGrade(int knowledgeApplicationGrade) {
      this.knowledgeApplicationGrade = knowledgeApplicationGrade;
      return this;
    }

    public SelfEvaluationBuilder setInterestingGrade(int interestingGrade) {
      this.interestingGrade = interestingGrade;
      return this;
    }

    public SelfEvaluationBuilder setProductivityGrade(int productivityGrade) {
      this.productivityGrade = productivityGrade;
      return this;
    }

    public SelfEvaluationBuilder setCongruentGrade(int congruentGrade) {
      this.congruentGrade = congruentGrade;
      return this;
    }

    public SelfEvaluationBuilder setInformedByOrganization(int informedByOrganization) {
      this.informedByOrganization = informedByOrganization;
      return this;
    }

    public SelfEvaluationBuilder setRegulatedByOrganization(int regulatedByOrganization) {
      this.regulatedByOrganization = regulatedByOrganization;
      return this;
    }

    public SelfEvaluationBuilder setImportanceForProfessionalDevelopment(int importanceForProfessionalDevelopment) {
      this.importanceForProfessionalDevelopment = importanceForProfessionalDevelopment;
      return this;
    }

    public SelfEvaluationBuilder setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public SelfEvaluationDTO build() {
      return new SelfEvaluationDTO(this);
    }
  }
}
