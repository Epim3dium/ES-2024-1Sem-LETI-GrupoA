package iscte.se.landmanagement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {
    @Test
    void initialization() {
        Coordinates coords = new Coordinates(1, 2);
        assertEquals(1, coords.getX());
        assertEquals(2, coords.getY());
    }
    @Test
    void settingAndGetting() {
        Coordinates coords = new Coordinates(0, 0);
        coords.setX(21);
        coords.setY(37);
        assertEquals(21, coords.getX());
        assertEquals(37, coords.getY());
    }
}