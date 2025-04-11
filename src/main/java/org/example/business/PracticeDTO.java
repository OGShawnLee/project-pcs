package org.example.business;

public class PracticeDTO {
    private final String idStudent;
    private final String idProject;
    private final String reasonOfAssignation;

    public PracticeDTO(Builder builder) {
        this.idStudent = builder.idStudent;
        this.idProject = builder.idProject;
        this.reasonOfAssignation = builder.reasonOfAssignation;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public String getIdProject() {
        return idProject;
    }

    public String getReasonOfAssignation() {
        return reasonOfAssignation;
    }

    public static class Builder {
        protected String idStudent;
        protected String idProject;
        protected String reasonOfAssignation;

        public Builder setIdStudent(String idStudent) {
            this.idStudent = idStudent;
            return this;
        }

        public Builder setIdProject(String idProject) {
            this.idProject = idProject;
            return this;
        }

        public Builder SetReasonOfAssignation(String reasonOfAssignation) {
            this.reasonOfAssignation = reasonOfAssignation;
            return this;
        }

        public PracticeDTO build() {
            return new PracticeDTO(this);
        }
    }
}
