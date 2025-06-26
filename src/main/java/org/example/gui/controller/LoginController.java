package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.example.business.Validator;
import org.example.business.auth.AuthClient;
import org.example.business.dao.AccountDAO;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.enumeration.AccountRole;
import org.example.gui.Modal;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class LoginController extends Controller {
  private static final AccountDAO ACCOUNT_DAO = new AccountDAO();
  @FXML
  private TextField emailField;
  @FXML
  private PasswordField passwordField;

  public void initialize() {
    try {
      if (ACCOUNT_DAO.hasCoordinatorAccount()) return;

      Modal.displayInformation(
        "Bienvenido a su Sistema Gestor de Practicas Profesionales. Cree una Cuenta de Coordinador para comenzar."
      );
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible iniciar el sistema, intente más tarde.");
    }
  }

  private void handleAccountAccess(AccountDTO accountDTO) {
    if (accountDTO.hasAccess()) {
      AuthClient.getInstance().setCurrentUser(accountDTO);
      navigateToLandingPage();
      return;
    }

    AccountRole role = accountDTO.role();
    String message;

    switch (role) {
      case STUDENT ->
        message = "No tiene acceso al sistema. Por favor, contacte a su Académico encargado.";
      case ACADEMIC, ACADEMIC_EVALUATOR, EVALUATOR ->
        message = "No tiene acceso al sistema. Por favor, contacte al Coordinador de Prácticas Profesionales.";
      default ->
        message = "No tiene acceso al sistema. Por favor, contacte al Administrador del Sistema.";
    }

    Modal.displayError(message);
  }

  private void handleCreateCoordinatorAccount() throws SQLException {
    String email = Validator.getValidEmail(emailField.getText());
    String password = Validator.getValidPassword(passwordField.getText());

    ACCOUNT_DAO.createOne(
      new AccountDTO(email, BCrypt.hashpw(password, BCrypt.gensalt()), AccountRole.COORDINATOR, true)
    );

    emailField.clear();
    passwordField.clear();

    Modal.displaySuccess(
      "¡Cuenta de Coordinador creada! Bienvenido, " + email + " a su Sistema Gestor de Practicas Profesionales."
    );
    Modal.displayInformation(
      "Por favor utilice las credenciales de su Cuenta recién creada para iniciar sesión."
    );
  }

  private void handleLoginAccount() throws SQLException {
    String email = Validator.getValidEmail(emailField.getText());
    String password = Validator.getValidText(passwordField.getText(), "Contraseña");
    AccountDTO accountDTO = ACCOUNT_DAO.getOne(email);

    if (accountDTO == null) {
      Modal.displayError("No existe una cuenta con este correo electrónico.");
      return;
    }

    if (accountDTO.hasPasswordMatch(password)) {
      handleAccountAccess(accountDTO);
    } else {
      Modal.displayError("Las credenciales son inválidas. Por favor intente de nuevo.");
    }
  }

  public void handleLogin() {
    try {
      if (ACCOUNT_DAO.hasCoordinatorAccount()) {
        handleLoginAccount();
      } else {
        handleCreateCoordinatorAccount();
      }
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible iniciar sesión debido a un error en la base de datos.");
    }
  }
}
