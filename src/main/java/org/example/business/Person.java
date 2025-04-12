package org.example.business;

import java.time.LocalDateTime;

public abstract class Person {
  private final String name;
  private final String paternalLastName;
  private final String maternalLastName;
  private final String email;
  private final String state;
  private final LocalDateTime createdAt;

  public Person(PersonBuilder<?> builder) {
    this.name = builder.name;
    this.paternalLastName = builder.paternalLastName;
    this.maternalLastName = builder.maternalLastName;
    this.email = builder.email;
    this.state = builder.state;
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

  public String getEmail() {
    return email;
  }

  public String getState() {
    return state;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public static abstract class PersonBuilder<T extends PersonBuilder<T>> {
    protected String name;
    protected String paternalLastName;
    protected String maternalLastName;
    protected String email;
    protected String state;
    protected LocalDateTime createdAt;

    public T self() {
      return (T) this;
    }

    public T setName(String name) {
      this.name = name;
      return self();
    }

    public T setPaternalLastName(String paternalLastName) {
      this.paternalLastName = paternalLastName;
      return self();
    }

    public T setMaternalLastName(String maternalLastName) {
      this.maternalLastName = maternalLastName;
      return self();
    }

    public T setEmail(String email) {
      this.email = email;
      return self();
    }

    public T setState(String state) {
      this.state = state;
      return self();
    }

    public T setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return self();
    }

    public abstract Person build();
  }
}
