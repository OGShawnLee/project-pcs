package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dao.OrganizationDAO;
import org.example.business.dto.OrganizationDTO;
import org.example.gui.Modal;

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
  private TextField fieldAddress;
  @FXML
  private ComboBox<String> fieldState;
  @FXML
  private TextField fieldPhoneNumber;

  @Override
  public void initialize(OrganizationDTO currentOrganization) {
    super.initialize(currentOrganization);
    loadRecordState(fieldState);
    loadDataObjectFields();
  }

  public void loadDataObjectFields() {
    fieldName.setText(getCurrentDataObject().getName());
    fieldEmail.setText(getCurrentDataObject().getEmail());
    fieldRepresentative.setText(getCurrentDataObject().getRepresentativeFullName());
    fieldAddress.setText(getCurrentDataObject().getAddress());
    fieldPhoneNumber.setText(getCurrentDataObject().getPhoneNumber());
    fieldState.setValue(getCurrentDataObject().getState());
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      OrganizationDTO updatedOrganization = new OrganizationDTO.OrganizationBuilder()
        .setEmail(fieldEmail.getText())
        .setName(fieldName.getText())
        .setRepresentativeFullName(fieldRepresentative.getText())
        .setAddress(fieldAddress.getText())
        .setPhoneNumber(fieldPhoneNumber.getText())
        .setState(fieldState.getValue())
        .build();

      ORGANIZATION_DAO.updateOne(updatedOrganization);
      Modal.displaySuccess("La organización ha sido actualizada con éxito.");
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible actualizar organización debido a un error en el sistema.");
    }
  }
}
