package org.example.business;

public class EnrollmentDTO {
    private final String idCourse;
    private final String idStudent;
    private final String createdAt;

    public EnrollmentDTO(Builder builder) {
        this.idCourse = builder.idCourse;
        this.idStudent = builder.idStudent;
        this.createdAt = builder.createdAt;
    }

    public String getIdCourse() {
        return idCourse;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public static class Builder {
        protected String idCourse;
        protected String idStudent;
        protected String createdAt;

        public Builder setIdCourse(String idCourse) {
            this.idCourse = idCourse;
            return this;
        }

        public Builder setIdStudent(String idStudent) {
            this.idStudent = idStudent;
            return this;
        }

        public Builder setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public EnrollmentDTO build() {
            return new EnrollmentDTO(this);
        }
    }
}

