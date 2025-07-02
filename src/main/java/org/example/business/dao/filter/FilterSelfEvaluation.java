package org.example.business.dao.filter;

public class FilterSelfEvaluation extends FilterStudent {
  private final String idCourse;

  public FilterSelfEvaluation(String idStudent, String idCourse) {
    super(idStudent);
    this.idCourse = idCourse;
  }

  public String getIDCourse() {
    return idCourse;
  }
}
