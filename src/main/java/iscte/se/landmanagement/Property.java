package iscte.se.landmanagement;
import java.util.ArrayList;

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
}

