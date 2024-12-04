package iscte.se.landmanagement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AABBTest {
    @Test
    void generalUtils() {
        AABB aabb = new AABB(100, -100, -200, 300);
        Coordinates area = aabb.area();
        assertEquals(500, area.getX());
        assertEquals(200, area.getY());

        Coordinates position = aabb.center();
        assertEquals(50, position.getX());
        assertEquals(0, position.getY());
    }
    @Test
    void expanding() {
        AABB aabb = new AABB();
        aabb.expandToContain(new Coordinates(10, -10));
        aabb.expandToContain(new Coordinates(-10, 10));
        assertEquals(new Coordinates(0, 0), aabb.center());
        aabb.expandToContain(new Coordinates(-190, -290));
        assertEquals(new Coordinates(-90, -140), aabb.center());
        assertEquals(new Coordinates(200, 300), aabb.area());
        aabb.expandToContain(new Coordinates(0, 0));
        assertEquals(new Coordinates(-90, -140), aabb.center());
        assertEquals(new Coordinates(200, 300), aabb.area());

    }
    @Test
    void overlapping() {
        AABB aabb1 = new AABB(100, -100, -100, 100);
        AABB aabb2 = new AABB(-110, -190, -50, 150);
        assertFalse(aabb1.isOverlapping(aabb2, 0));
        assertTrue(aabb1.isOverlapping(aabb2, 20));
        aabb2.expandToContain(new Coordinates(0, 0));
        assertTrue(aabb1.isOverlapping(aabb2, 0));
        assertTrue(aabb2.isOverlapping(aabb1, 0));
        assertTrue(aabb1.isOverlapping(aabb1, 0));

    }
}