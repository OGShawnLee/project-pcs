package org.example.business.dto;

import org.example.business.validation.Validator;

import java.time.LocalDateTime;

public class EvaluationDTO {
    private int idProject;
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

    public void setIDProject(int idProject) {
        this.idProject = idProject;
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

    @Override
    public boolean equals(Object instance) {
        if (this == instance) return true;
        if (instance == null || getClass() != instance.getClass()) return false;

        EvaluationDTO that = (EvaluationDTO) instance;

        return
          idAcademic.equals(that.idAcademic) &&
          idStudent.equals(that.idStudent) &&
          idProject == that.idProject &&
          contentGrade == that.contentGrade &&
          feedback.equals(that.feedback) &&
          requirementsGrade == that.requirementsGrade &&
          skillGrade == that.skillGrade &&
          writingGrade == that.writingGrade;
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
            this.idStudent = Validator.getValidEnrollment(idStudent);
            return this;
        }

        public EvaluationBuilder setIDAcademic(String idAcademic) {
            this.idAcademic = Validator.getValidWorkerID(idAcademic);
            return this;
        }

        public EvaluationBuilder setSkillGrade(String skillGrade) {
            this.skillGrade = Validator.getValidGrade(skillGrade);
            return this;
        }

        public EvaluationBuilder setContentGrade(String contentGrade) {
            this.contentGrade = Validator.getValidGrade(contentGrade);
            return this;
        }

        public EvaluationBuilder setWritingGrade(String writingGrade) {
            this.writingGrade = Validator.getValidGrade(writingGrade);
            return this;
        }

        public EvaluationBuilder setRequirementsGrade(String requirementsGrade) {
            this.requirementsGrade = Validator.getValidGrade(requirementsGrade);;
            return this;
        }

        public EvaluationBuilder setFeedback(String feedback) {
            this.feedback = Validator.getValidText(feedback, "Feedback");
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
