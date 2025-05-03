package org.example.business.dao.filter;

public class FilterEvaluation extends FilterProject {
  private final String idAcademic;

  public FilterEvaluation(int idProject, String idStudent, String idAcademic) {
    super(idProject, idStudent);
    this.idAcademic = idAcademic;
  }

  public String getIDAcademic() {
    return idAcademic;
  }
}
