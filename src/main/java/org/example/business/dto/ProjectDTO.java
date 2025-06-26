package org.example.business.dto;

import org.example.business.Validator;
import org.example.business.dao.ProjectSector;

import java.time.LocalDateTime;

public class ProjectDTO implements Record {
  private int id;
  private final String idOrganization;
  private final String name;
  private final String description;
  private final String department;
  private final int availablePlaces;
  private final String methodology;
  private final String state;
  private final ProjectSector sector;
  private final LocalDateTime createdAt;

  public ProjectDTO(ProjectBuilder builder) {
    this.id = builder.id;
    this.idOrganization = builder.idOrganization;
    this.name = builder.name;
    this.description = builder.description;
    this.department = builder.department;
    this.availablePlaces = builder.availablePlaces;
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

  public String getDescription() {
    return description;
  }

  public String getDepartment() {
    return department;
  }

  public int getAvailablePlaces() {
    return availablePlaces;
  }

  public String getState() {
    return state;
  }

  public ProjectSector getSector() {
    return sector;
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    ProjectDTO that = (ProjectDTO) instance;

    return
      id == that.id &&
      idOrganization.equals(that.idOrganization) &&
      name.equals(that.name) &&
      description.equals(that.description) &&
      department.equals(that.department) &&
      availablePlaces == that.availablePlaces &&
      methodology.equals(that.methodology) &&
      state.equals(that.state) &&
      sector.equals(that.sector);
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public static class ProjectBuilder {
    protected int id;
    protected String idOrganization;
    protected String name;
    protected String methodology;
    protected String description;
    protected String department;
    protected int availablePlaces;
    protected String state;
    protected ProjectSector sector;
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

    public ProjectBuilder setDescription(String description) throws IllegalArgumentException {
      this.description = Validator.getValidText(description, "Descripción del Proyecto");
      return this;
    }

    public ProjectBuilder setDepartment(String department) throws IllegalArgumentException {
      this.department = Validator.getValidFlexibleName(department, "Departamento", 2, 128);
      return this;
    }

    public ProjectBuilder setAvailablePlaces(String availablePlaces) throws IllegalArgumentException {
      this.availablePlaces = Validator.getValidInteger(availablePlaces, "Plazas Disponibles", 1);
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

    public ProjectBuilder setSector(ProjectSector sector) throws IllegalArgumentException {
      this.sector = sector;
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

