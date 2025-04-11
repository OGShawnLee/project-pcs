package org.example.business;

public class MonthlyReportDTO {
    private final String idProject;
    private final String idStudent;
    private final int month;
    private final int year;
    private final int workedHours;
    private final String createdAt;

    public MonthlyReportDTO(Builder builder) {
        this.idProject = builder.idProject;
        this.idStudent = builder.idStudent;
        this.month = builder.month;
        this.year = builder.year;
        this.workedHours = builder.workedHours;
        this.createdAt = builder.createdAt;
    }

    public String getIdProject() {
        return idProject;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public int getMonth(){ return month; }

    public int getYear(){ return year; }

    public int getWorkedHours() { return workedHours; }

    public String getCreatedAt() {
        return createdAt;
    }

    public static class Builder {
        protected String idProject;
        protected String idStudent;
        protected int month;
        protected int year;
        protected int workedHours;
        protected String createdAt;

        public Builder setIdProject(String idProject) {
            this.idProject = idProject;
            return this;
        }

        public Builder setIdStudent(String idStudent) {
            this.idStudent = idStudent;
            return this;
        }

        public Builder setMonth(int month){
            this.month = month;
            return this;
        }

        public Builder setYear(int year){
            this.year = year;
            return this;
        }

        public Builder setWorkedHours(int workedHours){
            this.workedHours = workedHours;
            return this;
        }

        public Builder setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public MonthlyReportDTO build() {
            return new MonthlyReportDTO(this);
        }
    }
}