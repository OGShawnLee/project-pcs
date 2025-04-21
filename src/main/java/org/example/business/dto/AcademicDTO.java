package org.example.business.dto;

public class AcademicDTO extends Person {
  private final String id;
  private final String role;

  public AcademicDTO(AcademicBuilder builder) {
    super(builder);
    this.id = builder.id;
    this.role = builder.role;
  }

  public String getID() {
    return id;
  }

  public String getRole() {
    return role;
  }

  public static class AcademicBuilder extends PersonBuilder<AcademicBuilder> {
    private String id;
    private String role;

    public AcademicBuilder setID(String id) {
      this.id = id;
      return this;
    }

    public AcademicBuilder setRole(String role) {
      this.role = role;
      return this;
    }

    @Override
    public AcademicDTO build() {
      return new AcademicDTO(this);
    }
  }
}
