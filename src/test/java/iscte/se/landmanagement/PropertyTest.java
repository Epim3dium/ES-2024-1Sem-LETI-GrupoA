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

}