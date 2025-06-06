package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.business.dao.AcademicDAO;
import org.example.business.dto.AcademicDTO;
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
      Modal.displaySuccess("El académico ha sido actualizado exitosamente.");
      navigateToAcademicList();
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible actualizar académico debido a un error en el sistema.");
    }
  }

  @FXML
  private void navigateToAcademicList() {
    ReviewAcademicListController.navigateToAcademicListPage(getScene());
  }
}