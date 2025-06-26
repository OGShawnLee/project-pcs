package org.example.business.dto;

import org.example.business.Validator;

import java.time.LocalDateTime;

public class OrganizationDTO implements Record {
  private final String email;
  private final String name;
  private final String representativeFullName;
  private final String address;
  private final String state;
  private final String phoneNumber;
  private final LocalDateTime createdAt;

  public OrganizationDTO(OrganizationBuilder builder) {
    this.email = builder.email;
    this.name = builder.name;
    this.representativeFullName = builder.representativeFullName;
    this.address = builder.address;
    this.phoneNumber = builder.phoneNumber;
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

  public String getAddress() {
    return address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getState() {
    return state;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public String toString() {
    return name + " (" + email + ")";
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
      address.equals(that.address) &&
      state.equals(that.state);
  }

  public static class OrganizationBuilder {
    protected String email;
    protected String name;
    protected String representativeFullName;
    protected String address;
    protected String state;
    protected String phoneNumber;
    protected LocalDateTime createdAt;

    public OrganizationBuilder setEmail(String email) {
      this.email = Validator.getValidEmail(email);
      return this;
    }

    public OrganizationBuilder setName(String name) {
      this.name = Validator.getValidFlexibleName(name, "Nombre de Organización", 3, 256);
      return this;
    }

    public OrganizationBuilder setRepresentativeFullName(String representativeFullName) {
      this.representativeFullName = Validator.getValidName(representativeFullName, "Nombre del Representante", 3, 128);
      return this;
    }

    public OrganizationBuilder setAddress(String address) {
      this.address = Validator.getValidString(address, "Dirección de Organización", 3, 256);
      return this;
    }

    public OrganizationBuilder setPhoneNumber(String phoneNumber) {
      this.phoneNumber = Validator.getValidPhoneNumber(phoneNumber);
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

