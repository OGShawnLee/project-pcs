package org.example.gui.modal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.common.ExceptionHandler;
import org.example.gui.AlertFacade;
import org.example.gui.controller.ConfirmationController;
import org.example.gui.controller.ContextController;

import java.io.IOException;

/** ModalFacade is a utility class that simplifies the creation and display of modals in the GUI.
 * It handles the loading of FXML resources, displaying modals, and managing context for controllers.
*/
public class ModalFacade {
  private final static Logger LOGGER = LogManager.getLogger(ModalFacade.class);
  private final Modal modal;

  /**
   * Constructs a ModalFacade with the given configuration.
   *
   * @param configuration the configuration for the modal
   * @throws IOException if there is an error loading the FXML resource
   */
  private ModalFacade(ModalFacadeConfiguration configuration) throws IOException {
    this.modal = new Modal(configuration);
  }

  private Modal getModal() {
    return modal;
  }

  private void display() {
    getModal().showAndWait();
  }

  public static void createAndDisplay(ModalFacadeConfiguration configuration) {
    try {
      ModalFacade modalFacade = new ModalFacade(configuration);
      modalFacade.display();
    } catch (IOException e) {
      AlertFacade.showErrorAndWait(
        ExceptionHandler.handleGUILoadIOException(LOGGER, e).getMessage()
      );
    }
  }

  /**
   * Displays a modal with a specific context.
   *
   * @param context the context to be passed to the controller
   * @param <T>     the type of the context
   */
  private <T> void displayContextModal(T context) {
    Modal modal = getModal();
    ContextController<T> controller = modal.getController();
    controller.initialize(context);
    modal.showAndWait();
  }

  /**
   * Creates and displays a modal with a specific context.
   * This method is useful for scenarios where the modal needs to operate with a specific context,
   * such as when working on a database record.
   * <p>
   * It initializes the controller with the provided context and displays the modal.
   * This method is generic and can be used with any type of context.
   * It offers a convenient way to create and display modals without needing to
   * manually set up the controller and modal each time.
   *
   * @param configuration the configuration for the modal
   * @param context       the context to be passed to the controller
   * @param <T>           the type of the context
   */
  public static <T> void createAndDisplayContextModal(ModalFacadeConfiguration configuration, T context) {
    try {
      ModalFacade modalFacade = new ModalFacade(configuration);
      modalFacade.displayContextModal(context);
    } catch (IOException e) {
      AlertFacade.showErrorAndWait(
        ExceptionHandler.handleGUILoadIOException(LOGGER, e).getMessage()
      );
    }
  }

  private boolean displayConfirmation(String message) {
    Modal modal = getModal();
    ConfirmationController controller = modal.getController();
    controller.setStage(modal.getStage());
    controller.setMessage(message);
    modal.showAndWait();
    return controller.getIsConfirmed();
  }

  public static boolean createAndDisplayConfirmation(String message) {
    try {
      ModalFacade modalFacade = new ModalFacade(new ModalFacadeConfiguration("Confirmación", "ConfirmationModal"));
      return modalFacade.displayConfirmation(message);
    } catch (IOException e) {
      AlertFacade.showErrorAndWait(
        ExceptionHandler.handleGUILoadIOException(LOGGER, e).getMessage()
      );
      return false;
    }
  }
}