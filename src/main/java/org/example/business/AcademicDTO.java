package org.example.business;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AcademicDTO extends Person {
    private String id;

    public AcademicDTO(SBuilder builder) {
        super(builder);
        this.id = builder.id;
    }

    public AcademicDTO(ResultSet resultSet) throws SQLException {
        super(
                new SBuilder()
                        .setID(resultSet.getString("id_academic"))
                        .setEmail(resultSet.getString("email"))
                        .setName(resultSet.getString("name"))
                        .setPaternalLastName(resultSet.getString("paternal_last_name"))
                        .setMaternalLastName(resultSet.getString("maternal_last_name"))
                        .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .setState(resultSet.getString("state"))
        );
        this.id = resultSet.getString("id_academic");
    }

    public String getID() {
        return id;
    }

    public static class SBuilder extends PBuilder<SBuilder> {
        private String id;

        public SBuilder setID(String id) {
            this.id = id;
            return this;
        }

        @Override
        public AcademicDTO build() {
            return new AcademicDTO(this);
        }
    }
}
