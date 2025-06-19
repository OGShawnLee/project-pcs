package org.example.business.dto;

import org.example.business.Validator;

import java.time.LocalDateTime;

public abstract class Person {
  private final String name;
  private final String paternalLastName;
  private final String maternalLastName;
  private final String email;
  private final String state;
  private final String phoneNumber;
  private final LocalDateTime createdAt;

  public Person(PersonBuilder<?> builder) {
    this.name = builder.name;
    this.paternalLastName = builder.paternalLastName;
    this.maternalLastName = builder.maternalLastName;
    this.email = builder.email;
    this.state = builder.state;
    this.phoneNumber = builder.phoneNumber;
    this.createdAt = builder.createdAt;
  }

  public String getName() {
    return name;
  }

  public String getPaternalLastName() {
    return paternalLastName;
  }

  public String getMaternalLastName() {
    return maternalLastName;
  }

  public String getFullName() {
    if (maternalLastName == null || maternalLastName.isEmpty()) {
      return name + " " + paternalLastName;
    }

    return name + " " + paternalLastName + " " + maternalLastName;
  }

  public String getEmail() {
    return email;
  }

  public String getState() {
    return state;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    Person that = (Person) instance;

    if (maternalLastName == null) {
      if (that.maternalLastName != null) return false;
    } else if (!maternalLastName.equals(that.maternalLastName)) {
      return false;
    }

    return
      name.equals(that.name) &&
      paternalLastName.equals(that.paternalLastName) &&
      email.equals(that.email) &&
      state.equals(that.state) &&
      phoneNumber.equals(that.phoneNumber);
  }

  public static abstract class PersonBuilder<T extends PersonBuilder<T>> {
    protected String name;
    protected String paternalLastName;
    protected String maternalLastName;
    protected String email;
    protected String state;
    protected String phoneNumber;
    protected LocalDateTime createdAt;

    public T self() {
      return (T) this;
    }

    public T setName(String name) throws IllegalArgumentException {
      this.name = Validator.getValidName(name, "Nombre", 3, 64);
      return self();
    }

    public T setPaternalLastName(String paternalLastName) throws IllegalArgumentException{
      this.paternalLastName = Validator.getValidName(paternalLastName, "Apellido Paterno", 3, 64);
      return self();
    }

    public T setMaternalLastName(String maternalLastName) throws IllegalArgumentException {
      if (maternalLastName == null || maternalLastName.isEmpty()) {
        return self();
      }

      this.maternalLastName = Validator.getValidName(maternalLastName, "Apellido Materno", 3, 64);
      return self();
    }

    public T setEmail(String email) throws IllegalArgumentException {
      this.email = Validator.getValidEmail(email);
      return self();
    }

    public T setState(String state) throws IllegalArgumentException {
      this.state = Validator.getValidState(state);
      return self();
    }

    public T setPhoneNumber(String phoneNumber) throws IllegalArgumentException {
      this.phoneNumber = Validator.getValidPhoneNumber(phoneNumber);
      return self();
    }

    public T setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return self();
    }

    public abstract Person build();
  }
}
