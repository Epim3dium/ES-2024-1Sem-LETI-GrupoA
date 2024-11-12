package iscte.se.landmanagement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesFileReaderTest {
    PropertiesFileReader prop_reader;
    @BeforeEach
    void setUp() {
        prop_reader = new PropertiesFileReader("src/main/resources/test.csv");
    }
    @Test
    void readingFile() {
        assertDoesNotThrow(() -> {
            new PropertiesFileReader("src/main/resources/test.csv");
        });
    }
    @Test
    void initialization() {
        Property first = prop_reader.getProperties().get(0);

        assertEquals(1, first.getPropertyID());
        assertEquals(93, first.getOwnerID());
        assertEquals(202.05981432070362, first.getShapeArea());

        Property second = prop_reader.getProperties().get(1);
        assertEquals(2, second.getPropertyID());

        Property sixth = prop_reader.getProperties().get(5);
        assertEquals(6, sixth.getPropertyID());
    }

    @AfterEach
    void tearDown() {
        prop_reader = null;
    }
}