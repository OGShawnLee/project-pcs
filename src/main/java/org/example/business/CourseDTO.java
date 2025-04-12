package org.example.business;

public class CourseDTO {
    private final String nrc;
    private final String idAcademic;
    private final String section;
    private final String startedAt;
    private final String endedAt;

    public CourseDTO(CourseBuilder builder) {
        this.nrc = builder.nrc;
        this.idAcademic = builder.idAcademic;
        this.section = builder.section;
        this.startedAt = builder.startedAt;
        this.endedAt = builder.endedAt;
    }

    public String getNrc(){
        return nrc;
    }

    public String getIdAcademic() {
        return idAcademic;
    }

    public String getSection() {
        return section;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public String getEndedAt() {
        return endedAt;
    }

    public static class CourseBuilder {
        protected String nrc;
        protected String idAcademic;
        protected String section;
        protected String startedAt;
        protected String endedAt;

        public CourseBuilder setNrc(String nrc){
            this.nrc =nrc;
            return this;
        }

        public CourseBuilder setIdAcademic(String idAcademic) {
            this.idAcademic = idAcademic;
            return this;
        }

        public CourseBuilder setSection(String section) {
            this.section = section;
            return this;
        }

        public CourseBuilder setStartedAt(String startedAt) {
            this.startedAt = startedAt;
            return this;
        }

        public CourseBuilder setEndedAt(String endedAt) {
            this.endedAt = endedAt;
            return this;
        }

        public CourseDTO build() {
            return new CourseDTO(this);
        }
    }
}


