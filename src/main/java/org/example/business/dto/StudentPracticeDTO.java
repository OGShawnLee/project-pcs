package org.example.business.dto;

public class StudentPracticeDTO {
  private final StudentDTO dataObjectStudent;
  private final PracticeDTO dataObjectPractice;

  public StudentPracticeDTO(StudentDTO dataObjectStudent, PracticeDTO dataObjectPractice) {
    this.dataObjectStudent = dataObjectStudent;
    this.dataObjectPractice = dataObjectPractice;
  }

  public StudentDTO getStudentDTO() {
    return dataObjectStudent;
  }

  public PracticeDTO getPracticeDTO() {
    return dataObjectPractice;
  }
}
