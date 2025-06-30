package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.example.business.Validator;
import org.example.business.auth.AuthClient;
import org.example.business.dao.AccountDAO;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.enumeration.AccountRole;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

import org.mindrot.jbcrypt.BCrypt;

public class LoginController extends Controller {
  private static final AccountDAO ACCOUNT_DAO = new AccountDAO();
  @FXML
  private TextField emailField;
  @FXML
  private PasswordField passwordField;

  public void initialize() {
    try {
      boolean isSystemInitialized = ACCOUNT_DAO.hasCoordinatorAccount();
      if (isSystemInitialized) return;
      AlertFacade.showInformationAndWait("Bienvenido a su Sistema Gestor de Practicas Profesionales. Cree su cuenta para comenzar.");
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible iniciar el sistema.", e);
    }
  }

  private void onAccountHasAccess(AccountDTO accountDTO) {
    AuthClient.getInstance().setCurrentUser(accountDTO);
    navigateToLandingPage();
  }

  private void onRevokedAccess(AccountDTO accountDTO) {
    String message;

    switch (accountDTO.role()) {
      case STUDENT -> message = "No tiene acceso al sistema. Por favor, contacte a su Académico encargado.";
      case ACADEMIC, ACADEMIC_EVALUATOR, EVALUATOR ->
        message = "No tiene acceso al sistema. Por favor, contacte al Coordinador de Prácticas Profesionales.";
      default -> message = "No tiene acceso al sistema. Por favor, contacte al Administrador del Sistema.";
    }

    AlertFacade.showErrorAndWait(message);
  }

  private void handleAccountAccess(AccountDTO accountDTO) {
    if (accountDTO.hasAccess()) {
      onAccountHasAccess(accountDTO);
    } else {
      onRevokedAccess(accountDTO);
    }
  }

  private AccountDTO createAccountDTOFromFields() {
    String email = Validator.getValidEmail(emailField.getText());
    String password = Validator.getValidPassword(passwordField.getText());
    return new AccountDTO(email, BCrypt.hashpw(password, BCrypt.gensalt()), AccountRole.COORDINATOR, true);
  }

  private void handleCreateCoordinatorAccount() throws UserDisplayableException {
    AccountDTO accountDTO = createAccountDTOFromFields();

    ACCOUNT_DAO.createOne(accountDTO);

    emailField.clear();
    passwordField.clear();

    AlertFacade.showSuccessAndWait(
      "¡Cuenta de Coordinador creada! Bienvenido, " + accountDTO.email() + " a su Sistema Gestor de Practicas Profesionales."
    );
    AlertFacade.showInformationAndWait(
      "Por favor utilice las credenciales de su Cuenta recién creada para iniciar sesión."
    );
  }

  private void handleLoginAccount() throws UserDisplayableException {
    String email = Validator.getValidEmail(emailField.getText());
    String password = Validator.getValidText(passwordField.getText(), "Contraseña");
    AccountDTO accountDTO = ACCOUNT_DAO.getOne(email);

    if (accountDTO == null) {
      AlertFacade.showErrorAndWait("No existe una cuenta con este correo electrónico.");
      return;
    }

    if (accountDTO.hasPasswordMatch(password)) {
      handleAccountAccess(accountDTO);
    } else {
      AlertFacade.showErrorAndWait("Las credenciales son inválidas. Por favor intente de nuevo.");
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
      AlertFacade.showErrorAndWait(e.getMessage());
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible iniciar sesión.", e);
    }
  }
}
