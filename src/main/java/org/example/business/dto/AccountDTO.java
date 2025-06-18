package org.example.business.dto;

import org.example.business.Validator;

import org.mindrot.jbcrypt.BCrypt;

public record AccountDTO(String email, String password, Role role) {
  public enum Role {
    COORDINATOR,
    ACADEMIC,
    ACADEMIC_EVALUATOR,
    EVALUATOR,
    STUDENT;

    public static Role fromAcademicRole(AcademicDTO.Role academicRole) {
      return switch (academicRole) {
        case ACADEMIC -> Role.ACADEMIC;
        case ACADEMIC_EVALUATOR -> Role.ACADEMIC_EVALUATOR;
        case EVALUATOR -> Role.EVALUATOR;
      };
    }
  }

  public AccountDTO(String email, String password, Role role) {
    this.email = Validator.getValidEmail(email);
    this.password = Validator.getValidPassword(password);
    this.role = role;
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    AccountDTO that = (AccountDTO) instance;

    return
      email.equals(that.email) &&
      role == that.role;
  }

  public boolean hasPasswordMatch(String candidate) {
    return BCrypt.checkpw(candidate, this.password);
  }

  public static String getGeneratedHashedPassword(String plain) {
    return BCrypt.hashpw(plain + "@Password", BCrypt.gensalt());
  }
}
