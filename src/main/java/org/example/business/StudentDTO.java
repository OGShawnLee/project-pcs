package org.example.business;

public class StudentDTO extends Person {
  private final String id;

  public StudentDTO(StudentBuilder builder) {
    super(builder);
    this.id = builder.id;
  }

  public String getID() {
    return id;
  }

  public static class StudentBuilder extends PersonBuilder<StudentBuilder> {
    private String id;

    public StudentBuilder setID(String id) {
      this.id = id;
      return this;
    }

    @Override
    public StudentDTO build() {
      return new StudentDTO(this);
    }
  }
}