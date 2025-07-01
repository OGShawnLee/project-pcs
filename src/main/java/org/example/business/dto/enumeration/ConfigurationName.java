package org.example.business.dto.enumeration;

public enum ConfigurationName {
  EVALUATION_ENABLED_FIRST,
  EVALUATION_ENABLED_SECOND,
  EVALUATION_ENABLED_FINAL;

  @Override
  public String toString() {
    return switch (this) {
      case EVALUATION_ENABLED_FIRST -> "Evaluación de Primer Periodo Parcial";
      case EVALUATION_ENABLED_SECOND -> "Evaluación de Segundo Periodo Parcial";
      case EVALUATION_ENABLED_FINAL -> "Evaluación Final";
    };
  }

  public String toDBName() {
    return this.name();
  }

  public EvaluationKind toEvaluationKind() {
    return switch (this) {
      case EVALUATION_ENABLED_FIRST -> EvaluationKind.FIRST_PERIOD;
      case EVALUATION_ENABLED_SECOND -> EvaluationKind.SECOND_PERIOD;
      case EVALUATION_ENABLED_FINAL -> EvaluationKind.FINAL;
    };
  }
}
