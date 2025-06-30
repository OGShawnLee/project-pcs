package org.example.business.dto.enumeration;

public enum AcademicRole {
  ACADEMIC,
  ACADEMIC_EVALUATOR,
  EVALUATOR;

  @Override
  public String toString() {
    return switch (this) {
      case ACADEMIC -> "Académico";
      case ACADEMIC_EVALUATOR -> "Académico Evaluador";
      case EVALUATOR -> "Evaluador";
    };
  }

  public String toDBString() {
    return this.name();
  }
}
