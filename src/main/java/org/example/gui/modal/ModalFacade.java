package org.example.gui.modal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.common.ExceptionHandler;
import org.example.gui.AlertFacade;
import org.example.gui.controller.ConfirmationController;
import org.example.gui.controller.ContextController;

import java.io.IOException;

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

  private <T> void displayContextModal(T context) {
    Modal modal = getModal();
    ContextController<T> controller = modal.getController();
    controller.initialize(context);
    modal.showAndWait();
  }

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