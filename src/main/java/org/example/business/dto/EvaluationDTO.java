package org.example.business.dto;

import java.time.LocalDateTime;

public class EvaluationDTO {
    private final int idProject;
    private final String idStudent;
    private final String idAcademic;
    private final int skillGrade;
    private final int contentGrade;
    private final int writingGrade;
    private final int requirementsGrade;
    private final String feedback;
    private final LocalDateTime createdAt;

    public EvaluationDTO(EvaluationBuilder builder) {
        this.idProject = builder.idProject;
        this.idStudent = builder.idStudent;
        this.idAcademic = builder.idAcademic;
        this.skillGrade = builder.skillGrade;
        this.contentGrade = builder.contentGrade;
        this.writingGrade = builder.writingGrade;
        this.requirementsGrade = builder.requirementsGrade;
        this.feedback = builder.feedback;
        this.createdAt = builder.createdAt;
    }

    public int getIDProject() {
        return idProject;
    }

    public String getIDStudent() {
        return idStudent;
    }

    public String getIDAcademic() {
        return idAcademic;
    }

    public int getSkillGrade() {
        return skillGrade;
    }

    public int getContentGrade() {
        return contentGrade;
    }

    public int getWritingGrade() {
        return writingGrade;
    }

    public int getRequirementsGrade() {
        return requirementsGrade;
    }

    public String getFeedback() {
        return feedback;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static class EvaluationBuilder {
        private int idProject;
        private String idStudent;
        private String idAcademic;
        private int skillGrade;
        private int contentGrade;
        private int writingGrade;
        private int requirementsGrade;
        private String feedback;
        private LocalDateTime createdAt;

        public EvaluationBuilder setIDProject(int idProject) {
            this.idProject = idProject;
            return this;
        }

        public EvaluationBuilder setIDStudent(String idStudent) {
            this.idStudent = idStudent;
            return this;
        }

        public EvaluationBuilder setIDAcademic(String idAcademic) {
            this.idAcademic = idAcademic;
            return this;
        }

        public EvaluationBuilder setSkillGrade(int skillGrade) {
            this.skillGrade = skillGrade;
            return this;
        }

        public EvaluationBuilder setContentGrade(int contentGrade) {
            this.contentGrade = contentGrade;
            return this;
        }

        public EvaluationBuilder setWritingGrade(int writingGrade) {
            this.writingGrade = writingGrade;
            return this;
        }

        public EvaluationBuilder setRequirementsGrade(int requirementsGrade) {
            this.requirementsGrade = requirementsGrade;
            return this;
        }

        public EvaluationBuilder setFeedback(String feedback) {
            this.feedback = feedback;
            return this;
        }

        public EvaluationBuilder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public EvaluationDTO build() {
            return new EvaluationDTO(this);
        }
    }
}
