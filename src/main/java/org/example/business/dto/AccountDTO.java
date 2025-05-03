package org.example.business.dto;

import org.example.business.Validator;

public record AccountDTO(String email, String password) {
  public AccountDTO(String email, String password) {
    this.email = Validator.getValidEmail(email);
    this.password = Validator.getValidText(password, "Contraseña");
  }
}
