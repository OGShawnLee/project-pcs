package org.example.business.dto;

import org.example.business.Validator;
import org.mindrot.jbcrypt.BCrypt;

public record AccountDTO(String email, String password) {
  public AccountDTO(String email, String password) {
    this.email = Validator.getValidEmail(email);
    this.password = Validator.getValidText(password , "Contraseña");
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    AccountDTO that = (AccountDTO) instance;

    return email.equals(that.email) && password.equals(that.password);
  }
}
