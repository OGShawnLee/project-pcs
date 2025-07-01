package org.example.gui.controller.helpers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.example.business.auth.AuthClient;
import org.example.business.dao.NotFoundException;
import org.example.common.UserDisplayableException;
import org.example.gui.controller.ReviewEvaluationListController;

public class ReviewEvaluationListContext {
  private final ReviewEvaluationListState state;
  private final ReviewEvaluationListController controller;

  public ReviewEvaluationListContext(ReviewEvaluationListController controller) throws UserDisplayableException, NotFoundException {
    Logger LOGGER = LogManager.getLogger(ReviewEvaluationListContext.class.getName());
    this.controller = controller;
    this.state = switch (AuthClient.getInstance().getCurrentUser().role()) {
      case ACADEMIC_EVALUATOR -> new ReviewEvaluationListAcademicEvaluatorState(this);
      case ACADEMIC -> new ReviewEvaluationListAcademicState(this);
      case EVALUATOR -> new ReviewEvaluationListEvaluatorState(this);
      case COORDINATOR -> {
        LOGGER.fatal("El rol de Coordinador no puede ver evaluaciones directamente.");
        throw new UserDisplayableException("Acción Prohibida. El rol de Coordinador no puede ver evaluaciones directamente.");
      }
      case STUDENT -> new ReviewEvaluationListStudentState(this);
    };
  }

  public ReviewEvaluationListController getController() {
    return controller;
  }

  public void loadTableItemsAndView() throws NotFoundException, UserDisplayableException {
    state.loadTableItemsAndView();
  }

  public void loadTableItems() throws NotFoundException, UserDisplayableException {
    state.loadTableItems();
  }
}