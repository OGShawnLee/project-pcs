package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.business.dao.OrganizationDAO;
import org.example.business.dto.OrganizationDTO;
import org.example.gui.AlertDialog;

import java.sql.SQLException;

public class ManageOrganizationController extends ManageController<OrganizationDTO> {
  private final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldEmail;
  @FXML
  private TextField fieldRepresentative;
  @FXML
  private TextField fieldColony;
  @FXML
  private TextField fieldStreet;
  @FXML
  private ComboBox<String> fieldState;

  public void initialize(OrganizationDTO currentOrganization) {
    fieldName.setText(currentOrganization.getName());
    fieldEmail.setText(currentOrganization.getEmail());
    fieldRepresentative.setText(currentOrganization.getRepresentativeFullName());
    fieldColony.setText(currentOrganization.getColony());
    fieldStreet.setText(currentOrganization.getStreet());
    fieldState.setValue(currentOrganization.getState());
    fieldState.getItems().addAll("Activo", "Inactivo");
  }

  @Override
  @FXML
  protected void handleUpdateCurrentDataObject() {
    try {
      OrganizationDTO updatedOrganization = new OrganizationDTO.OrganizationBuilder()
        .setEmail(fieldEmail.getText())
        .setName(fieldName.getText())
        .setRepresentativeFullName(fieldRepresentative.getText())
        .setColony(fieldColony.getText())
        .setStreet(fieldStreet.getText())
        .setState(fieldState.getValue())
        .build();

      ORGANIZATION_DAO.updateOne(updatedOrganization);
      AlertDialog.showSuccess("La organización ha sido actualizada con éxito.");
      navigateToOrganizationList();
    } catch (IllegalArgumentException e) {
      AlertDialog.showError(e.getMessage());
    } catch (SQLException e) {
      AlertDialog.showError("No ha sido posible actualizar la organización debido a un error de sistema.");
    }
  }

  public void navigateToOrganizationList() {
    ReviewOrganizationListController.navigateToOrganizationListPage(getScene());
  }
}
