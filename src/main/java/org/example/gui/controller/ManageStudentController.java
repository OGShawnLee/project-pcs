package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dao.StudentDAO;
import org.example.business.dto.StudentDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;

public class ManageStudentController extends ManageController<StudentDTO> {
  private final StudentDAO STUDENT_DAO = new StudentDAO();
  @FXML
  private TextField fieldPaternalLastName;
  @FXML
  private TextField fieldMaternalLastName;
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldIDStudent;
  @FXML
  private TextField fieldEmail;
  @FXML
  private TextField fieldPhoneNumber;
  @FXML
  private ComboBox<String> comboBoxState;

  @Override
  public void initialize(StudentDTO dataObject) {
    super.initialize(dataObject);
    ComboBoxLoader.loadComboBoxState(comboBoxState);
    loadDataObjectFields();
  }

  public void loadDataObjectFields() {
    fieldIDStudent.setText(getContext().getID());
    fieldEmail.setText(getContext().getEmail());
    fieldName.setText(getContext().getName());
    fieldPaternalLastName.setText(getContext().getPaternalLastName());
    fieldMaternalLastName.setText(getContext().getMaternalLastName());
    fieldPhoneNumber.setText(getContext().getPhoneNumber());
    comboBoxState.setValue(getContext().getState());
  }

  private StudentDTO createStudentDTOFromFields() {
    return new StudentDTO.StudentBuilder()
      .setID(fieldIDStudent.getText())
      .setEmail(fieldEmail.getText())
      .setName(fieldName.getText())
      .setPaternalLastName(fieldPaternalLastName.getText())
      .setMaternalLastName(fieldMaternalLastName.getText())
      .setPhoneNumber(fieldPhoneNumber.getText())
      .setState(comboBoxState.getValue())
      .build();
  }

  private void updateStudentDTO() throws UserDisplayableException {
    STUDENT_DAO.updateOne(createStudentDTOFromFields());
    AlertFacade.showSuccessAndWait("El estudiante ha sido actualizado exitosamente.");
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      updateStudentDTO();
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible actualizar estudiante.", e.getMessage());
    }
  }
}