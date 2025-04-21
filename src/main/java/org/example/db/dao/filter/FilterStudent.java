package org.example.db.dao.filter;

public class FilterStudent extends Filter {
  private final String idStudent;

  public FilterStudent(String idStudent) {
    this.idStudent = idStudent;
  }

  public String getIDStudent() {
    return idStudent;
  }
}
