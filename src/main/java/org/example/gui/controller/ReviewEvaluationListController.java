package org.example.gui.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.business.auth.AuthClient;
import org.example.business.dao.*;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.EvaluationDTO;
import org.example.business.dto.EvaluationPreviewDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dto.enumeration.AccountRole;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

public class ReviewEvaluationListController extends ReviewListController {
  private static final EvaluationDAO EVALUATION_DAO = new EvaluationDAO();
  private static final EvaluationPreviewDAO EVALUATION_PREVIEW_DAO = new EvaluationPreviewDAO();
  private static final StudentDAO STUDENT_DAO = new StudentDAO();
  private static final AcademicDAO ACADEMIC_DAO = new AcademicDAO();
  @FXML
  private TableView<EvaluationPreviewDTO> tableEvaluation;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnFullNameAcademic;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnFullNameStudent;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnIDProject;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnCreatedAt;

  public void initialize() {
    loadDataList();
  }

  @Override
  public void loadDataList() {
    AccountRole currentUserRole = AuthClient.getInstance().getCurrentUser().role();
    switch (currentUserRole) {
      case STUDENT:
        loadStudentView();
      case ACADEMIC:
        loadAcademicView();
        break;
      case EVALUATOR:
        loadEvaluatorView();
        break;
      case ACADEMIC_EVALUATOR:
        loadAcademicEvaluatorView();
        break;
    }
  }

  public void loadStudentView() {
    try {
      StudentDTO currentStudentDTO = AuthClient.getInstance().getCurrentStudentDTO();
      tableEvaluation.setItems(FXCollections.observableList(
        EVALUATION_PREVIEW_DAO.getAllByStudent(currentStudentDTO.getID())
      ));
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible visualizar evaluaciones.", e.getMessage());
    }
  }

  public void loadAcademicView() {
    try {
      AcademicDTO currentAcademicDTO = AuthClient.getInstance().getCurrentAcademicDTO();
    } catch (UserDisplayableException | NotFoundException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }

  public void loadEvaluatorView() {
    try {
      AcademicDTO currentAcademicDTO = AuthClient.getInstance().getCurrentAcademicDTO();

    } catch (UserDisplayableException | NotFoundException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }

  public void loadAcademicEvaluatorView() {
    // TODO: FIGURE OUT WHAT TO DO HERE
  }

  @Override
  public void loadTableColumns() {
    columnIDProject.setCellValueFactory(new PropertyValueFactory<>("id_project"));
    columnFullNameAcademic.setCellValueFactory((it) -> new ReadOnlyStringWrapper(it.getValue().getFullNameAcademic()));
    columnFullNameStudent.setCellValueFactory((it) -> new ReadOnlyStringWrapper(it.getValue().getFullNameStudent()));
    columnCreatedAt.setCellValueFactory((it) -> new ReadOnlyStringWrapper(it.getValue().getEvaluationDTO().getFormattedCreatedAt()));
  }

  public void handleOpenRegisterEvaluationModal() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration(
        "Registrar Evaluacion",
        "RegisterEvaluationModal",
        this::loadDataList
      )
    );
  }

  public void handleVisualizeEvaluationModal() {
    EvaluationPreviewDTO selectedPreviewEvaluation = tableEvaluation.getSelectionModel().getSelectedItem();

    if (selectedPreviewEvaluation == null) return;

    ModalFacade.createAndDisplayContextModal(
      new ModalFacadeConfiguration(
        "Visualizar Evaluacion",
        "VisualizeEvaluationModal"
      ),
      selectedPreviewEvaluation
    );
  }

  public static void navigateToEvaluationListPage(Stage currentStage) {
    navigateTo(currentStage, "Lista de Evaluaciones", "ReviewEvaluationListPage");
  }
}