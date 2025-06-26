package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.db.DBConnector;

import java.io.IOException;

public class Main {
  public static class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
      FXMLLoader loader = new FXMLLoader(Main.class.getResource("/org/example/LoginPage.fxml"));
      Scene scene = new Scene(loader.load());
      stage.setTitle("Sistema Gestor de Practicas Profesionales");
      stage.setScene(scene);
      stage.show();
    }

    @Override
    public void stop() {
      DBConnector.close();
    }
  }

  public static void main(String[] args) {
    Application.launch(App.class);
  }
}