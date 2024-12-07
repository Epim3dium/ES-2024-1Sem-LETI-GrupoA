package iscte.se.landmanagement;

import java.util.List;

/**
 * Represents a point in a 2D coordinate system.
 * Provides methods for accessing and modifying the X and Y values,
 * calculating the average of multiple coordinates, and comparing coordinates.
 */
public class Coordinates {
    private double x;
    private double y;


    /**
     * Constructs a {@link Coordinates} object with the specified X and Y values.
     *
     * @param x the X value.
     * @param y the Y value.
     */
    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the X value of the coordinate.
     *
     * @return the X value.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the X value of the coordinate.
     *
     * @param x the new X value.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the Y value of the coordinate.
     *
     * @return the Y value.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the Y value of the coordinate.
     *
     * @param y the new Y value.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns a string representation of the coordinate.
     * The format is "Coordinates: x-> [X value], y-> [Y value]".
     *
     * @return a string representing the coordinate.
     */
    @Override
    public String toString() {
        return "Coordinates:" + "x-> " + x + ", y-> " + y + " " ;
    }

    /**
     * Checks if this {@link Coordinates} object is equal to another object.
     * Two {@link Coordinates} objects are considered equal if their X and Y values
     * are exactly the same.
     *
     * @param obj the object to compare with this coordinate.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coordinates coords = (Coordinates) obj;
        if(coords.x != this.x) {
            return false;
        }
        return coords.y == this.y;
    }

    /**
     * Calculates the average of a list of coordinates.
     * The average is computed by summing the X and Y values of all coordinates
     * in the list and dividing by the number of coordinates.
     *
     * @param coords the list of {@link Coordinates} to average.
     *               Must not be null or empty.
     * @return a {@link Coordinates} object representing the average X and Y values
     *         of the input coordinates.
     * @throws IllegalArgumentException if the list is null or empty.
     */
    static Coordinates avg(List<Coordinates> coords) {
        if(coords.size() == 0) throw new IllegalArgumentException();
        Coordinates result = new Coordinates(0, 0);
        for (Coordinates coord : coords) {
            result.setX(result.getX() + coord.getX());
            result.setY(result.getY() + coord.getY());
        }
        result.setX(result.getX() / coords.size());
        result.setY(result.getY() / coords.size());
        return result;
    }
}
