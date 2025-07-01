package org.example.gui.controller.helpers;

import org.example.business.auth.AuthClient;
import org.example.business.dao.NotFoundException;
import org.example.business.dto.StudentDTO;
import org.example.common.UserDisplayableException;

public class ReviewEvaluationListStudentState extends ReviewEvaluationListState {
  private final StudentDTO currentStudentDTO;

  public ReviewEvaluationListStudentState(ReviewEvaluationListContext context) throws NotFoundException, UserDisplayableException {
    super(context);
    this.currentStudentDTO = AuthClient.getInstance().getCurrentStudentDTO();
  }

  public StudentDTO getCurrentStudentDTO() {
    return currentStudentDTO;
  }

  /*
   * Load the columns for the Student view.
   * The Student doesn't need to view the Student full name as it is the current user that is logged in,
   * so it only needs to view the Academic's full name.
   * The Register Evaluation button is not needed for the Student view, nor the combobox for filtering by course.
   */
  @Override
  protected void loadView() {
    getContext().getController().setColumnFullNameAcademicVisibilityAndConfigure(true);
    getContext().getController().setColumnFullNameStudentVisibilityAndConfigure(false);
    getContext().getController().setButtonRegisterEvaluationVisibility(false);
    getContext().getController().setButtonViewAcademicEvaluatorEvaluationListVisibility(false);
    getContext().getController().setContainerComboboxVisibility(false);
  }

  @Override
  protected void loadTableItems() throws UserDisplayableException {
    getContext().getController().setTableItems(
      getEvaluationPreviewDAO().getAllByStudent(getCurrentStudentDTO().getID())
    );
  }
}
