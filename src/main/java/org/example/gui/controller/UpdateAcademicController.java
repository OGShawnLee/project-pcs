package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.example.business.Validator;
import org.example.business.dao.AcademicDAO;
import org.example.business.dao.AccountDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class UpdateAcademicController extends ManageController<AcademicDTO> {
  private final AccountDAO ACCOUNT_DAO = new AccountDAO();
  private final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
  @FXML
  private TextField fieldIDAcademic;
  @FXML
  private TextField fieldEmail;
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldPaternalLastName;
  @FXML
  private TextField fieldMaternalLastName;
  @FXML
  private TextField fieldPhoneNumber;
  @FXML
  private PasswordField fieldPassword;
  @FXML
  private PasswordField fieldPasswordConfirm;

  @Override
  public void initialize(AcademicDTO currentUser) {
    super.initialize(currentUser);
    loadDataObjectFields();
  }

  public void loadDataObjectFields() {
    fieldIDAcademic.setText(getCurrentDataObject().getID());
    fieldEmail.setText(getCurrentDataObject().getEmail());
    fieldName.setText(getCurrentDataObject().getName());
    fieldPaternalLastName.setText(getCurrentDataObject().getPaternalLastName());
    fieldMaternalLastName.setText(getCurrentDataObject().getMaternalLastName());
    fieldPhoneNumber.setText(getCurrentDataObject().getPhoneNumber());
  }

  private void handlePasswordUpdate() throws IllegalArgumentException, SQLException {
    String password = fieldPassword.getText();
    String passwordConfirm = fieldPasswordConfirm.getText();

    if (!Validator.isValidString(password) && !Validator.isValidString(passwordConfirm)) {
      return; // No password change requested
    }

    if (!Validator.isValidString(password)) {
      throw new IllegalArgumentException("Debe ingresar una nueva contraseña.");
    }

    if (!Validator.isValidString(passwordConfirm)) {
      throw new IllegalArgumentException("Debe confirmar la nueva contraseña.");
    }

    if (!password.equals(passwordConfirm)) {
      throw new IllegalArgumentException("Las contraseñas no coinciden.");
    }

    AccountDTO accountDTO = new AccountDTO(
      getCurrentDataObject().getEmail(),
      fieldPassword.getText(),
      AccountDTO.Role.fromAcademicRole(getCurrentDataObject().getRole())
    );
    ACCOUNT_DAO.updateOne(accountDTO);
  }

  private void handleAcademicUpdate() throws SQLException {
    AcademicDTO academicDTO = new AcademicDTO.AcademicBuilder()
      .setID(getCurrentDataObject().getID())
      .setEmail(getCurrentDataObject().getEmail())
      .setName(fieldName.getText())
      .setPaternalLastName(fieldPaternalLastName.getText())
      .setMaternalLastName(fieldMaternalLastName.getText())
      .setPhoneNumber(fieldPhoneNumber.getText())
      .setRole(getCurrentDataObject().getRole())
      .setState(getCurrentDataObject().getState())
      .build();

    ACADEMIC_DAO.updateOne(academicDTO);
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      handlePasswordUpdate();
      handleAcademicUpdate();

      Modal.displaySuccess("Sus datos han sido actualizados exitosamente.");
    } catch (IllegalArgumentException e ){
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible actualizar sus datos debido a un error de sistema.");
    }
  }
}
