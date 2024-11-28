package org.openjfx.es20241semletigrupoa;

import iscte.se.landmanagement.PropFileReader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IO;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.PolicyNode;

public class HelloController {

    private Stage stage;

    private File selectedFile;

    @FXML
    private Button button;
    @FXML
    private Label fileNameLabel;

    @FXML
    private Button uploadButton;

    @FXML
    private AnchorPane rootPane;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void readData(MouseEvent event) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Stage2.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        HelloController controller = fxmlLoader.getController();
        controller.setStage(stage);


        stage.setTitle("Stage 2");
        stage.setScene(scene);
        stage.show();
    }

//    @FXML
//    void goBack(MouseEvent event) throws IOException {
//        Stage stage = (Stage) button.getScene().getWindow();
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Stage1.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Stage 1");
//        stage.setScene(scene);
//    }

    @FXML
    void handleUpload(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showOpenDialog(stage);

        if(file != null) {
            selectedFile = file;
            fileNameLabel.setText(file.getName());

            moveFile(file);
            createStoreButton();
        }else {
            fileNameLabel.setText("No file Selected");
        }

    }

    private void moveFile(File file) throws IOException {
        File  resources=new File("src/main/resources");
        File dest=new File(resources,file.getName());

        Files.copy(file.toPath(), dest.toPath());
    }


    private void createStoreButton() {
        Button storeButton = new Button("Store File");
        storeButton.setOnAction(event -> {
            try {
                processFile(selectedFile.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        storeButton.setLayoutX(100);
        storeButton.setLayoutY(220);

        rootPane.getChildren().add(storeButton);
    }

    private void processFile(String file) throws Exception {
        String lastSegment = Paths.get(file).getFileName().toString();

        System.out.println(lastSegment);
//        URL url = Thread.currentThread().getContextClassLoader().getResource(lastSegment);
//        if (url == null) {
//            System.out.println("Arquivo CSV não encontrado!");
//            return;
//        }

        String path1="C:\\Users\\migue\\ES-2024-1Sem-LETI-GrupoA\\src\\main\\resources\\"+lastSegment;

        Path path = Paths.get(path1);
        PropFileReader propFileReader = new PropFileReader(path);

        propFileReader.readFile();
        propFileReader.convertToPropertiy();
        System.out.println(propFileReader.getProperties());
    }

}