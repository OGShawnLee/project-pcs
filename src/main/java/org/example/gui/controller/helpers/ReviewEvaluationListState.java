package org.example.gui.controller.helpers;

import org.example.business.dao.EvaluationPreviewDAO;
import org.example.business.dao.NotFoundException;
import org.example.common.UserDisplayableException;

/**
 * Abstract class representing the state of the review evaluation list.
 * This class provides a common structure for different user roles (Academic, Student, Evaluator)
 * to load the evaluation previews and configure the view accordingly.
 */
public abstract class ReviewEvaluationListState {
  private static final EvaluationPreviewDAO EVALUATION_PREVIEW_DAO = new EvaluationPreviewDAO();
  private final ReviewEvaluationListContext context;

  public ReviewEvaluationListState(ReviewEvaluationListContext context) {
    this.context = context;
  }

  /**
   * Load the table items and view based on the user role.
   * This method should be called to initialize the view and load the evaluation previews
   * according to the user's role (Academic, Student, Evaluator).
   *
   * @throws NotFoundException if a required resource is not found.
   * @throws UserDisplayableException if an error occurs that should be displayed to the user.
   */
  protected void loadTableItemsAndView() throws NotFoundException, UserDisplayableException {
    loadView();
    loadTableItems();
  }

  /**
   * Load the view specific to the user role.
   * This method should be implemented by subclasses to configure the view
   * according to the user's role (Academic, Student, Evaluator).
   *
   * @throws NotFoundException if a required resource is not found.
   * @throws UserDisplayableException if an error occurs that should be displayed to the user.
   */
  protected abstract void loadView() throws NotFoundException, UserDisplayableException;

  /**
   * Load the table items specific to the user role.
   * This method should be implemented by subclasses to load the evaluation previews
   * according to the user's role (Academic, Student, Evaluator).
   *
   * @throws NotFoundException if a required resource is not found.
   * @throws UserDisplayableException if an error occurs that should be displayed to the user.
   */
  protected abstract void loadTableItems() throws NotFoundException, UserDisplayableException;

  protected EvaluationPreviewDAO getEvaluationPreviewDAO() {
    return EVALUATION_PREVIEW_DAO;
  }

  protected ReviewEvaluationListContext getContext() {
    return context;
  }
}