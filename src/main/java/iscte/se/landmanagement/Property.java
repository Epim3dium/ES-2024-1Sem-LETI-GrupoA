package iscte.se.landmanagement;

import java.util.ArrayList;

/**
 * Represents a property with geographic and ownership details.
 * Each property includes attributes such as area, perimeter, corners (coordinates),
 * ownership information, and neighboring properties.
 */
public class Property {
    private int propertyID;
    private double parcelID;
    private double parcelNum;
    private double shapeLength;
    private double shapeArea;
    private ArrayList<Coordinates> corners;
    private int ownerID;
    private String parish;
    private String municipality;
    private String island;
    private ArrayList<Property> neighbors;

    /**
     * Constructs a {@code Property} with full details including coordinates and neighbors.
     *
     * @param propertyID   The unique identifier for the property.
     * @param parcelID     The parcel ID associated with the property.
     * @param parcelNum    The parcel number within its region.
     * @param shapeLength  The perimeter of the property in appropriate units.
     * @param shapeArea    The area of the property in appropriate units.
     * @param corners      A list of {@link Coordinates} representing the property's boundaries.
     * @param ownerID      The unique identifier of the property's owner.
     * @param parish       The parish where the property is located.
     * @param municipality The municipality where the property is located.
     * @param island       The island where the property is located.
     */
    public Property(int propertyID, double parcelID, double parcelNum, double shapeLength, double shapeArea, ArrayList<Coordinates> corners, int ownerID, String parish, String municipality, String island) {
        this.propertyID = propertyID;
        this.parcelID = parcelID;
        this.parcelNum = parcelNum;
        this.shapeLength = shapeLength;
        this.shapeArea = shapeArea;
        this.corners = corners;
        this.ownerID = ownerID;
        this.parish = parish;
        this.municipality = municipality;
        this.island = island;
        this.neighbors = new ArrayList<Property>();
    }

    /**
     * Constructs a simplified {@code Property} for an owner with aggregated area information.
     * Primarily used for summarizing properties owned by the same owner.
     *
     * @param ownerID The unique identifier of the owner.
     * @param areaSum The total area of properties owned by the owner.
     */
    public Property(int ownerID, double areaSum) {
        this.ownerID = ownerID;
        this.parish = "";
        this.municipality = "";
        this.island = "";
        this.parcelID = 0;
        this.parcelNum = areaSum;
        this.shapeLength = 0;
        this.shapeArea = areaSum;
        this.neighbors = new ArrayList<Property>();

    }

    /**
     * Returns a string representation of the property with basic identifying information.
     *
     * @return A string containing the property ID, parish, municipality, and island.
     */
    @Override
    public String toString() {
        return propertyID + ", " + parish + ", " + municipality + ", " + island;
    }

    /**
     * Checks if this property is equal to another property based on its unique ID.
     *
     * @param obj The property to compare with.
     * @return {@code true} if the properties have the same ID, {@code false} otherwise.
     */
    public boolean equals(Property obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return propertyID == ((Property) obj).propertyID;
    }

    /**
     * Generates a hash code for the property based on its ID.
     *
     * @return The hash code of the property.
     */
    @Override
    public int hashCode() {
        return propertyID;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }

    public double getParcelID() {
        return parcelID;
    }

    public void setParcelID(double parcelID) {
        this.parcelID = parcelID;
    }

    public double getParcelNum() {
        return parcelNum;
    }

    public void setParcelNum(int parcelNum) {
        this.parcelNum = parcelNum;
    }

    public double getShapeLength() {
        return shapeLength;
    }

    public void setShapeLength(double shapeLength) {
        this.shapeLength = shapeLength;
    }

    public double getShapeArea() {
        return shapeArea;
    }

    public void setShapeArea(double shapeArea) {
        this.shapeArea = shapeArea;
    }

    public ArrayList<Coordinates> getCorners() {
        return corners;
    }

    public void setCorners(ArrayList<Coordinates> corners) {
        this.corners = corners;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }

    // Neighbor Management

    /**
     * Adds a neighboring property to this property.
     *
     * @param p The neighboring property to add.
     */
    public void addNeighbour(Property p) {
        neighbors.add(p);
    }

    /**
     * Retrieves the list of neighboring properties.
     *
     * @return An {@code ArrayList} of neighboring {@link Property} objects.
     */
    public ArrayList<Property> getNeighbours() {
        return this.neighbors;
    }
}

