package org.example.db.dao.filter;

public class FilterEnrollment extends FilterStudent {
  private final String idCourse;

  public FilterEnrollment(String idStudent, String idCourse) {
    super(idStudent);
    this.idCourse = idCourse;
  }

  public String getIDCourse() {
    return idCourse;
  }
}
