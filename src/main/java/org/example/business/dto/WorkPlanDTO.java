package org.example.business.dto;

import org.example.business.Validator;

import java.time.LocalDateTime;

public class WorkPlanDTO {
  private final int idProject;
  private final String projectGoal;
  private final String theoreticalScope;
  private final String firstMonthActivities;
  private final String secondMonthActivities;
  private final String thirdMonthActivities;
  private final String fourthMonthActivities;
  private final LocalDateTime createdAt;

  public WorkPlanDTO(WorkPlanBuilder builder) {
    this.idProject = builder.idProject;
    this.projectGoal = builder.projectGoal;
    this.theoreticalScope = builder.theoreticalScope;
    this.firstMonthActivities = builder.firstMonthActivities;
    this.secondMonthActivities = builder.secondMonthActivities;
    this.thirdMonthActivities = builder.thirdMonthActivities;
    this.fourthMonthActivities = builder.fourthMonthActivities;
    this.createdAt = builder.createdAt;
  }

  public int getIDProject() {
    return idProject;
  }

  public String getProjectGoal() {
    return projectGoal;
  }

  public String getTheoreticalScope() {
    return theoreticalScope;
  }

  public String getFirstMonthActivities() {
    return firstMonthActivities;
  }

  public String getSecondMonthActivities() {
    return secondMonthActivities;
  }

  public String getThirdMonthActivities() {
    return thirdMonthActivities;
  }

  public String getFourthMonthActivities() {
    return fourthMonthActivities;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public static class WorkPlanBuilder {
    private int idProject;
    private String projectGoal;
    private String theoreticalScope;
    private String firstMonthActivities;
    private String secondMonthActivities;
    private String thirdMonthActivities;
    private String fourthMonthActivities;
    private LocalDateTime createdAt;

    public WorkPlanBuilder setIDProject(int idProject) {
      this.idProject = idProject;
      return this;
    }

    public WorkPlanBuilder setProjectGoal(String projectGoal) {
      this.projectGoal = Validator.getValidText(projectGoal, "Objetivo del Proyecto");
      return this;
    }

    public WorkPlanBuilder setTheoreticalScope(String theoreticalScope) {
      this.theoreticalScope = Validator.getValidText(theoreticalScope, "Alcance Teórico");
      return this;
    }

    public WorkPlanBuilder setFirstMonthActivities(String firstMonthActivities) {
      this.firstMonthActivities = Validator.getValidText(firstMonthActivities, "Actividades del Primer Mes");
      return this;
    }

    public WorkPlanBuilder setSecondMonthActivities(String secondMonthActivities) {
      this.secondMonthActivities = Validator.getValidText(secondMonthActivities, "Actividades del Segundo Mes");
      return this;
    }

    public WorkPlanBuilder setThirdMonthActivities(String thirdMonthActivities) {
      this.thirdMonthActivities = Validator.getValidText(thirdMonthActivities, "Actividades del Tercer Mes");
      return this;
    }

    public WorkPlanBuilder setFourthMonthActivities(String fourthMonthActivities) {
      this.fourthMonthActivities = Validator.getValidText(fourthMonthActivities, "Actividades del Cuarto Mes");
      return this;
    }

    public WorkPlanBuilder setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public WorkPlanDTO build() {
      return new WorkPlanDTO(this);
    }
  }
}
