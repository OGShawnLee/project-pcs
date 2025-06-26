package org.example.business.dto;

import org.example.business.Validator;

import org.example.business.dto.enumeration.AccountRole;
import org.mindrot.jbcrypt.BCrypt;

public record AccountDTO(String email, String password, AccountRole role, boolean hasAccess) {
  public AccountDTO(String email, String password, AccountRole role, boolean hasAccess) {
    this.email = Validator.getValidEmail(email);
    this.password = Validator.getValidPassword(password);
    this.role = role;
    this.hasAccess = hasAccess;
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    AccountDTO that = (AccountDTO) instance;

    return
      email.equals(that.email) &&
      hasAccess == that.hasAccess &&
      role == that.role;
  }

  public boolean hasPasswordMatch(String candidate) {
    return BCrypt.checkpw(candidate, this.password);
  }

  public static String getGeneratedHashedPassword(String plain) {
    return BCrypt.hashpw(plain + "@Password", BCrypt.gensalt());
  }
}
