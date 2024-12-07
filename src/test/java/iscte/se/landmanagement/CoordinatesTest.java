package iscte.se.landmanagement;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @Test
    void testAvgMultipleCoordinates() {
        List<Coordinates> coordsList = Arrays.asList(new Coordinates(1, 2), new Coordinates(3, 4), new Coordinates(5, 6));
        Coordinates avgCoords = Coordinates.avg(coordsList);

        assertEquals(3.0, avgCoords.getX(), 0.001);
        assertEquals(4.0, avgCoords.getY(), 0.001);
    }

    @Test
    void testAvgSingleCoordinate() {
        List<Coordinates> coordsList = Collections.singletonList(new Coordinates(10, 20));
        Coordinates avgCoords = Coordinates.avg(coordsList);

        assertEquals(10.0, avgCoords.getX(), 0.001);
        assertEquals(20.0, avgCoords.getY(), 0.001);
    }

    @Test
    void testAvgEmptyList() {
        List<Coordinates> coordsList = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> Coordinates.avg(coordsList));
    }

    @Test
    void testAvgNullList() {
        List<Coordinates> coordsList = null;
        assertThrows(NullPointerException.class, () -> Coordinates.avg(coordsList));
    }
}