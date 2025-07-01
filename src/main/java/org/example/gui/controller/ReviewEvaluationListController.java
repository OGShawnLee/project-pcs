package org.example.gui.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.example.business.dao.NotFoundException;
import org.example.business.dto.CourseDTO;
import org.example.business.dto.EvaluationPreviewDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.controller.helpers.ReviewEvaluationListContext;
import org.example.gui.modal.ModalFacade;
import org.example.gui.modal.ModalFacadeConfiguration;

import java.util.List;
import java.util.Optional;

public class ReviewEvaluationListController extends Controller {
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
  private HBox containerComboBoxCourse;
  @FXML
  private ComboBox<CourseDTO> comboBoxCourse;
  @FXML
  private Button buttonRegisterEvaluation;
  @FXML
  public Button buttonViewAcademicEvaluatorViewEvaluationList;
  private ReviewEvaluationListContext stateContext;

  public void initialize() {
    try {
      this.stateContext = new ReviewEvaluationListContext(this);
      this.stateContext.loadTableItemsAndView();
      loadCommonTableColumns();
    } catch (NotFoundException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible cargar evaluaciones.", e.getMessage());
    }
  }

  public void setColumnFullNameAcademicVisibilityAndConfigure(boolean visibility) {
    columnFullNameAcademic.setVisible(visibility);
    if (visibility) {
      columnFullNameAcademic.setCellValueFactory((it) ->
        new ReadOnlyStringWrapper(it.getValue().fullNameAcademic())
      );
    }
  }

  public void setColumnFullNameStudentVisibilityAndConfigure(boolean visibility) {
    columnFullNameStudent.setVisible(visibility);
    if (visibility) {
      columnFullNameStudent.setCellValueFactory((it) ->
        new ReadOnlyStringWrapper(it.getValue().fullNameStudent())
      );
    }
  }

  public ComboBox<CourseDTO> getComboBoxCourse() {
    return comboBoxCourse;
  }

  public Button getButtonViewAcademicEvaluatorViewEvaluationList() {
    return buttonViewAcademicEvaluatorViewEvaluationList;
  }

  public void setButtonRegisterEvaluationVisibility(boolean visibility) {
    buttonRegisterEvaluation.setVisible(visibility);
  }

  public void setButtonViewAcademicEvaluatorEvaluationListVisibility(boolean visibility) {
    buttonViewAcademicEvaluatorViewEvaluationList.setVisible(visibility);
  }

  public void setContainerComboboxVisibility(boolean visibility) {
    containerComboBoxCourse.setVisible(visibility);
  }

  public void setTableItems(List<EvaluationPreviewDTO> evaluationPreviewDTOList) {
    tableEvaluation.setItems(FXCollections.observableArrayList(evaluationPreviewDTOList));
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
      if (this.stateContext != null) {
        this.stateContext.loadTableItems();
        return;
      }

      AlertFacade.showErrorAndWait("Recarga Exitosa", "Las evaluaciones han sido recargadas correctamente.");
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