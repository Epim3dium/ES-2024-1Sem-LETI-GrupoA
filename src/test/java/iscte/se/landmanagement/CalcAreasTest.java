package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
class CalcAreasTest {
    private Graph<Property, DefaultEdge> graph;

    @BeforeEach
    void setUp() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("Madeira-Moodle-1.1.csv");
        Path path = Paths.get(url.toURI());
        if (url == null) {
            System.out.println("Arquivo CSV não encontrado!");
            return;
        }
        PropFileReader propFileReader = new PropFileReader(path);
        propFileReader.readFile();
        propFileReader.convertToPropertiy();

        GraphStructure g = new GraphStructure(propFileReader.getProperties(), 1);
        this.graph = g.getG();

    }

    @Test
    void calcArea3() {
        String AreaN = "Calheta";
        String AreaT = "Municipio";
        CalcAreas c = new CalcAreas(graph);
        double d = c.calcArea3(AreaN, AreaT);
        assertEquals(914.5761711609113,d);

    }

    @Test
    void calcArea4() {
        String AreaN = "Calheta";
        String AreaT = "Municipio";
        CalcAreas c = new CalcAreas(graph);
        double d = c.calcArea4(AreaN, AreaT);
        assertEquals(917.3995595614084,d);
    }
}