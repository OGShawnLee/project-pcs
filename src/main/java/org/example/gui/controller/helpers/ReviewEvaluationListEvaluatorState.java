package org.example.gui.controller.helpers;

import org.example.business.auth.AuthClient;
import org.example.business.dao.NotFoundException;
import org.example.business.dto.AcademicDTO;
import org.example.common.UserDisplayableException;

public class ReviewEvaluationListEvaluatorState extends ReviewEvaluationListState {
  private final AcademicDTO currentAcademicDTO;

  public ReviewEvaluationListEvaluatorState(ReviewEvaluationListContext context) throws NotFoundException, UserDisplayableException {
    super(context);
    currentAcademicDTO = AuthClient.getInstance().getCurrentAcademicDTO();
  }

  public AcademicDTO getCurrentAcademicDTO() {
    return currentAcademicDTO;
  }

  /**
   * Load the columns for the Evaluator view.
   * The Evaluator doesn't need to view the Academic full name as it the current user that is logged in,
   * so it only needs to view the Student's full name.
   */
  @Override
  protected void loadView() {
    getContext().getController().setColumnFullNameAcademicVisibilityAndConfigure(false);
    getContext().getController().setColumnFullNameStudentVisibilityAndConfigure(true);
    getContext().getController().setButtonRegisterEvaluationVisibility(true);
    getContext().getController().setButtonViewAcademicEvaluatorEvaluationListVisibility(false);
    getContext().getController().setContainerComboboxVisibility(false);
  }

  @Override
  protected void loadTableItems() throws UserDisplayableException {
    getContext().getController().setTableItems(
      getEvaluationPreviewDAO().getAllByEvaluator(getCurrentAcademicDTO().getID())
    );
  }
}