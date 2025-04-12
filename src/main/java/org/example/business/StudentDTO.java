package org.example.business;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDTO extends Person {
  private String id;

  public StudentDTO(SBuilder builder) {
    super(builder);
    this.id = builder.id;
  }

  public StudentDTO(ResultSet resultSet) throws SQLException {
    super(
      new SBuilder()
        .setID(resultSet.getString("id_student"))
        .setEmail(resultSet.getString("email"))
        .setName(resultSet.getString("name"))
        .setPaternalLastName(resultSet.getString("paternal_last_name"))
        .setMaternalLastName(resultSet.getString("maternal_last_name"))
    );
    this.id = resultSet.getString("id_student");
  }

  public String getID() {
    return id;
  }

  public static class SBuilder extends PersonBuilder<SBuilder> {
    private String id;

    public SBuilder setID(String id) {
      this.id = id;
      return this;
    }

    @Override
    public StudentDTO build() {
      return new StudentDTO(this);
    }
  }
}