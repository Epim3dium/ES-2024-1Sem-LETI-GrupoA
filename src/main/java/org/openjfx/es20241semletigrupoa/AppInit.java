package org.openjfx.es20241semletigrupoa;

import iscte.se.landmanagement.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppInit {

    // Constants
    private static final String RESOURCES_PATH = "src/main/resources/";
    private static final String DEFAULT_FILE_NAME = "Madeira-Moodle-1.1.csv";

    private static class Record {
        private int ranking = 0;
        private int otherOwner = 0;
        private int myProperty = 0;
        private int otherProperty = 0;
        private double myAreaInc = 0;
        private double otherAreaInc = 0;

        final public double getMyAreaInc() {return myAreaInc;}
        final public double getOtherAreaInc() {return otherAreaInc;}
        final public int getRanking() {return ranking;}
        final public int getOtherOwner() { return otherOwner;}
        final public int getMyProperty() { return myProperty;}
        final public int getOtherProperty() { return otherProperty;}
        Record(int ranking, int otherOwner, int myProperty, int otherProperty, double myAreaInc, double otherAreaInc) {
            this.ranking = ranking;
            this.otherOwner = otherOwner;
            this.myProperty = myProperty;
            this.otherProperty = otherProperty;
            this.myAreaInc = myAreaInc;
            this.otherAreaInc = otherAreaInc;
        }
    };

    // FXML Elements
    @FXML private TableView<Record> tableView;
    @FXML private TableColumn<Record, Integer> otherOwnerColumn;
    @FXML private TableColumn<Record, Integer> myPropertyColumn;
    @FXML private TableColumn<Record, Integer> otherPropertyColumn;
    @FXML private TableColumn<Record, Integer> rankColumn;
    @FXML private TableColumn<Record, Double> areaIncColumn;
    @FXML private TableColumn<Record, Double> otherIncColumn;
    @FXML private TextField OwID;
    @FXML private Button Ex;
    @FXML private Button Sugg;
    @FXML private Button goBack5;
    @FXML private Button goBack3;
    @FXML private Button goB3;
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



    private FXMLLoader fxmlLoader;
    private Stage stage;
    private File selectedFile;
    private static PropFileReader propFileReader;
    private static GraphStructure graphStructure;
    private static CalcAreas calcAreas;
    private static OwnerGraphStructure ownerGraphStructure;
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
            graphStructure = new GraphStructure(propFileReader.getProperties(), 4);
            //OwgraphStructure = new OwnerGraphStructure(graphStructure.getG());
            ownerGraphStructure =new OwnerGraphStructure(graphStructure.getG());
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
        try {

           ownerGraphStructure.visualizeGraph();
        } catch (Exception e) {
            throw new RuntimeException("Error visualizing structure 1", e);
        }
    }


    /**
     * Builds the hierarchical location data.
     */
    private void buildLocationData() {
        for (Property property : propFileReader.getProperties()) {
            String island = property.getIsland();
            String municipality = property.getMunicipality();
            String parish = property.getParish();


            locationData.putIfAbsent(island, new HashMap<>());


            locationData.get(island).putIfAbsent(municipality, new ArrayList<>());


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

    @FXML
    public void initialize() {
        try {
            otherOwnerColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Integer>, ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Record, Integer> p) {
                    return new SimpleIntegerProperty(p.getValue().getOtherOwner()).asObject();
                }
            });
            myPropertyColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Integer>, ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Record, Integer> p) {
                    return new SimpleIntegerProperty(p.getValue().getMyProperty()).asObject();
                }
            });
            otherPropertyColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Integer>, ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Record, Integer> p) {
                    return new SimpleIntegerProperty(p.getValue().getOtherProperty()).asObject();
                }
            });
            rankColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Integer>, ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Record, Integer> p) {
                    return new SimpleIntegerProperty(p.getValue().getRanking()).asObject();
                }
            });
            areaIncColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Double>, ObservableValue<Double>>() {
                public ObservableValue<Double> call(TableColumn.CellDataFeatures<Record, Double> p) {
                    return new SimpleDoubleProperty(p.getValue().getMyAreaInc()).asObject();
                }
            });
            otherIncColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Double>, ObservableValue<Double>>() {
                public ObservableValue<Double> call(TableColumn.CellDataFeatures<Record, Double> p) {
                    return new SimpleDoubleProperty(p.getValue().getOtherAreaInc()).asObject();
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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


    public void handleSugg(MouseEvent mouseEvent) {
        navigateToScene("Stage7.fxml", "Stage 7");

    }

    public void listEx(MouseEvent mouseEvent) throws Exception {
        int OwnerId = Integer.parseInt(OwID.getText());



        ArrayList<Record> s=new ArrayList<>();
        List<OwnerGraphStructure.PropertyPair> exchanges = ownerGraphStructure.generateAllExchanges();
        //filter
        for(int i = 0; i < exchanges.size(); i++) {
            OwnerGraphStructure.PropertyPair pair = exchanges.get(i);
            if (pair.getFirst().getOwnerID() != OwnerId) {
                pair.swap();
            }
            if (pair.getFirst().getOwnerID() != OwnerId) {
                exchanges.remove(i);
                i--;
            }
        }
        int i = 1;
        for (OwnerGraphStructure.PropertyPair pair:exchanges) {
            Pair<Double, Double> increases= OwnerGraphStructure.calcAvgAreaIncrease(ownerGraphStructure, pair);
            if(increases.getValue() < 0 || increases.getKey() < 0)
                continue;
            Record r = new Record(i++, pair.getSecond().getOwnerID(), pair.getFirst().getPropertyID(), pair.getSecond().getPropertyID(), increases.getKey(), increases.getValue());
            s.add(r);
        }
        ObservableList<Record> items = FXCollections.observableArrayList(s);
        tableView.setItems(items);
    }
}

