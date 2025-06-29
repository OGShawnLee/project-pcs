package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.example.business.auth.AuthClient;
import org.example.business.dao.AcademicDAO;
import org.example.business.dao.StudentDAO;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.StudentDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.Modal;

public abstract class LandingController extends Controller {
  @FXML
  private Label labelEmail;

  public void initialize() {
    labelEmail.setText(AuthClient.getInstance().getCurrentUser().email());
  }

  public void handleOpenUpdateProfile() {
    AccountDTO currentUserAccount = AuthClient.getInstance().getCurrentUser();

    try {
      switch (currentUserAccount.role()) {
        case COORDINATOR:
          Modal.displayContextModal("Actualizar Perfil", "UpdateCoordinatorModal", currentUserAccount);
          break;
        case ACADEMIC, ACADEMIC_EVALUATOR, EVALUATOR: {
          AcademicDTO currentStaffDTO = new AcademicDAO().getOneByEmail(currentUserAccount.email());
          Modal.displayContextModal("Actualizar Perfil", "UpdateAcademicModal", currentStaffDTO);
          break;
        }
        case STUDENT: {
          StudentDTO currentStudentDTO = new StudentDAO().getOneByEmail(currentUserAccount.email());
          Modal.displayContextModal("Actualizar Perfil", "UpdateStudentModal", currentStudentDTO);
          break;
        }
      }
    } catch (UserDisplayableException e) {
      Modal.displayError("No ha sido posible actualizar perfil", e);
    }
  }

  public void handleLogOut() {
    AuthClient.getInstance().setCurrentUser(null);
    navigateFromThisPageTo("Página de Inicio", "LoginPage");
  }
}
