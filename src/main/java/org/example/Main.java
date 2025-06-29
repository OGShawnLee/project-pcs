package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.common.ExceptionHandler;
import org.example.gui.Modal;

import java.io.IOException;

public class Main {
  private static final Logger LOGGER = LogManager.getLogger(Main.class);

  public static class App extends Application {
    @Override
    public void start(Stage stage) {
      try {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/org/example/LoginPage.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Sistema Gestor de Practicas Profesionales");
        stage.setScene(scene);
        stage.show();
      } catch (IOException e) {
        Modal.displayError(ExceptionHandler.handleGUILoadIOException(LOGGER, e).getMessage());
      } catch (IllegalStateException e) {
        handleIllegalStateException(e);
      } catch (Exception e) {
        Modal.displayError(
          ExceptionHandler.handleUnexpectedException(LOGGER, e, "Error al iniciar la aplicación.").getMessage()
        );
      }
    }

    private void handleIllegalStateException(IllegalStateException e) {
      LOGGER.fatal("Error al iniciar la aplicación: {}", e.getMessage(), e);
      Modal.displayError(
        "Error de estado de interfaz gráfica al iniciar la aplicación. Por favor, comuníquese con el desarrollador si el error persiste."
      );
    }
  }

  public static void main(String[] args) {
    Application.launch(App.class);
  }
}