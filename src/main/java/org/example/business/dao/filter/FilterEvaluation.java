package org.example.business.dao.filter;

import org.example.business.dto.enumeration.EvaluationKind;

public class FilterEvaluation extends FilterProject {
  private final String idAcademic;
  private final EvaluationKind kind;

  public FilterEvaluation(int idProject, String idStudent, String idAcademic, EvaluationKind kind) {
    super(idProject, idStudent);
    this.idAcademic = idAcademic;
    this.kind = kind;
  }

  public String getIDAcademic() {
    return idAcademic;
  }

  public EvaluationKind getKind() {
    return kind;
  }
}
