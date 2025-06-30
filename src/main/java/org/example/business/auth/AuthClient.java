package org.example.business.auth;

import org.example.business.dao.AcademicDAO;
import org.example.business.dao.NotFoundException;
import org.example.business.dao.StudentDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dto.enumeration.AccountRole;
import org.example.common.UserDisplayableException;

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

  public AcademicDTO getCurrentAcademicDTO() throws IllegalStateException, NotFoundException, UserDisplayableException {
    AccountDTO currentUser = getCurrentUser();

    if (currentUser == null) {
      throw new IllegalStateException("No existe un usuario que haya iniciado sesión en el sistema.");
    }

    return switch (currentUser.role()) {
      case AccountRole.ACADEMIC, AccountRole.ACADEMIC_EVALUATOR, EVALUATOR ->
        new AcademicDAO().getOneByEmail(currentUser.email());
      default -> throw new IllegalStateException("El usuario actual no es un académico.");
    };
  }

  public StudentDTO getCurrentStudentDTO() throws IllegalStateException, NotFoundException, UserDisplayableException {
    AccountDTO currentUser = getCurrentUser();

    if (currentUser == null) {
      throw new IllegalStateException("No existe un usuario que haya iniciado sesión en el sistema.");
    }

    if (currentUser.role() != AccountRole.STUDENT) {
      throw new IllegalStateException("El usuario actual no es un estudiante.");
    }

    return new StudentDAO().getOneByEmail(currentUser.email());
  }
}
