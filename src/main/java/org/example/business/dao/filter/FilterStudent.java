package org.example.business.dao.filter;

public abstract class FilterStudent {
  private final String idStudent;

  public FilterStudent(String idStudent) {
    this.idStudent = idStudent;
  }

  public String getIDStudent() {
    return idStudent;
  }
}
