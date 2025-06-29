package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.example.business.Validator;
import org.example.business.dao.AcademicDAO;
import org.example.business.dao.AccountDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.enumeration.AccountRole;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

// TODO: UPDATE USE CASE
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
    fieldIDAcademic.setText(getContext().getID());
    fieldEmail.setText(getContext().getEmail());
    fieldName.setText(getContext().getName());
    fieldPaternalLastName.setText(getContext().getPaternalLastName());
    fieldMaternalLastName.setText(getContext().getMaternalLastName());
    fieldPhoneNumber.setText(getContext().getPhoneNumber());
  }

  private void handlePasswordUpdate() throws IllegalArgumentException, UserDisplayableException {
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
      getContext().getEmail(),
      fieldPassword.getText(),
      AccountRole.fromAcademicRole(getContext().getRole()),
      true
    );
    ACCOUNT_DAO.updateOne(accountDTO);
  }

  private void handleAcademicUpdate() throws UserDisplayableException {
    AcademicDTO academicDTO = new AcademicDTO.AcademicBuilder()
      .setID(getContext().getID())
      .setEmail(getContext().getEmail())
      .setName(fieldName.getText())
      .setPaternalLastName(fieldPaternalLastName.getText())
      .setMaternalLastName(fieldMaternalLastName.getText())
      .setPhoneNumber(fieldPhoneNumber.getText())
      .setRole(getContext().getRole())
      .setState(getContext().getState())
      .build();

    ACADEMIC_DAO.updateOne(academicDTO);
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      handlePasswordUpdate();
      handleAcademicUpdate();

      AlertFacade.showSuccessAndWait("Sus datos han sido actualizados exitosamente.");
    } catch (IllegalArgumentException e ){
      AlertFacade.showErrorAndWait(e.getMessage());
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible actualizar sus datos.", e);
    }
  }
}
