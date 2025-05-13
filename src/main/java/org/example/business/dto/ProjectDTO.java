package org.example.business.dto;

import org.example.business.Validator;

import java.time.LocalDateTime;

public class ProjectDTO {
  private int id;
  private final String idOrganization;
  private final String name;
  private final String methodology;
  private final String state;
  private final String sector;
  private final LocalDateTime createdAt;

  public ProjectDTO(ProjectBuilder builder) {
    this.id = builder.id;
    this.idOrganization = builder.idOrganization;
    this.name = builder.name;
    this.methodology = builder.methodology;
    this.state = builder.state;
    this.sector = builder.sector;
    this.createdAt = builder.createdAt;
  }

  public int getID() {
    return id;
  }

  public void setID(int id) {
    this.id = id;
  }

  public String getIDOrganization() {
    return idOrganization;
  }

  public String getName() {
    return name;
  }

  public String getMethodology() {
    return methodology;
  }

  public String getState() {
    return state;
  }

  public String getSector() {
    return sector;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public static class ProjectBuilder {
    protected int id;
    protected String idOrganization;
    protected String name;
    protected String methodology;
    protected String state;
    protected String sector;
    protected LocalDateTime createdAt;

    public ProjectBuilder setID(int id) {
      this.id = id;
      return this;
    }

    public ProjectBuilder setIDOrganization(String idOrganization) throws IllegalArgumentException {
      this.idOrganization = Validator.getValidEmail(idOrganization);
      return this;
    }

    public ProjectBuilder setName(String name) throws IllegalArgumentException {
      this.name = Validator.getValidFlexibleName(name, "Nombre del Proyecto", 3, 128);
      return this;
    }

    public ProjectBuilder setMethodology(String methodology) throws IllegalArgumentException {
      this.methodology = Validator.getValidFlexibleName(methodology, "Metodología", 2, 128);
      return this;
    }

    public ProjectBuilder setState(String state) throws IllegalArgumentException {
      this.state = Validator.getValidState(state);
      return this;
    }

    public ProjectBuilder setSector(String sector) throws IllegalArgumentException {
      this.sector = Validator.getValidProjectSector(sector);
      return this;
    }


    public ProjectBuilder setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public ProjectDTO build() {
      return new ProjectDTO(this);
    }
  }
}

