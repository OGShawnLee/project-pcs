package org.example.business.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.business.validation.Validator;
import org.example.db.dao.AcademicDAO;
import org.example.db.dao.AccountDAO;
import org.example.gui.AlertDialog;

import java.sql.SQLException;

public class RegisterAcademicController {
  private final AccountDAO ACCOUNT_DAO = new AccountDAO();
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
  private TextField fieldRole;

  public void handleRegisterAcademic(javafx.event.ActionEvent event) {
    AcademicDTO dataObjectAcademic = null;

    try {
      dataObjectAcademic = new AcademicDTO.AcademicBuilder()
        .setID(
          Validator.getValidStringWithLength(
            fieldIDAcademic.getText(),"ID de Trabajador", 5,5
          )
        )
        .setEmail(
          Validator.getValidEmail(fieldEmail.getText())
        )
        .setName(
          Validator.getValidName(fieldName.getText(), "Nombre", 3,64)
        )
        .setPaternalLastName(
          Validator.getValidName(
            fieldPaternalLastName.getText(),"Apellido Paterno",3,64
          )
        )
        .setMaternalLastName(
          Validator.getValidName(
            fieldMaternalLastName.getText(),"Apellido Materno",3,64
          )
        )
        .setRole(
          Validator.getValidAcademicRoleFromSpanishLabel(fieldRole.getText()).getRole()
        )
        .build();
    } catch (IllegalArgumentException e) {
      AlertDialog.showError(e.getMessage());
    }

    try {
      assert dataObjectAcademic != null;

      AccountDTO existingAccount = ACCOUNT_DAO.getOne(dataObjectAcademic.getEmail());
      if (existingAccount != null) {
        AlertDialog.showError("No ha sido posible registrar el académico debido a que ya esta registrada su cuenta.");
        return;
      }

      AcademicDTO existingAcademic = ACADEMIC_DAO.getOne(dataObjectAcademic.getID());
      if (existingAcademic != null) {
        AlertDialog.showError("No ha sido posible registrar el académico debido a que ya existe un académico con el mismo NRC.");
        return;
      }

      ACCOUNT_DAO.createOne(new AccountDTO(dataObjectAcademic.getEmail(), dataObjectAcademic.getID()));
      ACADEMIC_DAO.createOne(dataObjectAcademic);
    } catch (SQLException e) {
      AlertDialog.showError("No ha sido posible registrar el académico debido a un error de sistema.");
    }
  }
}
