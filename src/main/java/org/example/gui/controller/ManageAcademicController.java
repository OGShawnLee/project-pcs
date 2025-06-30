package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dao.AcademicDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.enumeration.AcademicRole;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.combobox.AcademicComboBoxLoader;

public class ManageAcademicController extends ManageController<AcademicDTO> {
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
  private ComboBox<AcademicRole> comboBoxRole;
  @FXML
  private ComboBox<String> comboBoxState;

  @Override
  public void initialize(AcademicDTO dataObject) {
    super.initialize(dataObject);
    AcademicComboBoxLoader.loadAcademicRoleComboBox(comboBoxRole);
    ComboBoxLoader.loadComboBoxState(comboBoxState);
    loadDataObjectFields();
  }

  public void loadDataObjectFields() {
    fieldIDAcademic.setText(getContext().getID());
    fieldEmail.setText(getContext().getEmail());
    fieldName.setText(getContext().getName());
    fieldPaternalLastName.setText(getContext().getPaternalLastName());
    fieldMaternalLastName.setText(getContext().getMaternalLastName());
    comboBoxRole.setValue(getContext().getRole());
    fieldPhoneNumber.setText(getContext().getPhoneNumber());
    comboBoxState.setValue(getContext().getState());
  }

  private AcademicDTO createAcademicDTOFromFields() {
    return new AcademicDTO.AcademicBuilder()
      .setID(fieldIDAcademic.getText())
      .setEmail(fieldEmail.getText())
      .setName(fieldName.getText())
      .setPaternalLastName(fieldPaternalLastName.getText())
      .setMaternalLastName(fieldMaternalLastName.getText())
      .setPhoneNumber(fieldPhoneNumber.getText())
      .setRole(comboBoxRole.getValue())
      .setState(comboBoxState.getValue())
      .build();
  }

  private void updateAcademicDTO() throws UserDisplayableException {
    ACADEMIC_DAO.updateOne(createAcademicDTOFromFields());
    AlertFacade.showSuccessAndWait("El académico ha sido actualizado exitosamente.");
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      updateAcademicDTO();
    } catch (IllegalArgumentException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible actualizar académico", e.getMessage());
    }
  }
}