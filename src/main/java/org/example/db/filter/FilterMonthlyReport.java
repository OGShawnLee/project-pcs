package org.example.db.filter;

public class FilterMonthlyReport extends FilterProject {
  private final int month;
  private final int year;

  public FilterMonthlyReport(String idStudent, int idProject, int month, int year) {
    super(idProject, idStudent);
    this.month = month;
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public int getYear() {
    return year;
  }
}
