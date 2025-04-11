package org.example.business;

public class SelfEvaluationDTO {
    private final String idStudent;
    private final int followUpGrade;
    private final int safetyGrade;
    private final int knowledgeApplicationGrade;
    private final int interestingGrade;
    private final int productivityGrade;
    private final int congruentGrade;
    private final int informatedByOrganization;
    private final int regulatedByOrganization;
    private final int importanceProfessionalDevelopment;
    private final String createdAt;

    public SelfEvaluationDTO(Builder builder) {
        this.idStudent = builder.idStudent;
        this.followUpGrade = builder.followUpGrade;
        this.safetyGrade = builder.safetyGrade;
        this.knowledgeApplicationGrade = builder.knowledgeApplicationGrade;
        this.interestingGrade = builder.interestingGrade;
        this.productivityGrade = builder.productivityGrade;
        this.congruentGrade = builder.congruentGrade;
        this.informatedByOrganization = builder.informatedByOrganization;
        this.regulatedByOrganization = builder.regulatedByOrganization;
        this.importanceProfessionalDevelopment = builder.importanceProfessionalDevelopment;
        this.createdAt = builder.createdAt;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public int getFollowUpGrade() {
        return followUpGrade;
    }

    public int getSafetyGrade() {
        return safetyGrade;
    }

    public int getKnowledgeApplicationGrade() {
        return knowledgeApplicationGrade;
    }

    public int getInterestingGrade() {
        return interestingGrade;
    }

    public int getProductivityGrade() {
        return productivityGrade;
    }

    public int getCongruentGrade() {
        return congruentGrade;
    }

    public int getInformatedByOrganization(){
        return informatedByOrganization;
    }

    public int getRegulatedByOrganization() {
        return regulatedByOrganization;
    }

    public int getImportanceProfessionalDevelopment() {
        return importanceProfessionalDevelopment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public static class Builder {
        protected String idStudent;
        protected int followUpGrade;
        protected int safetyGrade;
        protected int knowledgeApplicationGrade;
        protected int interestingGrade;
        protected int productivityGrade;
        protected int congruentGrade;
        protected int informatedByOrganization;
        protected int regulatedByOrganization;
        protected int importanceProfessionalDevelopment;
        protected String createdAt;

        public Builder setIdStudent(String idStudent) {
            this.idStudent = idStudent;
            return this;
        }

        public Builder SetFollowUpGrade(int followUpGrade) {
            this.followUpGrade = followUpGrade;
            return this;
        }

        public Builder SetSafetyGrade(int safetyGrade) {
            this.safetyGrade = safetyGrade;
            return this;
        }

        public Builder setKnowledgeApplicationGrade(int knowledgeApplicationGrade) {
            this.knowledgeApplicationGrade = knowledgeApplicationGrade;
            return this;
        }

        public Builder setInterestingGrade(int interestingGrade) {
            this.interestingGrade = interestingGrade;
            return this;
        }

        public Builder setProductivityGrade(int productivityGrade) {
            this.productivityGrade = productivityGrade;
            return this;
        }

        public Builder setCongruentGrade(int congruentGrade) {
            this.congruentGrade = congruentGrade;
            return this;
        }

        public Builder setInformatedByOrganization(int informatedByOrganization) {
            this.informatedByOrganization = informatedByOrganization;
            return this;
        }

        public Builder setRegulatedByOrganization(int regulatedByOrganization) {
            this.regulatedByOrganization = regulatedByOrganization;
            return this;
        }

        public Builder setImportanceProfessionalDevelopment(int importanceProfessionalDevelopment) {
            this.importanceProfessionalDevelopment = importanceProfessionalDevelopment;
            return this;
        }

        public SelfEvaluationDTO build() {
            return new SelfEvaluationDTO(this);
        }
    }
}
