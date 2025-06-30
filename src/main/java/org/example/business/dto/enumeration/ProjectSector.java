package org.example.business.dto.enumeration;

public enum ProjectSector {
  PRIVATE,
  PUBLIC,
  SOCIAL;

  @Override
  public String toString() {
    return switch (this) {
      case PRIVATE -> "Privado";
      case PUBLIC -> "Público";
      case SOCIAL -> "Social";
    };
  }

  public String toDBString() {
    return this.name();
  }
}