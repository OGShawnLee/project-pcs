package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.example.business.dao.AcademicDAO;
import org.example.business.dao.AccountDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.gui.Modal;

import java.sql.SQLException;

public class ManageAcademicController extends ManageController<AcademicDTO> {
  private final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
  private final AccountDAO ACCOUNT_DAO = new AccountDAO();
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
  private ComboBox<AcademicDTO.Role> fieldRole;
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
    fieldState.setValue(getCurrentDataObject().getState());
  }

  @Override
  public void handleUpdateCurrentDataObject() {
    try {
      System.out.println(getCurrentDataObject().getRole());
      System.out.println(fieldRole.getValue());

      if (getCurrentDataObject().getRole() != fieldRole.getValue()) {
        AccountDTO accountDTO = ACCOUNT_DAO.getOne(getCurrentDataObject().getEmail());
        System.out.println("Updating account role from " + accountDTO.role() + " to " + fieldRole.getValue());
        ACCOUNT_DAO.updateOne(
          new AccountDTO(
            accountDTO.email(),
            accountDTO.password(),
            AccountDTO.Role.fromAcademicRole(fieldRole.getValue())
          )
        );
      }

      AcademicDTO academicDTO = new AcademicDTO.AcademicBuilder()
        .setID(fieldIDAcademic.getText())
        .setEmail(fieldEmail.getText())
        .setName(fieldName.getText())
        .setPaternalLastName(fieldPaternalLastName.getText())
        .setMaternalLastName(fieldMaternalLastName.getText())
        .setRole(fieldRole.getValue())
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