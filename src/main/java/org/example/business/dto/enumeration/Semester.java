package org.example.business.dto.enumeration;

public enum Semester {
  FEB_JUL,
  AUG_JAN;

  @Override
  public String toString() {
    return switch (this) {
      case FEB_JUL -> "Febrero - Julio";
      case AUG_JAN -> "Agosto - Enero";
    };
  }

  public String toDBString() {
    return this.name();
  }
}
