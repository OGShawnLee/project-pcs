package org.example.business.dto.enumeration;

public enum CourseState {
  ON_GOING, COMPLETED;

  @Override
  public String toString() {
    return switch (this) {
      case ON_GOING -> "En curso";
      case COMPLETED -> "Completado";
    };
  }

  public String toDBString() {
    return this.name();
  }
}
