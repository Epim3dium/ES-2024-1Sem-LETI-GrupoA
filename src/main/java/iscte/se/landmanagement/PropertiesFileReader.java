package iscte.se.landmanagement;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class PropertiesFileReader {
    private final InputStream inputStream;  // Alterado para InputStream
    private ArrayList<Property> properties;

    // Alterado para aceitar InputStream
    public PropertiesFileReader(InputStream inputStream) {
        this.inputStream = inputStream;
        properties = new ArrayList<>();
        readPropertiesFile();
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    /**
     * Reads all the content and transforms all the data of the Properties in an ArrayList
     */
    public void readPropertiesFile() {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        try (Scanner sc = new Scanner(inputStream)) {  // Usa o InputStream aqui
            if (sc.hasNextLine()) {
                sc.nextLine(); // Pular a primeira linha, se necessário
            }

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(";");
                if (parts.length == 7) {
                    int propertyId = Integer.parseInt(parts[0]);
                    double parcelID = Double.parseDouble(parts[1]);
                    int parcelNum = (int) Double.parseDouble(parts[2].replace(',', '.'));
                    double shapeLength = Double.parseDouble(parts[3]);
                    double shapeArea = Double.parseDouble(parts[4]);
                    coordinates = geometryCorners(parts[5]);
                    int ownerId = Integer.parseInt(parts[6]);
                    properties.add(new Property(propertyId, parcelID, parcelNum, shapeLength, shapeArea, coordinates, ownerId));
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    /**
     * Reads all the coordinates of the corners of the property
     *
     * @param line String that contains the coordinates
     * @return List of Coordinates representing the corners
     */
    private static ArrayList<Coordinates> geometryCorners(String line) {
        ArrayList<Coordinates> corners = new ArrayList<>();
        String cleanedLine = line.replace("MULTIPOLYGON", "").replace("(", "").replace(")", " ").trim();
        String[] coordinates = cleanedLine.split(",");

        for (String coordinate : coordinates) {
            String[] latitudeAndLongitude = coordinate.split(" ");
            if (latitudeAndLongitude.length == 2) {
                try {
                    double latitude = Double.parseDouble(latitudeAndLongitude[0]);
                    double longitude = Double.parseDouble(latitudeAndLongitude[1]);
                    corners.add(new Coordinates(latitude, longitude));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid coordinate " + coordinate);
                }
            }
        }
        return corners;
    }
}
