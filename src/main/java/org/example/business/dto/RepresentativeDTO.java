package org.example.business.dto;

public class RepresentativeDTO extends Person {
  private final String organizationID;
  private final String position;

  private RepresentativeDTO(RepresentativeBuilder builder) {
    super(builder);
    this.organizationID = builder.organizationID;
    this.position = builder.position;
  }

  public String getOrganizationID() {
    return organizationID;
  }

  public String getPosition() {
    return position;
  }

  @Override
  public String toString() {
    return getFullName() + " (" + position + ") - " + organizationID;
  }

  public static class RepresentativeBuilder extends Person.PersonBuilder<RepresentativeBuilder> {
    private String organizationID;
    private String position;

    public RepresentativeBuilder setOrganizationID(String organizationID) {
      this.organizationID = organizationID;
      return this;
    }

    public RepresentativeBuilder setPosition(String position) {
      this.position = position;
      return this;
    }

    @Override
    public RepresentativeDTO build() {
      return new RepresentativeDTO(this);
    }
  }
}
