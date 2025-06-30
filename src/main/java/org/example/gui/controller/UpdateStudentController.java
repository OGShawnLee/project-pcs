package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.example.business.Validator;
import org.example.business.dao.StudentDAO;
import org.example.business.dao.AccountDAO;
import org.example.business.dto.StudentDTO;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.enumeration.AccountRole;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

// TODO: UPDATE USE CASE
public class UpdateStudentController extends ManageController<StudentDTO> {
  private final AccountDAO ACCOUNT_DAO = new AccountDAO();
  private final StudentDAO STUDENT_DAO = new StudentDAO();
  @FXML
  private TextField fieldIDStudent;
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
  public void initialize(StudentDTO currentUser) {
    super.initialize(currentUser);
    loadDataObjectFields();
  }

  public void loadDataObjectFields() {
    fieldIDStudent.setText(getContext().getID());
    fieldEmail.setText(getContext().getEmail());
    fieldName.setText(getContext().getName());
    fieldPaternalLastName.setText(getContext().getPaternalLastName());
    fieldMaternalLastName.setText(getContext().getMaternalLastName());
    fieldPhoneNumber.setText(getContext().getPhoneNumber());
  }

  private AccountDTO createAccountDTOFromFields() {
    return new AccountDTO(
      getContext().getEmail(),
      fieldPassword.getText(),
      AccountRole.STUDENT,
      true
    );
  }

  private boolean isPasswordChangeRequired() throws IllegalArgumentException {
    String password = fieldPassword.getText();
    String passwordConfirm = fieldPasswordConfirm.getText();

    if (!Validator.isValidString(password) && !Validator.isValidString(passwordConfirm)) {
      return false;
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

    return true;
  }

  private void updateAccountDTO() throws IllegalArgumentException, UserDisplayableException {
    if (isPasswordChangeRequired()) {
      ACCOUNT_DAO.updateOne(createAccountDTOFromFields());
    }
  }

  private StudentDTO createStudentDTOFromFields() {
    return new StudentDTO.StudentBuilder()
      .setID(fieldIDStudent.getText())
      .setEmail(fieldEmail.getText())
      .setName(fieldName.getText())
      .setPaternalLastName(fieldPaternalLastName.getText())
      .setMaternalLastName(fieldMaternalLastName.getText())
      .setPhoneNumber(fieldPhoneNumber.getText())
      .setState(getContext().getState())
      .build();
  }

  private void updateStudentDTO() throws UserDisplayableException {
    STUDENT_DAO.updateOne(createStudentDTOFromFields());
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      updateAccountDTO();
      updateStudentDTO();
      AlertFacade.showSuccessAndWait("Sus datos han sido actualizados exitosamente.");
    } catch (IllegalArgumentException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible actualizar sus datos.", e);
    }
  }
}
