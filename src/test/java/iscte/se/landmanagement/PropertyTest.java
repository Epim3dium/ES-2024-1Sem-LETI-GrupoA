package iscte.se.landmanagement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PropertyTest {

    @Test
    void initialization() {
        Coordinates c = new Coordinates(0, 0);
        Property prop = new Property(1, 2, 3, 4,
                5, new ArrayList<>(), 6, "Arco da Calheta","Calheta","Madeira");
        assertEquals(1, prop.getPropertyID());
        assertEquals(2, prop.getParcelID());
        assertEquals(3, prop.getParcelNum());
        assertEquals(5, prop.getShapeArea());
        assertEquals(6, prop.getOwnerID());
    }
    @Test
    void notFullInitialization() {
        Property prop = new Property(1, 200.0);
        assertEquals(1, prop.getOwnerID());
        assertEquals(200.0, prop.getShapeArea());
        assertEquals("", prop.getMunicipality());
        assertEquals("", prop.getParish());
    }
    @Test
    void stringification() {
        Property prop = new Property(1, 2, 3, 4,
                5, new ArrayList<>(), 6, "Arco da Calheta","Calheta","Madeira");
        assertEquals("1, Arco da Calheta, Calheta, Madeira", prop.toString());
    }
    @Test
    void settingAndGetting() {
        Coordinates c = new Coordinates(0, 0);
        Property prop = new Property(1, 2, 3, 4,
                5, new ArrayList<>(), 6, "Arco da Calheta","Calheta","Madeira");
        prop.setPropertyID(2);
        prop.setParcelID(2);
        prop.setOwnerID(2);
        assertEquals(2, prop.getPropertyID());
        assertEquals(2, prop.getParcelID());
        assertEquals(2, prop.getOwnerID());
    }

    @Test
    void testEquals() {
        Property prop1 = new Property(1, 2, 3, 4, 5, new ArrayList<>(), 6, "Arco da Calheta", "Calheta", "Madeira");
        Property prop2 = new Property(1, 2, 3, 4, 5, new ArrayList<>(), 6, "Arco da Calheta", "Calheta", "Madeira");
        Property prop3 = new Property(2, 2, 3, 4, 5, new ArrayList<>(), 6, "Arco da Calheta", "Calheta", "Madeira");

        assertTrue(prop1.equals(prop2));
        assertFalse(prop1.equals(prop3));
    }

    @Test
    void testHashCode() {
        Property prop1 = new Property(1, 2, 3, 4, 5, new ArrayList<>(), 6, "Arco da Calheta", "Calheta", "Madeira");
        Property prop2 = new Property(1, 2, 3, 4, 5, new ArrayList<>(), 6, "Arco da Calheta", "Calheta", "Madeira");

        assertEquals(prop1.hashCode(), prop2.hashCode());
    }

    @Test
    void testAddAndGetNeighbours() {
        Property prop1 = new Property(1, 2, 3, 4, 5, new ArrayList<>(), 6, "Arco da Calheta", "Calheta", "Madeira");
        Property prop2 = new Property(2, 3, 4, 5, 6, new ArrayList<>(), 7, "Ponta do Sol", "Ponta do Sol", "Madeira");

        prop1.addNeighbour(prop2);

        ArrayList<Property> neighbours = prop1.getNeighbours();
        assertEquals(1, neighbours.size());
        assertEquals(prop2, neighbours.get(0));
    }


}