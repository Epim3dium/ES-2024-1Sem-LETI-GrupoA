package org.openjfx.es20241semletigrupoa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Stage1.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        AppInit controller = fxmlLoader.getController();

        stage.setTitle("Stage 1");
        stage.setScene(scene);
        stage.show();
        controller.setStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}