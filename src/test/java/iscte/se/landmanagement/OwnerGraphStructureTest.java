package iscte.se.landmanagement;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OwnerGraphStructureTest {
    private ArrayList<Property> properties;
    private GraphStructure graphExample;
    private OwnerGraphStructure ownerGraphExample;
    @BeforeEach
    void set() {

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

        ArrayList<Coordinates> coordinatesList4 = new ArrayList<>();
        coordinatesList4.add(new Coordinates(102.0, 200.0));
        coordinatesList4.add(new Coordinates(102.0, 200.5));
        coordinatesList4.add(new Coordinates(104.0, 200.5));
        coordinatesList4.add(new Coordinates(104.0, 200.0));
        ArrayList<Coordinates> coordinatesList5 = new ArrayList<>();
        coordinatesList5.add(new Coordinates(100.0, 200.0));
        coordinatesList5.add(new Coordinates(100.0, 202.0));
        coordinatesList5.add(new Coordinates(102.0, 202.0));
        coordinatesList5.add(new Coordinates(102.0, 200.0));
        properties = new ArrayList<>();

        Property p2 = new Property(2, 4, 5, 6, 7, coordinatesList2, 8, " ", " ", " ");
        Property p1 = new Property(1, 2, 3, 4, 5, coordinatesList1, 6, " ", " ", " ");
        Property p3 = new Property(3, 5, 6, 7, 8, coordinatesList3, 9, " ", " ", " ");
        Property p4 = new Property(4, 5, 6, 7, 8, coordinatesList4, 9, " ", " ", " ");
        Property p5 = new Property(5, 6, 7, 8, 9, coordinatesList5, 6, " ", " ", " ");


        this.properties.add(p1);
        this.properties.add(p2);
        this.properties.add(p3);
        this.properties.add(p4);
        this.properties.add(p5);


        graphExample = new GraphStructure(properties, 1);
        ownerGraphExample = new OwnerGraphStructure(graphExample.getG());
    }
    @Test
    void utils() {
        List<OwnerGraphStructure.PropertyPair> exchanges = ownerGraphExample.generateAllExchanges();
        //1-> 4    3 -> 5
        assertEquals(2, exchanges.size());
        List<Double> diffs = new ArrayList<>();
        for(OwnerGraphStructure.PropertyPair exchange : exchanges) {

            Pair<Double, Double> increases = OwnerGraphStructure.calcAvgAreaIncrease(ownerGraphExample, exchange);
            assertTrue(increases.getKey() > 0);
            assertTrue(increases.getValue() > 0);
            diffs.add(Math.abs(increases.getKey() - increases.getValue()));
        }
        assert(diffs.getFirst() > diffs.getLast());
    }

}