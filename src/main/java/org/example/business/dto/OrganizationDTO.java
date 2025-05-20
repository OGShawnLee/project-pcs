package org.example.business.dto;

import org.example.business.Validator;

import java.time.LocalDateTime;

public class OrganizationDTO {
  private final String email;
  private final String name;
  private final String representativeFullName;
  private final String colony;
  private final String street;
  private final String state;
  private final LocalDateTime createdAt;

  public OrganizationDTO(OrganizationBuilder builder) {
    this.email = builder.email;
    this.name = builder.name;
    this.representativeFullName = builder.representativeFullName;
    this.colony = builder.colony;
    this.street = builder.street;
    this.state = builder.state;
    this.createdAt = builder.createdAt;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getRepresentativeFullName() {
    return representativeFullName;
  }

  public String getColony() {
    return colony;
  }

  public String getStreet() {
    return street;
  }

  public String getState() {
    return state;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    OrganizationDTO that = (OrganizationDTO) instance;

    return
      email.equals(that.email) &&
      name.equals(that.name) &&
      representativeFullName.equals(that.representativeFullName) &&
      colony.equals(that.colony) &&
      street.equals(that.street) &&
      state.equals(that.state);
  }

  public static class OrganizationBuilder {
    protected String email;
    protected String name;
    protected String representativeFullName;
    protected String colony;
    protected String street;
    protected String state;
    protected LocalDateTime createdAt;

    public OrganizationBuilder setEmail(String email) {
      this.email = Validator.getValidEmail(email);
      return this;
    }

    public OrganizationBuilder setName(String name) {
      this.name = Validator.getValidFlexibleName(name, "Nombre de Organización", 3, 128);
      return this;
    }

    public OrganizationBuilder setRepresentativeFullName(String representativeFullName) {
      this.representativeFullName = Validator.getValidName(representativeFullName, "Nombre del Representante", 3, 128);
      return this;
    }

    public OrganizationBuilder setColony(String colony) {
      this.colony = Validator.getValidFlexibleName(colony, "Colonia", 3, 128);
      return this;
    }

    public OrganizationBuilder setStreet(String street) {
      this.street = Validator.getValidFlexibleName(street, "Calle", 3, 128);
      return this;
    }

    public OrganizationBuilder setState(String state) {
      this.state = Validator.getValidState(state);
      return this;
    }

    public OrganizationBuilder setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public OrganizationDTO build() {
      return new OrganizationDTO(this);
    }
  }
}

