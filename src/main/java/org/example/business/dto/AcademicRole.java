package org.example.business.dto;

import java.util.HashMap;

public enum AcademicRole {
  EVALUATOR("EVALUATOR"),
  PROFESSOR("PROFESSOR"),
  PROFESSOR_EVALUATOR("PROFESSOR-EVALUATOR");

  private static final HashMap<String, AcademicRole> ACADEMIC_ROLES_SPANISH_LABEL_TO_ROLE = new HashMap<>();

  static {
    ACADEMIC_ROLES_SPANISH_LABEL_TO_ROLE.put("Evaluador", EVALUATOR);
    ACADEMIC_ROLES_SPANISH_LABEL_TO_ROLE.put("Profesor", PROFESSOR);
    ACADEMIC_ROLES_SPANISH_LABEL_TO_ROLE.put("Profesor-Evaluador", PROFESSOR_EVALUATOR);
  }

  private final String role;

  AcademicRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }

  public static AcademicRole getAcademicRoleFromSpanishLabel(String role) {
    return ACADEMIC_ROLES_SPANISH_LABEL_TO_ROLE.get(role);
  }
}
