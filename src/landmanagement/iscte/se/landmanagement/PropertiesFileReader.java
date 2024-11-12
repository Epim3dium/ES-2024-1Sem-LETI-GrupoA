package iscte.se.landmanagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PropertiesFileReader {
    private final String filename;
    private ArrayList<Property> properties;

    public PropertiesFileReader(String filename) {
        this.filename = filename;
        properties = new ArrayList<>();
    }

    /**
     * Reads all the content and transforms all the data of the Properties in an ArrayList
     */
    public void readPropertiesFile() {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filename))) {
            if (sc.hasNextLine()) {
                sc.nextLine();
            }

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(";");
                if (parts.length == 7) {
                    int propertyId = Integer.parseInt(parts[0]);
                    double parcelID = Double.parseDouble(parts[1]);
                    int parcelNum = Integer.parseInt(parts[2]);
                    double shapeLength = Double.parseDouble(parts[3]);
                    double shapeArea = Double.parseDouble(parts[4]);
                    coordinates = geometryCorners(parts[5]);
                    int ownerId = Integer.parseInt(parts[6]);
                    properties.add(new Property(propertyId, parcelID, parcelNum, shapeLength, shapeArea, coordinates, ownerId));
                }

            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
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
