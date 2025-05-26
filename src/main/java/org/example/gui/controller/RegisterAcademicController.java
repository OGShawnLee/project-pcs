package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.business.dao.AcademicDAO;
import org.example.business.dao.AccountDAO;
import org.example.gui.Modal;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class RegisterAcademicController extends Controller {
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
        Modal.displayError("No ha sido posible registrar académico debido a que ya existe una cuenta con ese correo electrónico.");
        return;
      }

      AcademicDTO existingAcademic = ACADEMIC_DAO.getOne(dataObjectAcademic.getID());
      if (existingAcademic != null) {
        Modal.displayError("No ha sido posible registrar académico debido a que ya existe un académico con la misma ID de Trabajador.");
        return;
      }
      String hashedPassword = BCrypt.hashpw(dataObjectAcademic.getID(), BCrypt.gensalt());
      ACCOUNT_DAO.createOne(new AccountDTO(dataObjectAcademic.getEmail(), hashedPassword));
      ACADEMIC_DAO.createOne(dataObjectAcademic);
      Modal.displaySuccess("El académico ha sido registrado exitosamente.");
    } catch (IllegalArgumentException e) {
      Modal.displayError(e.getMessage());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      Modal.displayError("No ha sido posible registrar académico debido a un error en el sistema.");
    }
  }

  public void navigateToAcademicList() {
    ReviewAcademicListController.navigateToAcademicListPage(getScene());
  }
}