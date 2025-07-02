package org.example.business.dao.filter;

public class FilterMonthlyReport extends FilterProject {
  private final int month;
  private final int year;
  private final String idCourse;

  public FilterMonthlyReport(String idStudent, int idProject, int month, int year, String idCourse) {
    super(idProject, idStudent);
    this.month = month;
    this.year = year;
    this.idCourse = idCourse;
  }

  public int getMonth() {
    return month;
  }

  public int getYear() {
    return year;
  }

  public String getIDCourse() {
    return idCourse;
  }
}
