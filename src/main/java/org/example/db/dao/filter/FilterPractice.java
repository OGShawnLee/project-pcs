package org.example.db.dao.filter;

public class FilterPractice extends FilterStudent {
  private final int idPractice;

  public FilterPractice(String idStudent, int idPractice) {
    super(idStudent);
    this.idPractice = idPractice;
  }

  public int getIDPractice() {
    return idPractice;
  }
}
