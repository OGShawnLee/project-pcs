package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dao.AcademicDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.enumeration.AcademicRole;
import org.example.gui.Modal;

import java.sql.SQLException;

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
  private ComboBox<AcademicRole> fieldRole;
  @FXML
  private ComboBox<String> fieldState;

  @Override
  public void initialize(AcademicDTO dataObject) {
    super.initialize(dataObject);
    RegisterAcademicController.loadRoleComboBox(fieldRole);
    loadRecordState(fieldState);
    loadDataObjectFields();
  }

  public void loadDataObjectFields() {
    fieldIDAcademic.setText(getCurrentDataObject().getID());
    fieldEmail.setText(getCurrentDataObject().getEmail());
    fieldName.setText(getCurrentDataObject().getName());
    fieldPaternalLastName.setText(getCurrentDataObject().getPaternalLastName());
    fieldMaternalLastName.setText(getCurrentDataObject().getMaternalLastName());
    fieldRole.setValue(getCurrentDataObject().getRole());
    fieldPhoneNumber.setText(getCurrentDataObject().getPhoneNumber());
    fieldState.setValue(getCurrentDataObject().getState());
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      AcademicDTO academicDTO = new AcademicDTO.AcademicBuilder()
        .setID(fieldIDAcademic.getText())
        .setEmail(fieldEmail.getText())
        .setName(fieldName.getText())
        .setPaternalLastName(fieldPaternalLastName.getText())
        .setMaternalLastName(fieldMaternalLastName.getText())
        .setRole(fieldRole.getValue())
        .setPhoneNumber(fieldPhoneNumber.getText())
        .setState(fieldState.getValue())
        .build();

      ACADEMIC_DAO.updateOne(academicDTO);
      Modal.displaySuccess("El académico ha sido actualizado exitosamente.");
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible actualizar académico debido a un error en el sistema.");
    }
  }
}