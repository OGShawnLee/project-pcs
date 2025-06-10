package org.example.business.dto;

public class StatsDTO {
  private final int totalAcademics;
  private final int totalEvaluators;
  private final int totalOrganizations;
  private final int totalProjectRequests;
  private final int totalEvaluations;
  private final int totalSelfEvaluations;
  private final int totalMonthlyReports;
  private final int totalProjects;
  private final int totalCourses;
  private final int totalStudents;

  private StatsDTO(Builder builder) {
    this.totalAcademics = builder.totalAcademics;
    this.totalEvaluators = builder.totalEvaluators;
    this.totalOrganizations = builder.totalOrganizations;
    this.totalProjectRequests = builder.totalProjectRequests;
    this.totalEvaluations = builder.totalEvaluations;
    this.totalSelfEvaluations = builder.totalSelfEvaluations;
    this.totalMonthlyReports = builder.totalMonthlyReports;
    this.totalProjects = builder.totalProjects;
    this.totalCourses = builder.totalCourses;
    this.totalStudents = builder.totalStudents;
  }

  public int getTotalAcademics() {
    return totalAcademics;
  }

  public int getTotalEvaluators() {
    return totalEvaluators;
  }

  public int getTotalOrganizations() {
    return totalOrganizations;
  }

  public int getTotalProjectRequests() {
    return totalProjectRequests;
  }

  public int getTotalEvaluations() {
    return totalEvaluations;
  }

  public int getTotalSelfEvaluations() {
    return totalSelfEvaluations;
  }

  public int getTotalMonthlyReports() {
    return totalMonthlyReports;
  }

  public int getTotalProjects() {
    return totalProjects;
  }

  public int getTotalCourses() {
    return totalCourses;
  }

  public int getTotalStudents() {
    return totalStudents;
  }

  public static class Builder {
    private int totalAcademics;
    private int totalEvaluators;
    private int totalOrganizations;
    private int totalProjectRequests;
    private int totalEvaluations;
    private int totalSelfEvaluations;
    private int totalMonthlyReports;
    private int totalProjects;
    private int totalCourses;
    private int totalStudents;

    public Builder setTotalAcademics(int totalAcademics) {
      this.totalAcademics = totalAcademics;
      return this;
    }

    public Builder setTotalEvaluators(int totalEvaluators) {
      this.totalEvaluators = totalEvaluators;
      return this;
    }

    public Builder setTotalOrganizations(int totalOrganizations) {
      this.totalOrganizations = totalOrganizations;
      return this;
    }

    public Builder setTotalProjectRequests(int totalProjectRequests) {
      this.totalProjectRequests = totalProjectRequests;
      return this;
    }

    public Builder setTotalEvaluations(int totalEvaluations) {
      this.totalEvaluations = totalEvaluations;
      return this;
    }

    public Builder setTotalSelfEvaluations(int totalSelfEvaluations) {
      this.totalSelfEvaluations = totalSelfEvaluations;
      return this;
    }

    public Builder setTotalMonthlyReports(int totalMonthlyReports) {
      this.totalMonthlyReports = totalMonthlyReports;
      return this;
    }

    public Builder setTotalProjects(int totalProjects) {
      this.totalProjects = totalProjects;
      return this;
    }

    public Builder setTotalCourses(int totalCourses) {
      this.totalCourses = totalCourses;
      return this;
    }

    public Builder setTotalStudents(int totalStudents) {
      this.totalStudents = totalStudents;
      return this;
    }

    public StatsDTO build() {
      return new StatsDTO(this);
    }
  }
}
