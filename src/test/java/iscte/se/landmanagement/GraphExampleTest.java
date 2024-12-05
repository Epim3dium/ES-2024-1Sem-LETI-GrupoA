package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class GraphExampleTest {

    private ArrayList<Property> properties;
    private GraphStructure graphExample;


    @BeforeEach
    void set(){

        ArrayList<Coordinates> coordinatesList1 = new ArrayList<>();
        coordinatesList1.add(new Coordinates(10.0, 20.0));
        coordinatesList1.add(new Coordinates(10.0, 22.0));
        coordinatesList1.add(new Coordinates(12.0, 22.0));
        coordinatesList1.add(new Coordinates(12.0, 20.0));
        ArrayList<Coordinates> coordinatesList2 = new ArrayList<>();
        coordinatesList2.add(new Coordinates(2, 1));
        coordinatesList2.add(new Coordinates(3, 2));
        coordinatesList2.add(new Coordinates(2, 0));
        coordinatesList2.add(new Coordinates(3, 0));
        ArrayList<Coordinates> coordinatesList3 = new ArrayList<>();
        coordinatesList3.add(new Coordinates(12.0, 20.0));
        coordinatesList3.add(new Coordinates(12.0, 22.0));
        coordinatesList3.add(new Coordinates(14.0, 22.0));
        coordinatesList3.add(new Coordinates(14.0, 20.0));
        properties = new ArrayList<>();

        Property p1 = new Property(1, 2, 3, 4, 5, coordinatesList1, 6," ", " "," ");
        Property p2 = new Property(3, 4, 5, 6, 7, coordinatesList2, 8," ", " "," ");
        Property p3 = new Property(4, 5, 6, 7, 8, coordinatesList3, 9," ", " "," ");


        this.properties.add(p1);
        this.properties.add(p2);
        this.properties.add(p3);

        graphExample = new GraphStructure(properties,1);
    }

    @Test
    void ConstructorTest() {

        GraphStructure g = new GraphStructure(properties, 2);
        assertEquals(3, g.getG().vertexSet().size());
        assertEquals(new ArrayList<>(this.properties), new ArrayList<>(g.getG().vertexSet()));



        ArrayList<Property> emptyProperties = new ArrayList<>();
        g = new GraphStructure(emptyProperties, 1);
        assertEquals(0, g.getG().vertexSet().size());


        //assertThrows(NullPointerException.class, () -> new GraphStructure(null, 1));
    }


    @Test
    void GraphFormTest() {
        Graph<Property, DefaultEdge> graph = graphExample.getG();
        assertEquals(3,graph.vertexSet().size());
        assertTrue(graph.vertexSet().containsAll(this.properties));

        assertFalse(graph.edgeSet().isEmpty());
        assertEquals(1, graph.edgeSet().size());
    }

    @Test

    void AdjencyByDistanceTest() {
        Property p1 = this.properties.get(0);
        Property p2 = this.properties.get(1);
        Property p3 = this.properties.get(2);

        assertTrue(graphExample.areAdjacentByDistance(p1,p3));
        assertFalse(graphExample.areAdjacentByDistance(p1,p2));
    }

    @Test
    void AddNeighboursTest() {
        Property p1 = properties.get(0);
        Property p3 = properties.get(2);

        graphExample.areAdjacentByDistance(p1, p3);
        assertTrue(p1.getNeighbours().contains(p3));
        assertTrue(p3.getNeighbours().contains(p1));
    }

    void CalculateDistanceTest() {
        Coordinates c1 = new Coordinates(0, 0);
        Coordinates c2 = new Coordinates(3, 4);
        assertEquals(5.0, GraphStructure.calculateDistance(c1, c2), 0.01);


        assertEquals(0.0, GraphStructure.calculateDistance(c1, c1), 0.01);


        Coordinates c3 = new Coordinates(-3, -4);
        assertEquals(10.0, GraphStructure.calculateDistance(c1, c3), 0.01);
    }






}
