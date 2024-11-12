package iscte.se.landmanagement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {
    @Test
    void initialization() {
        Coordinates coords = new Coordinates(1, 2);
        assertTrue(coords.getX() == 1);
        assertTrue(coords.getY() == 2);
    }
    @Test
    void settingAndGetting() {
        Coordinates coords = new Coordinates(0, 0);
        coords.setX(21);
        coords.setY(37);
        assertTrue(coords.getX() == 21);
        assertTrue(coords.getY() == 37);
    }
}