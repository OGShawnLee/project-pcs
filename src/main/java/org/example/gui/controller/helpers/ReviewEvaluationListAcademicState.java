package org.example.gui.controller.helpers;

import org.example.business.auth.AuthClient;
import org.example.business.dao.NotFoundException;
import org.example.business.dto.AcademicDTO;
import org.example.common.UserDisplayableException;
import org.example.gui.AlertFacade;
import org.example.gui.combobox.CourseComboBoxLoader;

public class ReviewEvaluationListAcademicState extends ReviewEvaluationListState {
  private final AcademicDTO currentAcademicDTO;

  public ReviewEvaluationListAcademicState(ReviewEvaluationListContext context) throws NotFoundException, UserDisplayableException {
    super(context);
    this.currentAcademicDTO = AuthClient.getInstance().getCurrentAcademicDTO();
  }

  public AcademicDTO getCurrentAcademicDTO() {
    return currentAcademicDTO;
  }

  /**
   * Load the columns for the Academic view.
   * <p>
   * The Academic needs to view both the Academic's full name and the Student's full name,
   * so both columns are visible.
   * The Register Evaluation button is not needed for the Academic view, but the combobox for filtering by course is needed.
   */
  @Override
  protected void loadView() throws NotFoundException, UserDisplayableException {
    getContext().getController().setColumnFullNameAcademicVisibilityAndConfigure(true);
    getContext().getController().setColumnFullNameStudentVisibilityAndConfigure(true);
    getContext().getController().setButtonRegisterEvaluationVisibility(false);
    getContext().getController().setButtonViewAcademicEvaluatorEvaluationListVisibility(false);
    getContext().getController().setContainerComboboxVisibility(true);
    CourseComboBoxLoader.loadByCurrentAcademicDTO(
      getContext().getController().getComboBoxCourse()
    );
    setOnCourseComboBoxSelectionChange();
  }

  @Override
  protected void loadTableItems() throws UserDisplayableException {
    if (getContext().getController().getComboBoxCourse().getItems().isEmpty()) {
      throw new UserDisplayableException("No hay cursos disponibles para el académico actual.");
    }

    getContext().getController().setTableItems(
      getEvaluationPreviewDAO().getAllByAcademic(
        getCurrentAcademicDTO().getID(),
        getContext().getController().getComboBoxCourse().getValue().getNRC()
      )
    );
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
