package org.example.gui.controller.helpers;

import org.example.business.auth.AuthClient;
import org.example.business.dao.NotFoundException;
import org.example.business.dto.AcademicDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.combobox.CourseComboBoxLoader;


public class ReviewEvaluationListAcademicEvaluatorState extends ReviewEvaluationListState {
  private enum AcademicEvaluatorViewMode {
    ACADEMIC, EVALUATOR,
  }

  private final AcademicDTO currentAcademicDTO;
  private AcademicEvaluatorViewMode currentViewMode;

  public ReviewEvaluationListAcademicEvaluatorState(ReviewEvaluationListContext context) throws NotFoundException, UserDisplayableException {
    super(context);
    this.currentAcademicDTO = AuthClient.getInstance().getCurrentAcademicDTO();
    this.currentViewMode = AcademicEvaluatorViewMode.ACADEMIC; // Default view is Academic
    loadComboBox();
  }

  public AcademicDTO getCurrentAcademicDTO() {
    return currentAcademicDTO;
  }

  private AcademicEvaluatorViewMode getCurrentViewMode() {
    return currentViewMode;
  }

  private void setCurrentViewMode(AcademicEvaluatorViewMode viewMode) {
    this.currentViewMode = viewMode;
  }

  private boolean isAcademicView() {
    return getCurrentViewMode() == AcademicEvaluatorViewMode.ACADEMIC;
  }

  private boolean isEvaluatorView() {
    return getCurrentViewMode() == AcademicEvaluatorViewMode.EVALUATOR;
  }

  @Override
  protected void loadTableItemsAndView() throws NotFoundException, UserDisplayableException {
    loadView();
    loadTableItems();
    setOnViewModeChange();
  }

  /**
   * Load the combobox with courses based on the current academic.
   * <p>
   * This method is called during the initial loading of the view to ensure that the combobox
   * is populated with the relevant courses for the current academic only once.
   */
  protected void loadComboBox() throws NotFoundException, UserDisplayableException {
    CourseComboBoxLoader.loadByCurrentAcademicDTO(
      getContext().getController().getComboBoxCourse()
    );

    setOnCourseComboBoxSelectionChange();
  }

  /**
   * Load the columns for the Academic Evaluator view.
   * <p>
   * The Academic Evaluator only needs to view the Academic's full name if in Academic view, as they may be from a different Academic,
   * and the Student's full name is always visible.
   * The Register Evaluation button is visible only in the Evaluator view.
   * The combobox for filtering by course is visible only in the Academic view.
   */
  @Override
  protected void loadView() throws NotFoundException, UserDisplayableException {
    getContext().getController().setColumnFullNameAcademicVisibilityAndConfigure(isAcademicView());
    getContext().getController().setColumnFullNameStudentVisibilityAndConfigure(true);
    getContext().getController().setButtonRegisterEvaluationVisibility(isEvaluatorView());
    getContext().getController().setButtonViewAcademicEvaluatorEvaluationListVisibility(true);
    getContext().getController().setContainerComboboxVisibility(isAcademicView());
  }

  @Override
  protected void loadTableItems() throws UserDisplayableException {
    if (isAcademicView()) {
      if (getContext().getController().getComboBoxCourse().getItems().isEmpty()) {
        throw new UserDisplayableException("No hay cursos disponibles para el académico actual.");
      }

      getContext().getController().setTableItems(
        getEvaluationPreviewDAO().getAllByAcademic(
          getCurrentAcademicDTO().getID(),
          getContext().getController().getComboBoxCourse().getValue().getNRC()
        )
      );
    } else {
      getContext().getController().setTableItems(
        getEvaluationPreviewDAO().getAllByEvaluator(getCurrentAcademicDTO().getID())
      );
    }
  }

  /**
   * Set the action for the button to switch between Academic and Evaluator view modes.
   * <p>
   * This method toggles the current view mode and reloads the view accordingly.
   */
  private void setOnViewModeChange() {
    getContext().getController().getButtonViewAcademicEvaluatorViewEvaluationList().setOnAction(event -> {
      if (currentViewMode == AcademicEvaluatorViewMode.ACADEMIC) {
        setCurrentViewMode(AcademicEvaluatorViewMode.EVALUATOR);
      } else {
        setCurrentViewMode(AcademicEvaluatorViewMode.ACADEMIC);
      }

      try {
        loadTableItemsAndView();
      } catch (NotFoundException | UserDisplayableException e) {
        AlertFacade.showErrorAndWait("Error al cargar de vista.", e.getMessage());
      }
    });
  }

  private void setOnCourseComboBoxSelectionChange() {
    getContext().getController().getComboBoxCourse().setOnAction(event -> {
      try {
        loadTableItems();
      } catch (UserDisplayableException e) {
        AlertFacade.showErrorAndWait("Error al cargar las evaluaciones por curso.", e.getMessage());
      }
    });
  }
}
