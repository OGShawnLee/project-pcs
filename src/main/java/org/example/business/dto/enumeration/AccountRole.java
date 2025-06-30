package org.example.business.dto.enumeration;

public enum AccountRole {
  COORDINATOR,
  ACADEMIC,
  ACADEMIC_EVALUATOR,
  EVALUATOR,
  STUDENT;

  @Override
  public String toString() {
    return switch (this) {
      case COORDINATOR -> "Coordinador";
      case ACADEMIC -> "Académico";
      case ACADEMIC_EVALUATOR -> "Académico Evaluador";
      case EVALUATOR -> "Evaluador";
      case STUDENT -> "Estudiante";
    };
  }

  public String toDBString() {
    return this.name();
  }
}
