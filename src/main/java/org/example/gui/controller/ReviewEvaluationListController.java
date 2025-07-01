package org.example.gui.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.business.auth.AuthClient;
import org.example.business.dao.EvaluationPreviewDAO;
import org.example.business.dao.NotFoundException;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.AccountDTO;
import org.example.business.dto.EvaluationPreviewDTO;
import org.example.business.dto.StudentDTO;
import org.example.business.dto.enumeration.AccountRole;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

import java.util.List;
import java.util.Optional;

public class ReviewEvaluationListController extends Controller {
  private static final Logger LOGGER = LogManager.getLogger(ReviewEvaluationListController.class);
  private static final EvaluationPreviewDAO EVALUATION_PREVIEW_DAO = new EvaluationPreviewDAO();
  @FXML
  private TableView<EvaluationPreviewDTO> tableEvaluation;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnKind;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnFullNameAcademic;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnFullNameStudent;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnAdequateUseGrade;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnContentCongruenceGrade;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnWritingGrade;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnMethodologicalRigorGrade;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnAverage;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnCreatedAt;
  @FXML
  private Button buttonRegisterEvaluation;

  public void initialize() {
    try {
      loadTableItemsAndView();
      loadCommonTableColumns();
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible cargar evaluaciones.", e.getMessage());
    }
  }

  public void setButtonRegisterEvaluationVisibility(boolean visibility) {
    buttonRegisterEvaluation.setVisible(visibility);
  }

  public void loadTableItemsAndView() throws NotFoundException, UserDisplayableException {
    switch (AuthClient.getInstance().getCurrentUser().role()) {
      case EVALUATOR -> loadEvaluatorItemsAndView();
      case STUDENT -> loadStudentItemsAndView();
      case COORDINATOR -> loadCoordinatorItemsAndView();
    }
  }

  public void setTableItems(List<EvaluationPreviewDTO> evaluationPreviewDTOList) {
    tableEvaluation.setItems(FXCollections.observableArrayList(evaluationPreviewDTOList));
  }

  public void loadEvaluatorColumns() {
    columnFullNameAcademic.setVisible(false);
    columnFullNameStudent.setVisible(true);
    columnFullNameStudent.setCellValueFactory((it) ->
      new ReadOnlyStringWrapper(it.getValue().fullNameStudent())
    );
  }

  public void loadEvaluatorItemsAndView() throws NotFoundException, UserDisplayableException {
    AcademicDTO currentAcademicDTO = AuthClient.getInstance().getCurrentAcademicDTO();
    setTableItems(EVALUATION_PREVIEW_DAO.getAllByEvaluator(currentAcademicDTO.getID()));
    loadEvaluatorColumns();
    setButtonRegisterEvaluationVisibility(true);
  }

  public void loadStudentColumns() {
    columnFullNameStudent.setVisible(false);
    columnFullNameAcademic.setVisible(true);
    columnFullNameAcademic.setCellValueFactory((it) ->
      new ReadOnlyStringWrapper(it.getValue().fullNameAcademic())
    );
  }

  public void loadStudentItemsAndView() throws NotFoundException, UserDisplayableException {
    StudentDTO currentStudentDTO = AuthClient.getInstance().getCurrentStudentDTO();
    setTableItems(EVALUATION_PREVIEW_DAO.getAllByStudent(currentStudentDTO.getID()));
    loadStudentColumns();
    setButtonRegisterEvaluationVisibility(false);
  }

  public void loadCoordinatorItemsAndView() {
    LOGGER.fatal("El rol de Coordinador no puede ver evaluaciones directamente.");
    AlertFacade.showErrorAndWait("Acción Prohibida", "El rol de Coordinador no puede ver evaluaciones directamente.");
  }

  public void loadCommonTableColumns() {
    columnKind.setCellValueFactory((it) ->
      new ReadOnlyStringWrapper(it.getValue().evaluationDTO().getKind().toString())
    );
    columnAdequateUseGrade.setCellValueFactory((it) ->
      new ReadOnlyStringWrapper(String.valueOf(it.getValue().evaluationDTO().getAdequateUseGrade()))
    );
    columnContentCongruenceGrade.setCellValueFactory((it) ->
      new ReadOnlyStringWrapper(String.valueOf(it.getValue().evaluationDTO().getContentCongruenceGrade()))
    );
    columnWritingGrade.setCellValueFactory((it) ->
      new ReadOnlyStringWrapper(String.valueOf(it.getValue().evaluationDTO().getWritingGrade()))
    );
    columnMethodologicalRigorGrade.setCellValueFactory((it) ->
      new ReadOnlyStringWrapper(String.valueOf(it.getValue().evaluationDTO().getMethodologicalRigorGrade()))
    );
    columnAverage.setCellValueFactory((it) -> {
      float average = it.getValue().evaluationDTO().getAverageGrade();
      return new ReadOnlyStringWrapper(String.format("%.2f", average));
    });
    columnCreatedAt.setCellValueFactory((it) -> new ReadOnlyStringWrapper(it.getValue().evaluationDTO().getFormattedCreatedAt()));
  }

  public void refreshDataList() {
    try {
      loadTableItemsAndView();
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible recargar las evaluaciones.", e.getMessage());
    }
  }

  public void onRegisterEvaluation() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration(
        "Registrar Evaluacion",
        "RegisterEvaluationModal",
        this::refreshDataList
      )
    );
  }

  private Optional<EvaluationPreviewDTO> getSelectedItem() {
    return Optional.ofNullable(tableEvaluation.getSelectionModel().getSelectedItem());
  }

  public void onViewEvaluation() {
    getSelectedItem().ifPresent(evaluationPreview -> {
      ModalFacade.createAndDisplayContextModal(
        new ModalFacadeConfiguration("Ver Evaluación", "ReviewEvaluationModal"),
        evaluationPreview
      );
    });
  }

  public static void navigateToEvaluationListPage(Stage currentStage) {
    navigateTo(currentStage, "Lista de Evaluaciones", "ReviewEvaluationListPage");
  }
}