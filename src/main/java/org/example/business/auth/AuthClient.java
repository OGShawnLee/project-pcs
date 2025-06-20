package org.example.business.auth;

import org.example.business.dao.AcademicDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;

import java.sql.SQLException;

public class AuthClient {
  private AccountDTO currentUser;
  private static AuthClient instance;

  private AuthClient() {
    this.currentUser = null;
  }

  public static AuthClient getInstance() {
    if (instance == null) {
      instance = new AuthClient();
    }

    return instance;
  }

  public AccountDTO getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(AccountDTO currentUser) {
    this.currentUser = currentUser;
  }

  public AcademicDTO getCurrentAcademicDTO() throws IllegalStateException, SQLException {
    AccountDTO currentUser = getCurrentUser();

    if (currentUser == null) {
      throw new IllegalStateException("No existe un usuario que haya iniciado sesión en el sistema.");
    }

    return switch (currentUser.role()) {
      case AccountDTO.Role.ACADEMIC, AccountDTO.Role.ACADEMIC_EVALUATOR, EVALUATOR ->
        new AcademicDAO().getOneByEmail(currentUser.email());
      default -> throw new IllegalStateException("El usuario actual no es un académico.");
    };
  }
}
