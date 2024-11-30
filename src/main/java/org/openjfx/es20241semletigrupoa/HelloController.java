package org.openjfx.es20241semletigrupoa;

import iscte.se.landmanagement.CalcAreas;
import iscte.se.landmanagement.GraphStructure;
import iscte.se.landmanagement.PropFileReader;
import iscte.se.landmanagement.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloController {


    public Button goBack5;

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

    @FXML
    private Button structure1;

    @FXML
    private Button structure2;

    @FXML
    private Button areas;

    @FXML
    private Button backB;

    @FXML
    private ComboBox<String> islandCB;

    @FXML
    private ComboBox<String> muniCB;

    @FXML
    private ComboBox<String> parishCB;

    @FXML
    private Button A1;

    @FXML
    private Button A2;

    @FXML
    private Label labelA1;

    @FXML
    private Label labelA2;

    private final Map<String, Map<String, List<String>>> locationData = new HashMap<>();

    private static Map<String,String> location = new HashMap<>();

    private PropFileReader propFileReader;

    //private GraphStructure structureO;

    private CalcAreas careas;

    private boolean b=false;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void readData(MouseEvent event) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Stage2.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        fxmlLoader.setController(this);
        this.setStage(stage);


        stage.setTitle("Stage 2");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goBack(MouseEvent event) throws IOException {
        Stage stage = (Stage) backB.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Stage3.fxml"));
        //fxmlLoader.setController(this);
        this.setStage(stage);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Stage 3");
        stage.setScene(scene);
    }

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
        button = new Button("Store File");
        button.setOnMouseClicked(event -> {
            try {
                processFile(selectedFile.toString());
                System.out.println(propFileReader + "2");
                if(b){
                    changeStage3("Stage3.fxml");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        button.setLayoutX(100);
        button.setLayoutY(220);

        rootPane.getChildren().add(button);
    }

    private void changeStage3(String s) throws IOException {

        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(s));
        Scene scene = new Scene(fxmlLoader.load());

        fxmlLoader.setController(this);
        this.setStage(stage);


        stage.setTitle("Stage 3");
        stage.setScene(scene);
        stage.show();

        System.out.println(propFileReader + "3");


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
        propFileReader = new PropFileReader(path);

        propFileReader.readFile();
        propFileReader.convertToPropertiy();

//        structureO=new GraphStructure(propFileReader.getProperties(),4);
//        System.out.println(structureO==null);



        b=true;

        System.out.println(propFileReader.getProperties());
    }

    private void processFile2() throws Exception {

        String path1="C:\\Users\\migue\\ES-2024-1Sem-LETI-GrupoA\\src\\main\\resources\\Madeira-Moodle-1.1.csv";

        Path path = Paths.get(path1);
        propFileReader = new PropFileReader(path);

        propFileReader.readFile();
        propFileReader.convertToPropertiy();



        b=true;


    }

    @FXML
    void getStructure1(MouseEvent event) throws IOException {
//        b=false;
//        structureO=new GraphStructure(propFileReader.getProperties(),4);
//        b=true;
//        if(b) {
//            Stage stage = (Stage) structure1.getScene().getWindow();
//            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Stage4.fxml"));
//            Scene scene = new Scene(fxmlLoader.load());
//
//            fxmlLoader.setController(this);
//            this.setStage(stage);
//
//
//            stage.setTitle("Stage 4");
//            stage.setScene(scene);
//            stage.show();
//        }



    }

    @FXML
    void getStructure2(MouseEvent event) {

    }

    @FXML
    void calcAreas(MouseEvent event) throws Exception {

        Stage stage = (Stage) areas.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Stage5.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        fxmlLoader.setController(this);
        this.setStage(stage);


        stage.setTitle("Stage 5");
        stage.setScene(scene);
        stage.show();

        System.out.println(propFileReader + "5");

        rootPane=fxmlLoader.getRoot();

        initCB();
    }

    @FXML
    private void initCB() throws Exception {
        processFile2();
        buildLocationData();

        islandCB= new ComboBox<>();
        muniCB= new ComboBox<>();
        parishCB= new ComboBox<>();

        islandCB.setPrefWidth(200);
        islandCB.setPrefHeight(30);

        muniCB.setPrefWidth(200);
        muniCB.setPrefHeight(30);

        parishCB.setPrefWidth(200);
        parishCB.setPrefHeight(30);


        ObservableList<String> islands = FXCollections.observableArrayList(locationData.keySet());

        islandCB.setItems(islands);

        rootPane.getChildren().add(islandCB);
        islandCB.setLayoutX(200);
        islandCB.setLayoutY(100);


        islandCB.setOnAction(event -> handleIslandSelection());
        muniCB.setOnAction(event -> handleMunicipalitySelection());
        



    }

    private void createNextButton() {
        button = new Button("Next");
        button.setOnMouseClicked(event -> {
            try {
                location.putIfAbsent("Ilha",islandCB.getValue());
                location.putIfAbsent("Municipio",muniCB.getValue());
                location.putIfAbsent("Freguesia",parishCB.getValue());
                changeStage6();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        button.setLayoutX(280);
        button.setLayoutY(220);

        rootPane.getChildren().add(button);

    }

    private void changeStage6() throws IOException {

        Map<String, String> location6 = location;
        System.out.println(location6);
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Stage6.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        fxmlLoader.setController(this);
        this.setStage(stage);

        location = location6;

        stage.setTitle("Stage 6");
        stage.setScene(scene);
        stage.show();


        System.out.println(location6);
        location=location6;




        //careas=new CalcAreas(new GraphStructure(propFileReader.getProperties(),4).getG());






    }

    private void handleIslandSelection() {
        String selectedIsland = (String) islandCB.getValue();

        if (selectedIsland != null) {
            // Populate municipalityComboBox based on selected island
            Map<String, List<String>> municipalities = locationData.get(selectedIsland);
            ObservableList<String> municipalityList = FXCollections.observableArrayList(municipalities.keySet());
            muniCB.setItems(municipalityList);

            // Clear downstream selections
            muniCB.getSelectionModel().clearSelection();
            parishCB.getSelectionModel().clearSelection();
            parishCB.getItems().clear();

            rootPane.getChildren().add(muniCB);
            muniCB.setLayoutX(200);
            muniCB.setLayoutY(130);
        }

    }

    private void handleMunicipalitySelection() {
        String selectedIsland = (String) islandCB.getValue();
        String selectedMunicipality = (String) muniCB.getValue();

        if (selectedIsland != null && selectedMunicipality != null) {
            // Populate parishComboBox based on selected municipality
            Map<String, List<String>> municipalities = locationData.get(selectedIsland);
            List<String> parishes = municipalities.get(selectedMunicipality);
            ObservableList<String> parishList = FXCollections.observableArrayList(parishes);
            parishCB.setItems(parishList);

            // Clear parish selection
            parishCB.getSelectionModel().clearSelection();

            rootPane.getChildren().add(parishCB);
            parishCB.setLayoutX(200);
            parishCB.setLayoutY(160);
        }

        createNextButton();


    }

    private void buildLocationData() {

        System.out.println(propFileReader.getProperties().size());

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

    public void calcA1(MouseEvent actionEvent) throws Exception {
        System.out.println(location);
        processFile2();
        System.out.println(location);
        String Ilha = location.get("Ilha");
        String Municipio = location.get("Municipio");
        String Freguesia = location.get("Freguesia");

        System.out.println(Ilha + Municipio + Freguesia);

        if (Freguesia == null) {
            labelA1.setText(String.valueOf(new CalcAreas(new GraphStructure(propFileReader.getProperties(),4).getG()).calcArea3(Municipio,"Municipio")));
        } else {
            labelA1.setText(String.valueOf(new CalcAreas(new GraphStructure(propFileReader.getProperties(),4).getG()).calcArea3(Freguesia,"Freguesia")));

        }
    }

    public void calcA2(ActionEvent actionEvent) throws Exception {
        System.out.println(location);
        processFile2();
        System.out.println(location);
        String Ilha = location.get("Ilha");
        String Municipio = location.get("Municipio");
        String Freguesia = location.get("Freguesia");

        System.out.println(Ilha + Municipio + Freguesia);

        if (Freguesia == null) {
            labelA2.setText(String.valueOf(new CalcAreas(new GraphStructure(propFileReader.getProperties(),4).getG()).calcArea4(Municipio,"Municipio")));
        } else {
            labelA2.setText(String.valueOf(new CalcAreas(new GraphStructure(propFileReader.getProperties(),4).getG()).calcArea4(Freguesia,"Freguesia")));

        }
    }

    public void goBack5(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) goBack5.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Stage5.fxml"));
//        fxmlLoader.setController(this);
        this.setStage(stage);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Stage 5");
        stage.setScene(scene);
    }
}