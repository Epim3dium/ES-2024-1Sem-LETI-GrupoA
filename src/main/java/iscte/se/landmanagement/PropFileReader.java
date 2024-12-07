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

/**
 * A utility class for reading and parsing property data from a `.csv` file.
 * The file contains information about properties, including their attributes and coordinates.
 */
public class PropFileReader {
    private final Path filePath;
    private ArrayList<Property> properties;
    private List<String[]> fileLines;

    /**
     * Initializes a reader for a property `.csv` file based on the given file path.
     *
     * @param filePath The path to the `.csv` file to be read.
     */
    public PropFileReader(Path filePath) {
        this.filePath = filePath;
        this.properties = new ArrayList<>();
        this.fileLines = new ArrayList<>();
    }

    /**
     * Retrieves the raw lines read from the `.csv` file.
     *
     * @return A list of string arrays, where each array represents a row in the `.csv` file.
     */
    public List<String[]> getFileLines() {
        return fileLines;
    }

    /**
     * Retrieves the list of properties parsed from the `.csv` file.
     *
     * @return An {@code ArrayList} of {@link Property} objects.
     */
    public ArrayList<Property> getProperties() {
        return properties;
    }

    /**
     * Reads the `.csv` file and parses its contents into raw lines.
     * The parser uses `;` as the delimiter, as defined in the file format.
     *
     * @throws Exception If the file cannot be read (e.g., invalid path or file format issues).
     */
    public void readFile() throws Exception {
        List<String[]> lines = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

        try (Reader reader = Files.newBufferedReader(filePath); CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build()) {
            csvReader.skip(1);
            String[] fileline;
            while ((fileline = csvReader.readNext()) != null) {
                lines.add(fileline);
            }
        }
        fileLines = lines;
    }

    /**
     * Converts a string of coordinates from the `.csv` file into a list of {@link Coordinates} objects.
     *
     * @param line A string containing the coordinates of property corners.
     * @return An {@code ArrayList} of {@link Coordinates} representing the corners of the property.
     */
    protected ArrayList<Coordinates> readCoordinates(String line) {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        if (line.contains("MULTIPOLYGON EMPTY")) {
            return coordinates;
        }
        //Replacing the unnecessary content with empty spaces
        line = line.replace("MULTIPOLYGON", "").replace("(", "").replace(")", "").trim();
        String[] cornersCoordinates = line.split(",");
        for (int i = 0; i < cornersCoordinates.length; i++) {

            cornersCoordinates[i] = cornersCoordinates[i].trim();
            String[] coordinate = cornersCoordinates[i].split("\\s+");
            coordinates.add(new Coordinates(Double.parseDouble(coordinate[0].trim()), Double.parseDouble(coordinate[1].trim())));
        }
        return coordinates;
    }

    /**
     * Converts the raw data read from the `.csv` file into a list of {@link Property} objects.
     * The method validates the data to ensure properties have valid coordinates and the correct number of attributes.
     */
    public void convertToPropertiy() {


        for (String[] lines : fileLines) {

            ArrayList<Coordinates> coords = readCoordinates(lines[5].trim());
            // If there is no coordinates assembled to a property it will be discarded
            if (coords.isEmpty()) {
                continue;
            }
            //If a line of the .csv doesn't have the correct amount of columns it will  be discarded
            if (lines.length == 10) {
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
            } else {
                System.err.println("The file doesn't have the required size of columns");
                System.exit(1);
            }

        }
    }
}