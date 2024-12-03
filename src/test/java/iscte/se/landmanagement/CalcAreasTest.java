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

        assertNotNull(url, "CSV file is missing!");

        Path path = Paths.get(url.toURI());

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
        assertEquals(914.5761711609113,d,0.001);

    }

    @Test
    void calcArea4() {
        String AreaN = "Calheta";
        String AreaT = "Municipio";
        CalcAreas c = new CalcAreas(graph);
        double d = c.calcArea4(AreaN, AreaT);
        assertEquals(917.6018973557536,d,0.001);
    }

    @Test
    void testCalcArea3EmptyGraph() {
        Graph<Property, DefaultEdge> emptyGraph = new GraphStructure(new ArrayList<>(), 1).getG();
        CalcAreas calcAreas = new CalcAreas(emptyGraph);
        double result = calcAreas.calcArea3("Something", "Municipio");
        assertEquals(0.0, result, "calcArea3 should return 0.0 for an empty graph.");
    }

    @Test
    void testCalcArea4EmptyGraph() {
        Graph<Property, DefaultEdge> emptyGraph = new GraphStructure(new ArrayList<>(), 1).getG();
        CalcAreas calcAreas = new CalcAreas(emptyGraph);
        double result = calcAreas.calcArea4("Something", "Municipio");
        assertEquals(0.0, result, "calcArea3 should return 0.0 for an empty graph.");
    }
}