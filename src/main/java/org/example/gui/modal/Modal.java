package org.example.gui.modal;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

class Modal {
  private ModalFacadeConfiguration configuration;
  private final Parent parent;
  private final Scene scene;
  private final Stage stage;
  private final FXMLLoader loader;

  public Modal(ModalFacadeConfiguration configuration) throws IOException {
    this.configuration = configuration;
    this.loader = createFXMLLoader(configuration.getResourceFileName());
    this.parent = loader.load();
    this.scene = new Scene(this.parent);
    this.stage = createStage(configuration.getTitle());
  }

  private ModalFacadeConfiguration getConfiguration() {
    return configuration;
  }

  public Parent getParent() {
    return parent;
  }

  public Scene getScene() {
    return scene;
  }

  public Stage getStage() {
    return stage;
  }

  private FXMLLoader createFXMLLoader(String resourceFileName) throws IOException {
    return new FXMLLoader(
      Objects.requireNonNull(Modal.class.getResource("/org/example/" + resourceFileName + ".fxml"))
    );
  }

  private Stage createStage(String title) {
    Stage modalStage = new Stage();
    modalStage.setTitle(title);
    modalStage.setScene(scene);
    modalStage.setResizable(false);
    modalStage.initModality(Modality.APPLICATION_MODAL);

    getConfiguration().getOnClose().ifPresent(runnable ->
      modalStage.setOnHidden(event -> runnable.run())
    );

    return modalStage;
  }

  public <T> T getController() {
    return loader.getController();
  }

  public void showAndWait() {
    stage.showAndWait();
  }
}