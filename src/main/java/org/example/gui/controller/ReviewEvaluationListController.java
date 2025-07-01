package org.example.gui.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import org.example.business.auth.AuthClient;
import org.example.business.dao.EvaluationPreviewDAO;
import org.example.business.dao.NotFoundException;
import org.example.business.dto.AcademicDTO;
import org.example.business.dto.EvaluationPreviewDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

import java.util.Optional;

public class ReviewEvaluationListController extends Controller {
  private static final EvaluationPreviewDAO EVALUATION_PREVIEW_DAO = new EvaluationPreviewDAO();
  @FXML
  private TableView<EvaluationPreviewDTO> tableEvaluation;
  @FXML
  private TableColumn<EvaluationPreviewDTO, String> columnKind;
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

  public void initialize() {
    try {
      loadDataList();
      loadTableColumns();
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible cargar evaluaciones.", e.getMessage());
    }
  }

  public void loadDataList() throws NotFoundException, UserDisplayableException {
    AcademicDTO currentAcademicDTO = AuthClient.getInstance().getCurrentAcademicDTO();
    tableEvaluation.setItems(
      FXCollections.observableArrayList(
        EVALUATION_PREVIEW_DAO.getAllByEvaluator(currentAcademicDTO.getID())
      )
    );
  }

  public void loadTableColumns() {
    columnKind.setCellValueFactory((it) ->
      new ReadOnlyStringWrapper(it.getValue().evaluationDTO().getKind().toString())
    );
    columnFullNameStudent.setCellValueFactory((it) ->
      new ReadOnlyStringWrapper(it.getValue().fullNameStudent())
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
      loadDataList();
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
        new ModalFacadeConfiguration(
          "Visualizar Evaluacion",
          "VisualizeEvaluationModal"
        ),
        evaluationPreview
      );
    });
  }

  public static void navigateToEvaluationListPage(Stage currentStage) {
    navigateTo(currentStage, "Lista de Evaluaciones", "ReviewEvaluationListPage");
  }
}