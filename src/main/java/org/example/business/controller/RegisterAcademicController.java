package org.example.business.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
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
  private ComboBox<String> fieldRole;

  @FXML
  public void initialize() {
    fieldRole.getItems().addAll("Evaluador", "Evaluador y Profesor", "Profesor");
    fieldRole.setValue("Profesor");
  }

  public void handleRegisterAcademic(ActionEvent event) {
    try {
      AcademicDTO dataObjectAcademic = new AcademicDTO.AcademicBuilder()
        .setID(fieldIDAcademic.getText())
        .setEmail(fieldEmail.getText())
        .setName(fieldName.getText())
        .setPaternalLastName(fieldPaternalLastName.getText())
        .setMaternalLastName(fieldMaternalLastName.getText())
        .setRole(fieldRole.getValue())
        .build();

      AccountDTO existingAccount = ACCOUNT_DAO.getOne(dataObjectAcademic.getEmail());
      if (existingAccount != null) {
        AlertDialog.showError("No ha sido posible registrar el académico debido a que ya existe una cuenta con ese correo electrónico.");
        return;
      }

      AcademicDTO existingAcademic = ACADEMIC_DAO.getOne(dataObjectAcademic.getID());
      if (existingAcademic != null) {
        AlertDialog.showError("No ha sido posible registrar el académico debido a que ya existe un académico con la misma ID de Trabajador.");
        return;
      }

      ACCOUNT_DAO.createOne(new AccountDTO(dataObjectAcademic.getEmail(), dataObjectAcademic.getID()));
      ACADEMIC_DAO.createOne(dataObjectAcademic);
      AlertDialog.showSuccess("Académico registrado exitosamente.");
    } catch (IllegalArgumentException e) {
      AlertDialog.showError(e.getMessage());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      AlertDialog.showError("No ha sido posible registrar el académico debido a un error de sistema.");
    }
  }
}