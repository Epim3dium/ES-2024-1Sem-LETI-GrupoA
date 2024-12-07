package iscte.se.landmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropFileReaderTest {
    private PropFileReader propFileReader;

    @BeforeEach
    void setUp() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("Madeira-Moodle-1.1.csv");
        if (url == null) {
            System.out.println("Arquivo CSV não encontrado!");
            return;
        }

        Path testFilePath = Paths.get(url.toURI());
        propFileReader = new PropFileReader(testFilePath);
        propFileReader.readFile();
    }

    @Test
    void readFile() {
        List<String[]> fileLines = propFileReader.getFileLines();

        assertNotNull(fileLines);
        assertFalse(fileLines.isEmpty());
        assertEquals(35045, fileLines.size());

    }

    @Test
    void readCoordinates() {
        String line = "299218.5203999998 3623637.4791, 299218.5033999998 3623637.4715, 299218.04000000004 3623638.4800000004, 299232.7400000002 3623644.6799999997, 299236.6233999999 3623637.1974, 299236.93709999975 3623636.7885999996, 299238.04000000004 3623633.4800000004, 299222.63999999966 3623627.1799999997, 299218.5203999998 3623637.4791";
        ArrayList<Coordinates> coords = propFileReader.readCoordinates(line);
        assertNotNull(coords);
        assertEquals(9, coords.size());
        assertEquals(299218.5203999998, coords.getFirst().getX());
        assertEquals(3623637.4791, coords.get(0).getY());

        String emptyLine = "MULTIPOLYGON EMPTY";
        ArrayList<Coordinates> emptyCoords = propFileReader.readCoordinates(emptyLine);
        assertNotNull(emptyCoords);
        assertTrue(emptyCoords.isEmpty());
    }

    @Test
    void convertToPropertiy() {
        propFileReader.convertToPropertiy();
        ArrayList<Property> props = propFileReader.getProperties();

        assertNotNull(props);
        assertEquals(35043, props.size());
        Property prop = props.get(0);
        assertEquals(1, prop.getPropertyID());
        assertEquals(93, prop.getOwnerID());
    }

}