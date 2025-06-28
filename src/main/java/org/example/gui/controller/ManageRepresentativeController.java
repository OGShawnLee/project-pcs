package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dao.OrganizationDAO;
import org.example.business.dao.RepresentativeDAO;
import org.example.business.dto.OrganizationDTO;
import org.example.business.dto.RepresentativeDTO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class ManageRepresentativeController extends ManageController<RepresentativeDTO> {
  private final RepresentativeDAO REPRESENTATIVE_DAO = new RepresentativeDAO();
  private final OrganizationDAO ORGANIZATION_DAO = new OrganizationDAO();
  @FXML
  private TextField fieldEmail;
  @FXML
  private ComboBox<OrganizationDTO> comboBoxOrganization;
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldPaternalLastName;
  @FXML
  private TextField fieldMaternalLastName;
  @FXML
  private TextField fieldPhoneNumber;
  @FXML
  private TextField fieldPosition;
  @FXML
  private ComboBox<String> comboBoxState;

  @Override
  public void initialize(RepresentativeDTO dataObject) {
    super.initialize(dataObject);
    loadDataObjectFields();
  }

  public void loadDataObjectFields() {
    ComboBoxLoader.loadComboBoxState(comboBoxState);
    ComboBoxLoader.loadComboBoxOrganizationWithRepresentatives(comboBoxOrganization);

    loadOrganization();

    fieldEmail.setText(getContext().getEmail());
    fieldName.setText(getContext().getName());
    fieldPaternalLastName.setText(getContext().getPaternalLastName());
    fieldMaternalLastName.setText(getContext().getMaternalLastName());
    fieldPhoneNumber.setText(getContext().getPhoneNumber());
    fieldPosition.setText(getContext().getPosition());
    comboBoxState.setValue(getContext().getState());
  }

  private void loadOrganization() {
    try {
      for (OrganizationDTO organizationDTO : comboBoxOrganization.getItems()) {
        if (organizationDTO.getEmail().equals(getContext().getOrganizationID())) {
          comboBoxOrganization.setValue(organizationDTO);
          break;
        }
      }

      OrganizationDTO organization = ORGANIZATION_DAO.getOne(getContext().getOrganizationID());
      comboBoxOrganization.setValue(organization);
    } catch (SQLException e) {
      Modal.displayError("No ha sido posible cargar la organización del proyecto debido a un error en el sistema.");
    }
  }

  public RepresentativeDTO getDTOFromFields() {
    return new RepresentativeDTO.RepresentativeBuilder()
      .setEmail(fieldEmail.getText())
      .setOrganizationID(comboBoxOrganization.getValue().getEmail())
      .setName(fieldName.getText())
      .setPaternalLastName(fieldPaternalLastName.getText())
      .setMaternalLastName(fieldMaternalLastName.getText())
      .setPhoneNumber(fieldPhoneNumber.getText())
      .setPosition(fieldPosition.getText())
      .setState(comboBoxState.getValue())
      .build();
  }

  public boolean getUpdateConfirmation(RepresentativeDTO representativeDTO) {
    boolean isSameState = representativeDTO.getState().equals(getContext().getState());

    if (isSameState) {
      return true;
    }

    String state = representativeDTO.getState();

    if (state.equals("ACTIVE")) {
      return Modal.displayConfirmation("¿Está seguro de que desea activar al representante? Podrá participar en proyectos.");
    } else {
      return Modal.displayConfirmation("¿Está seguro de que desea retirar al representante? Ya no podrá participar en proyectos hasta que sea reactivado.");
    }
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      RepresentativeDTO representativeDTO = getDTOFromFields();
      if (getUpdateConfirmation(representativeDTO)) {
        REPRESENTATIVE_DAO.updateOne(representativeDTO);
        Modal.displaySuccess("El representante ha sido actualizado exitosamente.");
      }
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      Modal.displayError("No ha sido posible actualizar representante debido a un error en el sistema.");
    }
  }
}