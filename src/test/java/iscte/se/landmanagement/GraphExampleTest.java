package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class GraphExampleTest {

    private ArrayList<Property> properties;
    private GraphExample graphExample;


    @BeforeEach
    void set(){

        ArrayList<Coordinates> coordinatesList1 = new ArrayList<>();
        coordinatesList1.add(new Coordinates(0, 0));
        coordinatesList1.add(new Coordinates(1, 0));
        coordinatesList1.add(new Coordinates(1, 1));
        coordinatesList1.add(new Coordinates(0, 1));
        ArrayList<Coordinates> coordinatesList2 = new ArrayList<>();
        coordinatesList2.add(new Coordinates(2, 1));
        coordinatesList2.add(new Coordinates(3, 2));
        coordinatesList2.add(new Coordinates(2, 0));
        coordinatesList2.add(new Coordinates(3, 0));
        ArrayList<Coordinates> coordinatesList3 = new ArrayList<>();
        coordinatesList3.add(new Coordinates(0, 0));
        coordinatesList3.add(new Coordinates(1, 3));
        coordinatesList3.add(new Coordinates(2, 3));
        coordinatesList3.add(new Coordinates(2, 2));
        properties = new ArrayList<>();

        Property p1 = new Property(1, 2, 3, 4,
                5, coordinatesList1, 6);
        Property p2 = new Property(3, 4, 5, 6,
                7, coordinatesList2, 8);
        Property p3 = new Property(4, 5, 6, 7,
                8, coordinatesList3, 9);


        this.properties.add(p1);
        this.properties.add(p2);
        this.properties.add(p3);

        graphExample = new GraphExample(properties);
    }

    @Test
    void GraphFormTest() {
        Graph<Property, DefaultEdge> graph = graphExample.getG();
        assertEquals(3,graph.vertexSet().size());
        assertTrue(graph.vertexSet().containsAll(this.properties));

        assertFalse(graph.edgeSet().isEmpty());

    }

    @Test

    void AdjencyByDistanceTest() {
        Property p1 = this.properties.get(0);
        Property p2 = this.properties.get(1);
        Property p3 = this.properties.get(2);

        assertTrue(graphExample.areAdjacentByDistance(p1,p3,2));
        assertFalse(graphExample.areAdjacentByDistance(p1,p2,2));
    }
    




}
