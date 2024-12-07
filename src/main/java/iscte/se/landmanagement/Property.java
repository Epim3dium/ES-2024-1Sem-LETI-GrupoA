package iscte.se.landmanagement;
import java.util.ArrayList;
import java.util.Collection;

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

    public Property(int ownerID, double areaSum) {
        this.ownerID = ownerID;
        this.parish = "";
        this.municipality = "";
        this.island = "";
        this.parcelNum = 0;
        this.parcelID = 0;
        this.parcelNum = areaSum;
        this.shapeLength = 0;
        this.shapeArea = areaSum;
        this.neighbors = new ArrayList<Property>();

    }
    @Override
    public String toString() {
        return propertyID + ", " + parish + ", " + municipality + ", " + island;
    }
    public boolean equals(Property obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return propertyID == ((Property) obj).propertyID;
    }

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

    public void addNeighbour(Property p) {
        neighbors.add(p);
    }

    public ArrayList<Property> getNeighbours() {
        return this.neighbors;
    }
}

