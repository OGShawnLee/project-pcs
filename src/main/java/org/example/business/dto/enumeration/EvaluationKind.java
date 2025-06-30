package org.example.business.dto.enumeration;

public enum EvaluationKind {
  FIRST_PERIOD,
  SECOND_PERIOD,
  FINAL;

  @Override
  public String toString() {
    return switch (this) {
      case FIRST_PERIOD -> "Primer Período";
      case SECOND_PERIOD -> "Segundo Período";
      case FINAL -> "Final";
    };
  }

  public String toDBString() {
    return this.name();
  }
}
