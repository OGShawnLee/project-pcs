package org.example.business.dto.enumeration;

import org.example.business.dto.AccountDTO;

public enum AccountRole {
  COORDINATOR,
  ACADEMIC,
  ACADEMIC_EVALUATOR,
  EVALUATOR,
  STUDENT;

  public static AccountRole fromAcademicRole(AcademicRole academicRole) {
    return switch (academicRole) {
      case ACADEMIC -> AccountRole.ACADEMIC;
      case ACADEMIC_EVALUATOR -> AccountRole.ACADEMIC_EVALUATOR;
      case EVALUATOR -> AccountRole.EVALUATOR;
    };
  }
}
