package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.example.business.Validator;
import org.example.business.dao.AccountDAO;
import org.example.business.dto.AccountDTO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class UpdateCoordinatorController extends ManageController<AccountDTO> {
  private final AccountDAO ACCOUNT_DAO = new AccountDAO();
  @FXML
  private TextField fieldEmail;
  @FXML
  private PasswordField fieldPassword;
  @FXML
  private PasswordField fieldPasswordConfirm;

  @Override
  public void initialize(AccountDTO currentUser) {
    super.initialize(currentUser);
    loadDataObjectFields();
  }

  public void loadDataObjectFields() {
    fieldEmail.setText(getCurrentDataObject().email());
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      String password = Validator.getValidPassword(fieldPassword.getText());
      String passwordConfirm = Validator.getValidPassword(fieldPasswordConfirm.getText());

      if (!password.equals(passwordConfirm)) {
        throw new IllegalArgumentException("Las contraseñas no coinciden.");
      }

      AccountDTO accountDTO = new AccountDTO(
        getCurrentDataObject().email(),
        passwordConfirm,
        getCurrentDataObject().role()
      );
      ACCOUNT_DAO.updateOne(accountDTO);

      Modal.displaySuccess("Sus datos han sido actualizados exitosamente.");
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible actualizar sus datos debido a un error de sistema.");
    }
  }
}
