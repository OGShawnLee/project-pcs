package org.example.db.dao.filter;

public class FilterProject extends FilterStudent {
  private final int idProject;

  public FilterProject(int idProject, String idStudent) {
    super(idStudent);
    this.idProject = idProject;
  }

  public int getIDProject() {
    return idProject;
  }
}