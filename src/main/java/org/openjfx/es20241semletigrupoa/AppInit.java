package org.openjfx.es20241semletigrupoa;

import iscte.se.landmanagement.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class AppInit {

    // Constants
    private static final String RESOURCES_PATH = "src/main/resources/";
    private static final String DEFAULT_FILE_NAME = "Madeira-Moodle-1.1.csv";


    // FXML Elements
    @FXML private Button goBack5;
    @FXML private Button goBack3;
    @FXML private Button uploadButton;
    @FXML private Button nextButton;
    @FXML private Button calculateA1;
    @FXML private Button calculateA2;
    @FXML private ComboBox<String> islandComboBox;
    @FXML private ComboBox<String> municipalityComboBox;
    @FXML private ComboBox<String> parishComboBox;
    @FXML private Label fileNameLabel;
    @FXML private Label areaResultLabel1;
    @FXML private Label areaResultLabel2;
    @FXML private AnchorPane rootPane;
    @FXML private Button structure1Button;
    @FXML private Button structure2Button;
    @FXML private Button AreasButton;

    // Other Fields

    private FXMLLoader fxmlLoader;
    private Stage stage;
    private File selectedFile;
    private static PropFileReader propFileReader;
    private static GraphStructure graphStructure;
    private static CalcAreas calcAreas;
    private final static Map<String, Map<String, List<String>>> locationData = new HashMap<>();
    private static Map<String,String> location = new HashMap<>();
    private final Map<String, String> selectedLocation = new HashMap<>();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Reads data and navigates to "Stage2.fxml".
     */
    @FXML
    void readData(MouseEvent event) {
        navigateToScene("Stage2.fxml", "Stage 2");
    }

    /**
     * Handles file upload logic.
     */
    @FXML
    void handleFileUpload(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            fileNameLabel.setText(selectedFile.getName());
            moveFileToResources(selectedFile);
            createProcessFileButton();
        } else {
            fileNameLabel.setText("No file selected");
        }
    }

    /**
     * Moves the selected file to the resources directory.
     */
    private void moveFileToResources(File file) {
        try {
            Path destination = Paths.get(RESOURCES_PATH, file.getName());
            if(!destination.toFile().exists()) {
                Files.copy(file.toPath(), destination);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error moving file to resources directory", e);
        }
    }

    /**
     * Creates and adds the "Process File" button dynamically.
     */
    private void createProcessFileButton() {
        Button processButton = new Button("Process File");
        processButton.setOnMouseClicked(event -> processFile(selectedFile.getName()));
        processButton.setLayoutX(260);
        processButton.setLayoutY(240);
        rootPane.getChildren().add(processButton);
    }

    /**
     * Processes the uploaded file and initializes data.
     */
    private void processFile(String fileName) {
        try {
            Path filePath = Paths.get(RESOURCES_PATH, fileName);
            propFileReader = new PropFileReader(filePath);
            propFileReader.readFile();
            propFileReader.convertToPropertiy();
            graphStructure = new GraphStructure(propFileReader.getProperties(), 4);
            calcAreas = new CalcAreas(graphStructure.getG());
            buildLocationData();
            navigateToScene("Stage3.fxml", "Stage 3"); // Navigate to the next stage after processing the file
        } catch (Exception e) {
            throw new RuntimeException("Error processing file", e);
        }
    }


    /**
     * Navigates to Stage 5 and initializes combo boxes.
     */
    @FXML
    void goToAreas(MouseEvent event) {
        navigateToScene("Stage5.fxml", "Stage 5");
        rootPane=fxmlLoader.getRoot();
        initializeComboBoxes();

    }

    /**
     * Handles structure1 button action, visualizes the first structure.
     */
    @FXML
    void getStructure1(MouseEvent event) {
        try {
            graphStructure = new GraphStructure(propFileReader.getProperties(), 4);
            graphStructure.visualizeGraph(); // Assuming visualizeGraph() visualizes structure
        } catch (Exception e) {
            throw new RuntimeException("Error visualizing structure 1", e);
        }
    }

    /**
     * Handles structure2 button action (currently empty, add functionality if needed).
     */
    @FXML
    void getStructure2(MouseEvent event) {
        // Structure 2 functionality (if any) should be added here
        System.out.println("Structure 2 clicked");
    }

    /**
     * Builds the hierarchical location data.
     */
    private void buildLocationData() {
        for (Property property : propFileReader.getProperties()) {
            String island = property.getIsland();
            String municipality = property.getMunicipality();
            String parish = property.getParish();

            // Add island if not present
            locationData.putIfAbsent(island, new HashMap<>());

            // Add municipality to the island if not present
            locationData.get(island).putIfAbsent(municipality, new ArrayList<>());

            // Add parish to the municipality if not already in the list
            if (!locationData.get(island).get(municipality).contains(parish)) {
                locationData.get(island).get(municipality).add(parish);
            }
        }
    }

    /**
     * Initializes the dropdown menus for selecting locations.
     */
    private void initializeComboBoxes() {

        islandComboBox= new ComboBox<>();
        municipalityComboBox= new ComboBox<>();
        parishComboBox= new ComboBox<>();

        islandComboBox.setPrefWidth(200);
        islandComboBox.setPrefHeight(30);

        municipalityComboBox.setPrefWidth(200);
        municipalityComboBox.setPrefHeight(30);

        parishComboBox.setPrefWidth(200);
        parishComboBox.setPrefHeight(30);

        ObservableList<String> islands = FXCollections.observableArrayList(locationData.keySet());

        islandComboBox.setItems(islands);

        rootPane.getChildren().add(islandComboBox);

        islandComboBox.setLayoutX(200);
        islandComboBox.setLayoutY(100);


        islandComboBox.setOnAction(event -> populateMunicipalities());
        municipalityComboBox.setOnAction(event -> populateParishes());
        //parishComboBox.setOnAction(event -> enableCalculateAreasButton());
    }

    private void populateMunicipalities() {
        String selectedIsland = islandComboBox.getValue();
        if (selectedIsland != null) {
            Map<String, List<String>> municipalities = locationData.get(selectedIsland);
            ObservableList<String> municipalityList = FXCollections.observableArrayList(municipalities.keySet());
            municipalityComboBox.setItems(municipalityList);
            municipalityComboBox.getSelectionModel().clearSelection();

            parishComboBox.getSelectionModel().clearSelection();
            parishComboBox.getItems().clear();

            rootPane.getChildren().add(municipalityComboBox);

            municipalityComboBox.setLayoutX(200);
            municipalityComboBox.setLayoutY(130);
        }
    }

    private void populateParishes() {
        String selectedIsland = islandComboBox.getValue();
        String selectedMunicipality = municipalityComboBox.getValue();
        if (selectedIsland != null && selectedMunicipality != null) {
            Map<String, List<String>> municipalities = locationData.get(selectedIsland);
            List<String> parishes = municipalities.get(selectedMunicipality);
            ObservableList<String> parishList = FXCollections.observableArrayList(parishes);
            parishComboBox.setItems(parishList);

            parishComboBox.getSelectionModel().clearSelection();


            rootPane.getChildren().add(parishComboBox);
            parishComboBox.setLayoutX(200);
            parishComboBox.setLayoutY(160);
        }

        enableCalculateAreasButton();
    }

    private void enableCalculateAreasButton() {
        nextButton = new Button("Next");
        location=new HashMap<>();
        nextButton.setOnMouseClicked(event -> {
            location.putIfAbsent("Ilha",islandComboBox.getValue());
            location.putIfAbsent("Municipio",municipalityComboBox.getValue());
            location.putIfAbsent("Freguesia",parishComboBox.getValue());
            navigateToScene("Stage6.fxml", "Stage 6");
        });

        nextButton.setLayoutX(280);
        nextButton.setLayoutY(220);

        rootPane.getChildren().add(nextButton);

//        graphStructure = new GraphStructure(propFileReader.getProperties(), 4);
//        calcAreas = new CalcAreas(graphStructure.getG());
    }

    /**
     * Calculates and displays area 1.
     */
    @FXML
    void calculateA1(MouseEvent event) {

        try {
            String Ilha = location.get("Ilha");
            String Municipio = location.get("Municipio");
            String Freguesia = location.get("Freguesia");

//            graphStructure = new GraphStructure(propFileReader.getProperties(), 4);
//            CalcAreas calcAreas = new CalcAreas(graphStructure.getG());

            if (Freguesia == null) {
                areaResultLabel1.setText(String.valueOf(calcAreas.calcArea3(Municipio, "Municipio")));
            } else {
                areaResultLabel1.setText(String.valueOf(calcAreas.calcArea3(Freguesia, "Freguesia")));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Calculates and displays area 2.
     */
    @FXML
    void calculateA2(MouseEvent event) {

        try {
            String Ilha = location.get("Ilha");
            String Municipio = location.get("Municipio");
            String Freguesia = location.get("Freguesia");

            if (Freguesia == null) {
                areaResultLabel2.setText(String.valueOf(calcAreas.calcArea4(Municipio, "Municipio")));
            } else {
                areaResultLabel2.setText(String.valueOf(calcAreas.calcArea4(Freguesia, "Freguesia")));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles navigation back to the previous scene.
     */
    @FXML
    void goBack5(MouseEvent event) {
        navigateToScene("Stage5.fxml", "Stage 5");
        rootPane=fxmlLoader.getRoot();
        initializeComboBoxes();
    }

    /**
     * Handles navigation back to the previous scene.
     */
    @FXML
    void goBack3(MouseEvent event) {
        navigateToScene("Stage3.fxml", "Stage 3");
    }

    /**
     * Generic method to handle scene navigation.
     */
    private void navigateToScene(String fxmlFile, String title) {
        try {
            fxmlLoader = new FXMLLoader(AppInit.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            AppInit controller = fxmlLoader.getController(); // If you need to pass data to the next controller
            controller.setStage(stage); // Pass the stage
            stage.setScene(scene);
            stage.setTitle(title);
        } catch (IOException e) {
            throw new RuntimeException("Error loading scene: " + fxmlFile, e);
        }
    }
}

