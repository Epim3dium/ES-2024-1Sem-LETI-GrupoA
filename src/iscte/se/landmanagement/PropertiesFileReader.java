package iscte.se.landmanagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PropertiesFileReader {
    private final String filename;
    private ArrayList<Property> properties;

    public PropertiesFileReader() {
        filename = "Madeira-Moodle.csv";
        properties = new ArrayList<>();
    }

    private void FileReader() {
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
     * Reads all the corners of the property and returns them
     * @param line
     * @return
     */
    private static ArrayList<Coordinates> geometryCorners(String line) {
        ArrayList<Coordinates> corners = new ArrayList<>();
        String cleanedLine = line.replace("MULTIPOLYGON", "").replace("(", "").replace(")", " ").trim();
        String[] parts = cleanedLine.split(",");


        return corners;
    }
}
