package iscte.se.landmanagement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesFileReaderTest {
    PropertiesFileReader prop_reader;

    @BeforeEach
    void setUp()
    { InputStream is = Main.class.getClassLoader().getResourceAsStream("test.csv");
        if (is == null) {
            System.out.println("Erro: O ficheiro Madeira-Moodle.csv não foi encontrado no classpath.");
            return;
        }
        prop_reader = new PropertiesFileReader(is);
    }
    @Test
    void readingFile() {
        assertDoesNotThrow(() -> {
            InputStream is = Main.class.getClassLoader().getResourceAsStream("test.csv");
            if (is == null) {
                System.out.println("Erro: O ficheiro Madeira-Moodle.csv não foi encontrado no classpath.");
                return;
            }
            new PropertiesFileReader(is);
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