package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.business.dao.AcademicDAO;
import org.example.business.dto.AcademicDTO;
import org.example.gui.AlertDialog;

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
  private ComboBox<String> fieldRole;
  @FXML
  private ComboBox<String> fieldState;

  public void initialize(AcademicDTO currentAcademic) {
    fieldIDAcademic.setText(currentAcademic.getID());
    fieldEmail.setText(currentAcademic.getEmail());
    fieldName.setText(currentAcademic.getName());
    fieldPaternalLastName.setText(currentAcademic.getPaternalLastName());
    fieldMaternalLastName.setText(currentAcademic.getMaternalLastName());
    fieldRole.setValue(currentAcademic.getRole());
    fieldState.setValue(currentAcademic.getState());
    fieldRole.getItems().addAll("Evaluador", "Evaluador y Profesor", "Profesor");
    fieldState.getItems().addAll("Activo", "Inactivo");
    fieldIDAcademic.setEditable(false);
  }

  @Override
  @FXML
  protected void handleUpdateCurrentDataObject() {
    try {
      AcademicDTO updatedAcademic = new AcademicDTO.AcademicBuilder()
        .setID(fieldIDAcademic.getText())
        .setEmail(fieldEmail.getText())
        .setName(fieldName.getText())
        .setPaternalLastName(fieldPaternalLastName.getText())
        .setMaternalLastName(fieldMaternalLastName.getText())
        .setRole(fieldRole.getValue())
        .setState(fieldState.getValue())
        .build();

      ACADEMIC_DAO.updateOne(updatedAcademic);
      AlertDialog.showSuccess("La organización ha sido actualizada con exito.");
      navigateToAcademicList();
    } catch (IllegalArgumentException e) {
      AlertDialog.showError(e.getMessage());
    } catch (SQLException e) {
      AlertDialog.showError("No ha sido posible actualizar la organización debido a un error de sistema.");
    }
  }

  @FXML
  private void navigateToAcademicList() {
    ReviewAcademicListController.navigateToAcademicListPage(getScene());
  }
}