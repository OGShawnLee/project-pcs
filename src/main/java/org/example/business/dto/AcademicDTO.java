package org.example.business.dto;

import org.example.business.Validator;
import org.example.business.dto.enumeration.AcademicRole;

public class AcademicDTO extends Person {
  private final String id;
  private final AcademicRole role;

  public AcademicDTO(AcademicBuilder builder) {
    super(builder);
    this.id = builder.id;
    this.role = builder.role;
  }

  public String getID() {
    return id;
  }

  public AcademicRole getRole() {
    return role;
  }

  @Override
  public String toString() {
    return getFullName() + " (" +  getID()  + ")";
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    AcademicDTO that = (AcademicDTO) instance;

    return id.equals(that.id) && role.equals(that.role) && super.equals(that);
  }

  public static class AcademicBuilder extends PersonBuilder<AcademicBuilder> {
    private String id;
    private AcademicRole role;

    public AcademicBuilder setID(String id) throws IllegalArgumentException {
      this.id = Validator.getValidWorkerID(id);
      return this;
    }

    public AcademicBuilder setRole(AcademicRole role) throws IllegalArgumentException {
      this.role = role;
      return this;
    }

    @Override
    public AcademicDTO build() {
      return new AcademicDTO(this);
    }
  }
}
