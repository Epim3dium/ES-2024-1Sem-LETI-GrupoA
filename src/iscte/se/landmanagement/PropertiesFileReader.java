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

    protected void FileReader(String filename) {
        try (Scanner sc = new Scanner(new File(filename))) {
            if (sc.hasNextLine()) {
                sc.nextLine();
            }

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(";");
                if (parts.length > 7) {
                    int propertyId = Integer.parseInt(parts[0]);
                    double parcelID = Double.parseDouble(parts[1]);
                    int parcelNum = Integer.parseInt(parts[2]);
                    double shapeLength = Double.parseDouble(parts[3]);
                    double shapeArea = Double.parseDouble(parts[4]);


                }

            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        }
    }

    /**
     * Reads all the coordinates of the corners of the property
     * @param line
     * @return
     */
    private static ArrayList<Coordinates> geometryCorners(String line) {
        ArrayList<Coordinates> corners = new ArrayList<>();
        String cleanedLine = line.replace("MULTIPOLYGON", "").replace("(", "").replace(")", " ").trim();
        String[] coordinates = cleanedLine.split(",");

        for(String coordinate: coordinates) {
            String[] latitudeAndLongitude = coordinate.split("");
            corners.add(new Coordinates(Double.parseDouble(latitudeAndLongitude[0]), Double.parseDouble(latitudeAndLongitude[1])));
        }


        return corners;
    }
}
