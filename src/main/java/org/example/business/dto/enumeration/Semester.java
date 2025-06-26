package org.example.business.dto.enumeration;

public enum Semester {
  FEB_JUL("Febrero - Julio"),
  AUG_JAN("Agosto - Enero");

  private final String displayName;

  Semester(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }

  public String toDBString() {
    if (displayName.equals("Febrero - Julio")) {
      return "FEB_JUL";
    } else if (displayName.equals("Agosto - Enero")) {
      return "AUG_JAN";
    } else {
      throw new IllegalArgumentException("Unknown Semester: " + displayName);
    }
  }
}
