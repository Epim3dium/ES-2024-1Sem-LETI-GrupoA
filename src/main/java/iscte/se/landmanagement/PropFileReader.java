package iscte.se.landmanagement;


import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropFileReader {
    private final Path filePath;
    private ArrayList<Property> properties;
    private List<String[]> fileLines;

    public PropFileReader(Path filePath) {
        this.filePath = filePath;
        this.properties = new ArrayList<>();
        this.fileLines = new ArrayList<>();
    }

    public List<String[]> getFileLines() {
        return fileLines;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public void readFile() throws Exception {
        List<String[]> lines = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

        try (Reader reader = Files.newBufferedReader(filePath);
             CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build()) {
            csvReader.skip(1);
            String[] fileline;
            while ((fileline = csvReader.readNext()) != null) {
                lines.add(fileline);
            }
        }
        fileLines = lines;
    }

    public void convertToPropertiy() {


        for (String[] lines : fileLines) {
            ArrayList<Coordinates> coords = readCoordinates(lines[5].trim());
            if (coords.isEmpty()) {
                continue;
            }

            int propertyId = Integer.parseInt(lines[0].trim());
            double parcelId = Double.parseDouble(lines[1].trim());
            double parcelNum = Double.parseDouble(lines[2].replace(',', '.').trim());
            double shapeLength = Double.parseDouble(lines[3].trim());
            double shapeArea = Double.parseDouble(lines[4].trim());
            int ownerId = Integer.parseInt(lines[6].trim());
            String parish = lines[7].trim();
            String municipality = lines[8].trim();
            String island = lines[9].trim();

            properties.add(new Property(propertyId, parcelId, parcelNum, shapeLength, shapeArea, coords, ownerId, parish, municipality, island));
        }


    }

    public ArrayList<Coordinates> readCoordinates(String line) {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        if (line.contains("MULTIPOLYGON EMPTY")) {
            return coordinates;
        }
        line = line.replace("MULTIPOLYGON", "")
                .replace("(", "")
                .replace(")", "")
                .trim();
        String[] cornersCoordinates = line.split(",");
        for (int i = 0; i < cornersCoordinates.length; i++) {
            cornersCoordinates[i] = cornersCoordinates[i].trim();
            String[] coordinate = cornersCoordinates[i].split("\\s+");
            coordinates.add(new Coordinates(Double.parseDouble(coordinate[0].trim()), Double.parseDouble(coordinate[1].trim())));
        }
        return coordinates;
    }

}