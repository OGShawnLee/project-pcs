package org.example.business;

import java.time.LocalDateTime;

public class MonthlyReportDTO {
  private final int idProject;
  private final String idStudent;
  private final int month;
  private final int year;
  private final int workedHours;
  private final String report;
  private final LocalDateTime createdAt;

  public MonthlyReportDTO(MonthlyReportBuilder builder) {
    this.idProject = builder.idProject;
    this.idStudent = builder.idStudent;
    this.month = builder.month;
    this.year = builder.year;
    this.workedHours = builder.workedHours;
    this.report = builder.report;
    this.createdAt = builder.createdAt;
  }

  public int getIDProject() {
    return idProject;
  }

  public String getIDStudent() {
    return idStudent;
  }

  public int getMonth() {
    return month;
  }

  public int getYear() {
    return year;
  }

  public int getWorkedHours() {
    return workedHours;
  }

  public String getReport() {
    return report;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public static class MonthlyReportBuilder {
    protected int idProject;
    protected String idStudent;
    protected int month;
    protected int year;
    protected int workedHours;
    protected String report;
    protected LocalDateTime createdAt;

    public MonthlyReportBuilder setIDProject(int idProject) {
      this.idProject = idProject;
      return this;
    }

    public MonthlyReportBuilder setIDStudent(String idStudent) {
      this.idStudent = idStudent;
      return this;
    }

    public MonthlyReportBuilder setMonth(int month) {
      this.month = month;
      return this;
    }

    public MonthlyReportBuilder setYear(int year) {
      this.year = year;
      return this;
    }

    public MonthlyReportBuilder setWorkedHours(int workedHours) {
      this.workedHours = workedHours;
      return this;
    }

    public MonthlyReportBuilder setReport(String report) {
      this.report = report;
      return this;
    }

    public MonthlyReportBuilder setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public MonthlyReportDTO build() {
      return new MonthlyReportDTO(this);
    }
  }
}